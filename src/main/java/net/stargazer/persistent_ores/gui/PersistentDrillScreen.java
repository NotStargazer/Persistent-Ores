package net.stargazer.persistent_ores.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.stargazer.persistent_ores.PersistentOres;
import net.stargazer.persistent_ores.gui.render.EnergyInfoArea;
import net.stargazer.persistent_ores.util.MouseUtil;

import java.util.List;
import java.util.Optional;

public class PersistentDrillScreen extends AbstractContainerScreen<PersistentDrillMenu>
{
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(PersistentOres.MOD_ID, "textures/gui/persistent_drill_gui.png");

    private EnergyInfoArea energyInfoArea;

    public PersistentDrillScreen(PersistentDrillMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
        inventoryLabelY = 100000;
    }

    @Override
    protected void init()
    {
        super.init();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        energyInfoArea = new EnergyInfoArea(x + 148, y + 8, menu.blockEntity.getEnergyStorage());
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY)
    {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 148, 8, 16, 69))
        {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 127, 60, 18, 18))
        {
            var problems = menu.blockEntity.getProblems();
            renderTooltip(pPoseStack, problems.isEmpty() ? List.of(Component.translatable("gui.persistent_drill.none").withStyle(ChatFormatting.GREEN)) : problems,
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height)
    {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
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
        energyInfoArea.draw(stack);

        blit(stack, x + 127, y + 60, 176, menu.isProcessing() ? 44 : 26, 18, 18);
    }

    private void renderProgress(PoseStack stack, int x, int y)
    {
        blit(stack, x + 79, y + 47, 176, 0, 18, menu.getScaledProgress());
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta)
    {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        renderTooltip(stack, mouseX, mouseY);
    }
}
