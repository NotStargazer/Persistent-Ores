package net.stargazer.persistent_ores;

import net.minecraft.core.Direction;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.stargazer.persistent_ores.block.PersistentOreBlock;
import net.stargazer.persistent_ores.block.PersistentOresBlocks;

@Mod.EventBusSubscriber(modid = PersistentOres.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PersistentOresEventHandler
{
    @SubscribeEvent
    public static void restrictDrillPlacement(PlayerInteractEvent.RightClickBlock event)
    {
        var item = event.getItemStack().getItem();
        var placedAgainst = event.getLevel().getBlockState(event.getPos());

        if (item == PersistentOresBlocks.PERSISTENT_DRILL.get().asItem())
        {
            if (!(placedAgainst.getBlock() instanceof PersistentOreBlock && event.getFace() == Direction.UP))
            {
                event.setCanceled(true);
            }
        }
    }
}