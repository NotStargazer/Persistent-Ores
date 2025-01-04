package net.stargazer.persistent_ores.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.stargazer.persistent_ores.PersistentOres;
import net.stargazer.persistent_ores.item.PersistentOresItems;
import net.stargazer.persistent_ores.tags.PersistentOresTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider
{
    public ModItemTagsProvider(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags)
    {
        super(generator.getPackOutput(), pLookupProvider, pBlockTags);
    }
//    public ModItemTagsProvider(DataGenerator generator, BlockTagsProvider tagsProvider, @Nullable ExistingFileHelper existingFileHelper)
//    {
//        super(generator.getPackOutput(), tagsProvider, PersistentOres.MOD_ID, existingFileHelper);
//    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        for (var entry : PersistentOres.PERSISTENT_ORES_ENTRIES)
        {
            tag(PersistentOresTags.CLUSTER_MATERIAL.get(entry)).add(PersistentOresItems.CLUSTERS.get(entry).get());
            tag(PersistentOresTags.CLUSTER).add(PersistentOresItems.CLUSTERS.get(entry).get());

            var scanner = entry + "_impure";
            var scannerDense = entry + "_normal";
            var scannerVeryDense = entry + "_pure";

            tag(PersistentOresTags.SCAN_MATERIAL.get(entry))
                    .add(PersistentOresItems.MODULES.get(scanner).get(),
                            PersistentOresItems.MODULES.get(scannerDense).get(),
                            PersistentOresItems.MODULES.get(scannerVeryDense).get());
            tag(PersistentOresTags.SCAN_IMPURE)
                    .add(PersistentOresItems.MODULES.get(scanner).get());
            tag(PersistentOresTags.SCAN_NORMAL)
                    .add(PersistentOresItems.MODULES.get(scannerDense).get());
            tag(PersistentOresTags.SCAN_PURE)
                    .add(PersistentOresItems.MODULES.get(scannerVeryDense).get());
        }
    }
}
