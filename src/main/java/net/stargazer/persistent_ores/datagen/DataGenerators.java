package net.stargazer.persistent_ores.datagen;

import net.minecraft.data.DataGenerator;
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
        var lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(true, new ModItemModelProvider(generator, existingFileHelper));
        generator.addProvider(true, new ModBlockStateProvider(generator, existingFileHelper));
        var blockProvider = new ModBlockTagsProvider(generator, lookupProvider, existingFileHelper);
        generator.addProvider(true, blockProvider);
        generator.addProvider(true, new ModItemTagsProvider(generator, lookupProvider, blockProvider.contentsGetter()));
    }
}
