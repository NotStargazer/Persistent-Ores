package net.stargazer.persistent_ores.block.entity;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stargazer.persistent_ores.util.Easing;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PersistentDrillBlockEntityRenderer extends GeoBlockRenderer<PersistentDrillBlockEntity>
{
    private final float yLow = 0;
    private final float yHigh = -8;

    public PersistentDrillBlockEntityRenderer(EntityRendererProvider.Context rendererProvider)
    {
        super(new PersistentDrillModel());
    }

    @Override
    public void actuallyRender(PoseStack poseStack, PersistentDrillBlockEntity animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        var drill = model.getBone("drill").get();

        var progress = animatable.getSpinProgress();
        var spin = animatable.getSpinRotation();

        drill.setRotY(spin);
        drill.setPosY(Easing.EXPO_IN_OUT.ease(progress, yLow, yHigh, 1));

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
