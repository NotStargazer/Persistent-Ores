package net.stargazer.persistent_ores.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.LiquidBlock;
import net.stargazer.persistent_ores.PersistentOres;
import net.stargazer.persistent_ores.gui.render.EnergyInfoArea;
import net.stargazer.persistent_ores.util.MouseUtil;

import java.awt.*;
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
    protected void renderLabels(GuiGraphics guiGraphics, int pMouseX, int pMouseY)
    {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 148, 8, 16, 69))
        {
            var tooltip = List.of(energyInfoArea.getTooltips().get(0), Component.literal(String.format("%.2f FE/t", menu.blockEntity.getEnergyCost())));

            guiGraphics.renderTooltip(font, tooltip, Optional.empty(), pMouseX - x, pMouseY - y);
        }
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 127, 60, 18, 18))
        {
            var problems = menu.blockEntity.getProblems();
            var tooltip = problems.isEmpty()
                    ? Component.translatable("gui.persistent_drill.none").withStyle(ChatFormatting.GREEN).toFlatList() : problems;
            guiGraphics.renderTooltip(font, tooltip, Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height)
    {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderProgress(guiGraphics, x, y);
        energyInfoArea.draw(guiGraphics);

        guiGraphics.blit(TEXTURE, x + 127, y + 60, 176, menu.isProcessing() ? 44 : 26, 18, 18);
    }

    private void renderProgress(GuiGraphics guiGraphics, int x, int y)
    {
        guiGraphics.blit(TEXTURE, x + 79, y + 47, 176, 0, 18, menu.getScaledProgress());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta)
    {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
