package net.stargazer.persistent_ores.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.stargazer.persistent_ores.PersistentOres;
import net.stargazer.persistent_ores.block.PersistentOresBlocks;
import net.stargazer.persistent_ores.tags.PersistentOresTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider
{

    public ModBlockTagsProvider(DataGenerator output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output.getPackOutput(), lookupProvider, PersistentOres.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        for (var entry : PersistentOres.PERSISTENT_ORES_ENTRIES)
        {
            var name = "persistent_" + entry;
            var nameDense = "dense_persistent_" + entry;
            var nameVeryDense = "very_dense_persistent_" + entry;

            tag(PersistentOresTags.PERSISTENT_ORE_MATERIAL.get(entry))
                    .add(PersistentOresBlocks.PERSISTENT_ORES.get(name).getKey(),
                            PersistentOresBlocks.PERSISTENT_ORES.get(nameDense).getKey(),
                            PersistentOresBlocks.PERSISTENT_ORES.get(nameVeryDense).getKey());
            tag(PersistentOresTags.PERSISTENT_ORE)
                    .add(PersistentOresBlocks.PERSISTENT_ORES.get(name).getKey(),
                            PersistentOresBlocks.PERSISTENT_ORES.get(nameDense).getKey(),
                            PersistentOresBlocks.PERSISTENT_ORES.get(nameVeryDense).getKey());
            tag(PersistentOresTags.IMPURE)
                    .add(PersistentOresBlocks.PERSISTENT_ORES.get(name).getKey());
            tag(PersistentOresTags.NORMAL)
                    .add(PersistentOresBlocks.PERSISTENT_ORES.get(nameDense).getKey());
            tag(PersistentOresTags.PURE)
                    .add(PersistentOresBlocks.PERSISTENT_ORES.get(nameVeryDense).getKey());
        }
    }
}
