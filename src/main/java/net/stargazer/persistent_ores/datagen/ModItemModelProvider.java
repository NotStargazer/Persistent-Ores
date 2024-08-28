package net.stargazer.persistent_ores.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.stargazer.persistent_ores.PersistentOres;
import net.stargazer.persistent_ores.item.PersistentOresItems;
import net.stargazer.persistent_ores.item.PersistentOreScannerModule;

import java.util.Objects;

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

        for (RegistryObject<Item> item : PersistentOresItems.MODULES.values())
        {
            registerModule((PersistentOreScannerModule) item.get());
        }

        basicItem(PersistentOresItems.EFFICIENCY_MODULE_1.get());
        basicItem(PersistentOresItems.EFFICIENCY_MODULE_2.get());
        basicItem(PersistentOresItems.RANK_MODULE_1.get());
        basicItem(PersistentOresItems.RANK_MODULE_2.get());
        basicItem(PersistentOresItems.PRODUCTIVITY_MODULE.get());
        basicItem(PersistentOresItems.EMPTY_MODULE.get());
    }

    private ItemModelBuilder begin(Item item)
    {
        return this.withExistingParent(
                Objects.requireNonNull(
                        ForgeRegistries.ITEMS.getKey(item))
                        .getPath(), new ResourceLocation("item/generated"));
    }

    private void registerModule(PersistentOreScannerModule item)
    {
        this.begin(item)
                .texture("layer0", new ResourceLocation("scannable", "items/blank_module"))
                .texture("layer1", new ResourceLocation("scannable", "items/module_slot"))
                .texture("layer2", new ResourceLocation("persistent_ores", "item/"
                        + item.getMaterial() + "_module"))
                .texture("layer3", new ResourceLocation("persistent_ores", "item/"
                        + item.getTier() + "_module"));
    }
}
