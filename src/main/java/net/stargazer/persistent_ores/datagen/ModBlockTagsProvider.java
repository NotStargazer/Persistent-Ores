package net.stargazer.persistent_ores.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.stargazer.persistent_ores.PersistentOres;
import net.stargazer.persistent_ores.block.PersistentOresBlocks;
import net.stargazer.persistent_ores.item.PersistentOresItems;
import net.stargazer.persistent_ores.tags.PersistentOresTags;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider
{
    public ModBlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(generator, PersistentOres.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {
        for (var entry : PersistentOres.PERSISTENT_ORES_ENTRIES)
        {
            var name = "persistent_" + entry;
            var nameDense = "dense_persistent_" + entry;
            var nameVeryDense = "very_dense_persistent_" + entry;

            tag(PersistentOresTags.PERSISTENT_ORE_MATERIAL.get(entry))
                    .add(PersistentOresBlocks.PERSISTENT_ORES.get(name).get(),
                            PersistentOresBlocks.PERSISTENT_ORES.get(nameDense).get(),
                            PersistentOresBlocks.PERSISTENT_ORES.get(nameVeryDense).get());
            tag(PersistentOresTags.PERSISTENT_ORE)
                    .add(PersistentOresBlocks.PERSISTENT_ORES.get(name).get(),
                            PersistentOresBlocks.PERSISTENT_ORES.get(nameDense).get(),
                            PersistentOresBlocks.PERSISTENT_ORES.get(nameVeryDense).get());
            tag(PersistentOresTags.IMPURE)
                    .add(PersistentOresBlocks.PERSISTENT_ORES.get(name).get());
            tag(PersistentOresTags.NORMAL)
                    .add(PersistentOresBlocks.PERSISTENT_ORES.get(nameDense).get());
            tag(PersistentOresTags.PURE)
                    .add(PersistentOresBlocks.PERSISTENT_ORES.get(nameVeryDense).get());
        }
    }
}
