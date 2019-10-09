package captainmk.antistripmine;

import captainmk.antistripmine.world.gen.OreGenHandler;
import captainmk.antistripmine.world.gen.OreGenerator;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid= Reference.MODID, name= Reference.MODNAME, version= Reference.VERSION, acceptedMinecraftVersions= Reference.ACCEPTED_MINECRAFT_VERSIONS)
public class AntiStripMine {
    @Instance
    public static AntiStripMine instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println(Reference.MODID + ":preInit");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        OreGenHandler oreGen = new OreGenHandler();
        MinecraftForge.ORE_GEN_BUS.register(oreGen);
        GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
        System.out.println(Reference.MODID + ":init");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println(Reference.MODID + ":postInit");
    }
}