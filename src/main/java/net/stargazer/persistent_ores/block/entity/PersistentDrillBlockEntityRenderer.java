package net.stargazer.persistent_ores.block.entity;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.stargazer.persistent_ores.util.Easing;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class PersistentDrillBlockEntityRenderer extends GeoBlockRenderer<PersistentDrillBlockEntity>
{
    private final float yLow = 0;
    private final float yHigh = -8;

    public PersistentDrillBlockEntityRenderer(BlockEntityRendererProvider.Context rendererProvider)
    {
        super(rendererProvider, new PersistentDrillModel());
    }

    @Override
    public void render(GeoModel model, PersistentDrillBlockEntity animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        var drill = model.getBone("drill").get();

        var progress = animatable.getSpinProgress();
        var spin = animatable.getSpinRotation();

        drill.setRotationY(spin);
        drill.setPositionY(Easing.EXPO_IN_OUT.ease(progress, yLow, yHigh, 1));


        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
