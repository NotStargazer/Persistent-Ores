package net.stargazer.persistent_ores.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModuleItem extends Item
{
    public final float powerMultiplier;
    public final int rank;
    public final float productivity;

    public ModuleItem(float powerMultiplier, int rank, float productivity, Properties properties)
    {
        super(properties);
        this.powerMultiplier = powerMultiplier;
        this.rank = rank;
        this.productivity = productivity;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag)
    {
        if (Screen.hasShiftDown())
        {
            var multiplierUp = Component.translatable("item.description.module_multiplier_up");
            var multiplierDown = Component.translatable("item.description.module_multiplier_down");
            var rank = Component.translatable("item.description.module_rank");
            var rankEnd = Component.translatable("item.description.module_rank_end");
            var productivity = Component.translatable("item.description.module_productivity");
            if (this.productivity > 0)
            {
                components.add(productivity.withStyle(ChatFormatting.WHITE).append(
                        Component.literal(" " + (int)(this.productivity * 100) + "%").withStyle(ChatFormatting.BLUE)));
            }
            if (this.rank > 0)
            {
                components.add(rank.append(
                        Component.literal(" " + this.rank + " ").withStyle(ChatFormatting.BLUE).append(
                                rankEnd.withStyle(ChatFormatting.WHITE))));
            }
            components.add(this.powerMultiplier < 1
                    ? multiplierDown.withStyle(ChatFormatting.WHITE).append(Component.literal(String.format(" %.2fx", powerMultiplier)).withStyle(ChatFormatting.GREEN))
                    : multiplierUp.withStyle(ChatFormatting.WHITE).append(Component.literal(" " + (int)(powerMultiplier * 100) + "%").withStyle(ChatFormatting.RED)));
        }
        else
        {
            components.add(Component.translatable("item.description.instruction_shift").withStyle(ChatFormatting.YELLOW));
        }

        super.appendHoverText(stack, level, components, flag);
    }
}
