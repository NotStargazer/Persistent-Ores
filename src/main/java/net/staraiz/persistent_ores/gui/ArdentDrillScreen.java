package net.staraiz.persistent_ores.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.staraiz.persistent_ores.PersistentOres;

public class ArdentDrillScreen extends AbstractContainerScreen<ArdentDrillMenu>
{
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(PersistentOres.MOD_ID, "textures/gui/ardent_drill_gui.png");

    public ArdentDrillScreen(ArdentDrillMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
        inventoryLabelY = 100000;
    }

    @Override
    protected void init()
    {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTick, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        blit(stack, x, y, 0, 0, imageWidth, imageHeight);
        renderProgress(stack, x, y);
    }

    private void renderProgress(PoseStack stack, int x, int y)
    {
        blit(stack, x + 79, y + 60, 176, 0, 18, menu.getScaledProgress());
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta)
    {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        renderTooltip(stack, mouseX, mouseY);
    }
}
