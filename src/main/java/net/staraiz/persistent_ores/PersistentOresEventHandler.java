package net.staraiz.persistent_ores;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.staraiz.persistent_ores.block.PersistentOreBlock;
import net.staraiz.persistent_ores.block.PersistentOresBlocks;
import net.staraiz.persistent_ores.gui.ArdentDrillScreen;
import net.staraiz.persistent_ores.gui.PersistentOresMenuTypes;

@Mod.EventBusSubscriber(modid = PersistentOres.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PersistentOresEventHandler
{
    @SubscribeEvent
    public static void restrictDrillPlacement(PlayerInteractEvent.RightClickBlock event)
    {
        var item = event.getItemStack().getItem();
        var placedAgainst = event.getLevel().getBlockState(event.getPos());

        if (item == PersistentOresBlocks.ARDENT_DRILL.get().asItem())
        {
            if (!(placedAgainst.getBlock() instanceof PersistentOreBlock && event.getFace() == Direction.UP))
            {
                event.setCanceled(true);
            }
        }
    }
}