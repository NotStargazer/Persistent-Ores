package net.stargazer.persistent_ores.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.stargazer.persistent_ores.PersistentOres;
import net.stargazer.persistent_ores.block.PersistentOresBlocks;

public class ModBlockStateProvider extends BlockStateProvider
{
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen.getPackOutput(), PersistentOres.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        var persistentDrillModelFile = new ModelFile.UncheckedModelFile(modLoc("block/persistent_drill_block"));
        simpleBlock(PersistentOresBlocks.PERSISTENT_DRILL.get(), persistentDrillModelFile);
        simpleBlockItem(PersistentOresBlocks.PERSISTENT_DRILL.get(), persistentDrillModelFile);

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
