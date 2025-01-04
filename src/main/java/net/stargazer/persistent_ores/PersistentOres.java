package net.stargazer.persistent_ores;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import net.stargazer.persistent_ores.block.PersistentOresBlocks;
import net.stargazer.persistent_ores.block.entity.PersistentDrillBlockEntityRenderer;
import net.stargazer.persistent_ores.block.entity.PersistentOresBlockEntities;
import net.stargazer.persistent_ores.gui.PersistentDrillScreen;
import net.stargazer.persistent_ores.gui.PersistentOresMenuTypes;
import net.stargazer.persistent_ores.item.PersistentOresItems;
import net.stargazer.persistent_ores.networking.PersistentOresMessages;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;


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

        GeckoLib.initialize();

        PersistentOresItems.register(eventBus);
        PersistentOresBlocks.register(eventBus);
        PersistentOresBlockEntities.register(eventBus);
        PersistentOresMenuTypes.register(eventBus);
        TABS.register(eventBus);

        eventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        PersistentOresMessages.register();
    }

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = TABS.register
            ("persistent_ores_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(PersistentOresBlocks.PERSISTENT_DRILL.get().asItem()))
                    .title(Component.translatable("itemGroup.persistent_ores"))
                    .displayItems((itemDisplayParameters, output) -> {
                        PersistentOresItems.ITEM_REGISTER.getEntries().forEach(item -> output.accept(item.get()));
                        PersistentOresBlocks.BLOCK_REGISTER.getEntries().forEach(block -> output.accept(block.get()));
                    })
                    .build());

    @Mod.EventBusSubscriber(modid = PersistentOres.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class PersistentOresModEventHandler
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(PersistentOresMenuTypes.PERSISTENT_DRILL_MENU.get(), PersistentDrillScreen::new);
        }
    }
}