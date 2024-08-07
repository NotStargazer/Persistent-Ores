package net.stargazer.persistent_ores.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.stargazer.persistent_ores.PersistentOres;

@Mod.EventBusSubscriber(modid = PersistentOres.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
    @SubscribeEvent
    public static void GatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(true, new ModItemModelProvider(generator, existingFileHelper));
        generator.addProvider(true, new ModBlockStateProvider(generator, existingFileHelper));
        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(generator, existingFileHelper);
        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new ModItemTagsProvider(generator, blockTagsProvider, existingFileHelper));
    }
}
