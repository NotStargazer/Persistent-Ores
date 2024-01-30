package net.staraiz.persistent_ores.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.staraiz.persistent_ores.PersistentOres;
import net.staraiz.persistent_ores.PersistentOresItems;

public class ModItemModelProvider extends ItemModelProvider
{
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, PersistentOres.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        for (RegistryObject<Item> item : PersistentOresItems.CLUSTERS.values())
        {
            basicItem(item.get());
        }
    }
}
