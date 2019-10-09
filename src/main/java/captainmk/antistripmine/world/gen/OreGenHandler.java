package captainmk.antistripmine.world.gen;

import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OreGenHandler {
    @SubscribeEvent
    public void onGenerateMinable (OreGenEvent.GenerateMinable event) {
        switch (event.getType()) {
            case CUSTOM: case QUARTZ: case EMERALD: case GRAVEL: case DIORITE: case DIRT: case GRANITE: case ANDESITE: case SILVERFISH:
                event.setResult(Event.Result.DEFAULT);
                break;
            default:
                event.setResult(Event.Result.DENY);
        }
    }
}
