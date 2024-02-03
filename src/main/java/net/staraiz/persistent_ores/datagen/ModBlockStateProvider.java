package net.staraiz.persistent_ores.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.staraiz.persistent_ores.PersistentOres;
import net.staraiz.persistent_ores.block.PersistentOresBlocks;

import java.util.Collection;

public class ModBlockStateProvider extends BlockStateProvider
{
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen, PersistentOres.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        var ardentDrillModelFile = new ModelFile.UncheckedModelFile(modLoc("block/ardent_drill_block"));
        simpleBlock(PersistentOresBlocks.ARDENT_DRILL.get(), ardentDrillModelFile);
        simpleBlockItem(PersistentOresBlocks.ARDENT_DRILL.get(), ardentDrillModelFile);

        for (var entry : PersistentOres.PERSISTENT_ORES_ENTRIES)
        {
            oreCluster("", entry);
            oreCluster("dense_", entry);
            oreCluster("very_dense_", entry);
        }
    }

    private void oreCluster(String prefix, String blockName)
    {
        var provider = models();
        var itemProvider = itemModels();
        var poName = "persistent_" + blockName;

        var parent = provider.withExistingParent(prefix+poName+"_block", modLoc("block/"+prefix+"persistent_block"));
        parent.texture("cluster", modLoc("block/"+poName+"_cluster"));
        parent.texture("particle", modLoc("block/"+poName+"_block"));
        var itemParent = itemProvider.withExistingParent(prefix+poName+"_block", modLoc("block/"+prefix+"persistent_block"));
        itemParent.texture("cluster", modLoc("block/"+poName+"_cluster"));
        itemParent.texture("particle", modLoc("block/"+poName+"_block"));

        getVariantBuilder(PersistentOresBlocks.PERSISTENT_ORES.get(prefix+poName).get())
                .partialState().setModels(ConfiguredModel.builder().modelFile(parent).build());

    }
}
