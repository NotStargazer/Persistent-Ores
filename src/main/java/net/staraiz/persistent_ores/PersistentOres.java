package net.staraiz.persistent_ores;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.staraiz.persistent_ores.block.PersistentOresBlocks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;


@Mod(PersistentOres.MOD_ID)
public class PersistentOres
{
    public static final String MOD_ID = "persistent_ores";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String[] PERSISTENT_ORES_ENTRIES =
            {
//                    new PersistentOreEntry("copper", Items.RAW_COPPER),
//                    new PersistentOreEntry("tin", Items.RAW_COPPER),
//                    new PersistentOreEntry("iron", Items.RAW_IRON),
//                    new PersistentOreEntry("gold", Items.RAW_GOLD),
//                    new PersistentOreEntry("zinc", Items.RAW_GOLD),
//                    new PersistentOreEntry("silver", Items.RAW_GOLD),
//                    new PersistentOreEntry("platinum", Items.RAW_GOLD),
//                    new PersistentOreEntry("aluminum", Items.RAW_GOLD),
//                    new PersistentOreEntry("nickle", Items.RAW_GOLD),
//                    new PersistentOreEntry("osmium", Items.RAW_GOLD),
//                    new PersistentOreEntry("uranium", Items.RAW_GOLD),
//                    new PersistentOreEntry("titanium", Items.RAW_GOLD),
//                    new PersistentOreEntry("tungsten", Items.RAW_GOLD),
//                    new PersistentOreEntry("luminite", Items.RAW_GOLD),
//                    new PersistentOreEntry("solarite", Items.RAW_GOLD)
            };

    public PersistentOres()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PersistentOresItems.Register(eventBus);
        PersistentOresBlocks.Register(eventBus);
        PersistentOresConfiguredFeatures.Register(eventBus);

        eventBus.addListener(this::commonSetup);

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

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}