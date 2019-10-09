package captainmk.antistripmine.world.gen;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class OreGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch(world.provider.getDimension()) {
            // Nether
            case -1:
                break;
            // Overworld
            case 0:
                //TODO: Ideally this should iterate through a list of ores that run through the generator, so that other mods can use it
                runGenerator(Blocks.COAL_ORE.getDefaultState(), 17, 20, 1, 128, BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);
                runGenerator(Blocks.IRON_ORE.getDefaultState(), 9, 20, 1, 64, BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);
                runGenerator(Blocks.GOLD_ORE.getDefaultState(), 9, 2, 1, 32, BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);
                if (world.getBiome(new BlockPos(chunkX*16, 64, chunkZ*16)) instanceof BiomeMesa)
                    runGenerator(Blocks.GOLD_ORE.getDefaultState(), 9, 20, 32, 79, BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);
                runGenerator(Blocks.REDSTONE_ORE.getDefaultState(), 8, 8, 1, 16, BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);
                runGenerator(Blocks.DIAMOND_ORE.getDefaultState(), 8, 1, 1, 16, BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);
                runGenerator(Blocks.LAPIS_ORE.getDefaultState(), 9, 1, 5, 25, BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);
                break;
            // End
            case 1:
                break;
            default:
        }
    }

    // Generates ore vein if that vein is in a valid position (next to cave, water, lava, or another vein
    private void runGenerator(IBlockState blockToGen, int blockAmount, int chancesToSpawn, int minHeight, int maxHeight, Predicate<IBlockState> blockToReplace, World world, Random rand, int chunk_X, int chunk_Z){
        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        WorldGenMinable generator = new WorldGenMinable(blockToGen, blockAmount, blockToReplace);
        int heightdiff = maxHeight - minHeight +1;
        for (int n=0; n<chancesToSpawn; n++){
            int x = chunk_X * 16 +rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightdiff);
            int z = chunk_Z * 16 + rand.nextInt(16);

            if (isValidSpawn(x, y, z, world, blockAmount)) {
                BlockPos orePos = new BlockPos(x, y, z);
                generator.generate(world, rand, orePos);
            }
        }
    }

    // Tests if an ore spawning position is valid
    private boolean isValidSpawn(int x, int y, int z, World world, int blockAmount) {
        // Define ore boundaries based on vein size. Sometimes it comes up one block short on larger veins, but it
        // does not affect ore generation significantly
        int sizeOffset = (blockAmount-1)/8;
        int x1 = (x+9) + sizeOffset;
        int x2 = (x+6) - sizeOffset;
        int y1 = Math.min((y + 1), 255);
        int y2 = Math.max((y - 3 - sizeOffset), 1);
        int z1 = (z+9) + sizeOffset;
        int z2 = (z+6) - sizeOffset;
        boolean xTreme, yTreme, zTreme;     // true if x, y, and (x&&y) are on edge of boundary box
        BlockPos veinPos;

        // Tests for air, ore, water, or lava in a rounded boundary box that is ~1 block out from edge of vein
        for (int i = x2; i <= x1; i++) {
            xTreme = (i == x1 || i == x2);  // X coord is on edge of boundary box
            for (int j = y2; j <= y1; j++) {
                yTreme = (j == y1 || j == y2);  // Y coord is on edge of boundary box
                if (xTreme && yTreme) continue;
                zTreme = xTreme || yTreme;
                for (int k = z2; k <= z1; k++) {
                    if (zTreme && (k == z1 || k==z2)) continue; // Z is on edge AND one other coordinate is extreme

                    veinPos = new BlockPos(i,j,k);
                    Block block = world.getBlockState(veinPos).getBlock();
                    if (block == Blocks.AIR || block instanceof BlockOre || block instanceof BlockLiquid) return true;
                }
            }
        }
        return false;
    }
}
