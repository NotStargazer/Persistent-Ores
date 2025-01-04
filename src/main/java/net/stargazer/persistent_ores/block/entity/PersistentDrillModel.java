package net.stargazer.persistent_ores.block.entity;

import net.minecraft.resources.ResourceLocation;
import net.stargazer.persistent_ores.PersistentOres;
import software.bernie.geckolib.model.GeoModel;

public class PersistentDrillModel extends GeoModel<PersistentDrillBlockEntity>
{

    @Override
    public ResourceLocation getModelResource(PersistentDrillBlockEntity persistentDrillBlockEntityRenderer)
    {
        return new ResourceLocation(PersistentOres.MOD_ID, "geo/persistent_drill_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PersistentDrillBlockEntity persistentDrillBlockEntityRenderer)
    {
        return new ResourceLocation(PersistentOres.MOD_ID, "textures/block/persistent_drill.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PersistentDrillBlockEntity persistentDrillBlockEntity)
    {
        return null;
    }
}
