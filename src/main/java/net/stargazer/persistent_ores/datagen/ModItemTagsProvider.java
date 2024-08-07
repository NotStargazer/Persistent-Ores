package net.stargazer.persistent_ores.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.stargazer.persistent_ores.PersistentOres;
import net.stargazer.persistent_ores.item.PersistentOresItems;
import net.stargazer.persistent_ores.tags.PersistentOresTags;
import org.jetbrains.annotations.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider
{

    public ModItemTagsProvider(DataGenerator generator, BlockTagsProvider tagsProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(generator, tagsProvider, PersistentOres.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {
        for (var entry : PersistentOres.PERSISTENT_ORES_ENTRIES)
        {
            tag(PersistentOresTags.CLUSTER_MATERIAL.get(entry)).add(PersistentOresItems.CLUSTERS.get(entry).get());
            tag(PersistentOresTags.CLUSTER).add(PersistentOresItems.CLUSTERS.get(entry).get());
        }
    }
}
