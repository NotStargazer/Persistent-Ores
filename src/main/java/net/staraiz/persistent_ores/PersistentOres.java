package net.staraiz.persistent_ores;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.staraiz.persistent_ores.block.PersistentOresBlocks;
import net.staraiz.persistent_ores.block.entity.PersistentOresBlockEntities;
import net.staraiz.persistent_ores.gui.ArdentDrillScreen;
import net.staraiz.persistent_ores.gui.PersistentOresMenuTypes;
import net.staraiz.persistent_ores.item.PersistentOresItems;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;


@Mod(PersistentOres.MOD_ID)
public class PersistentOres
{
    public static final String MOD_ID = "persistent_ores";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String[] PERSISTENT_ORES_ENTRIES =
            {
                    "copper",
                    "tin",
                    "iron",
                    "gold",
                    "zinc",
                    "silver",
                    "lead",
                    "platinum",
                    "aluminum",
                    "nickel",
                    "osmium",
                    "uranium",
                    "titanium",
                    "tungsten",
                    "luminite",
                    "solarite"
            };

    public PersistentOres()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PersistentOresItems.register(eventBus);
        PersistentOresBlocks.register(eventBus);
        PersistentOresConfiguredFeatures.register(eventBus);
        PersistentOresBlockEntities.register(eventBus);
        PersistentOresMenuTypes.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(MOD_ID)
    {
        @Override
        public @NotNull ItemStack makeIcon()
        {
            return Items.DIAMOND_PICKAXE.getDefaultInstance();
        }
    };

    @Mod.EventBusSubscriber(modid = PersistentOres.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class PersistentOresModEventHandler
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(PersistentOresMenuTypes.ARDENT_DRILL_MENU.get(), ArdentDrillScreen::new);
        }
    }
}