package net.stargazer.persistent_ores.item;

import li.cil.scannable.common.config.Strings;
import li.cil.scannable.common.forge.capabilities.ScannerModuleWrapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.stargazer.persistent_ores.scanner.POModule;

import javax.annotation.Nullable;
import java.util.List;

public class PersistentOreScannerModule extends Item
{
    private final String material;
    private final String tier;
    private final int cost;
    private final ICapabilityProvider capability;

    public PersistentOreScannerModule(String entry, int tier, Properties pProperties)
    {
        super(pProperties);

        this.material = entry;
        this.tier = switch (tier)
        {
            default -> "impure";
            case 1 -> "normal";
            case 2 -> "pure";
        };

        var poMod = new POModule(entry, tier);
        this.cost = poMod.getEnergyCost(null);
        this.capability = new ScannerModuleWrapper(poMod);
    }

    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag tag) {
        return this.capability;
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        int cost = this.cost;
        if (cost > 0) {
            tooltip.add(Strings.energyUsage(cost));
        }
    }

    public String getMaterial()
    {
        return material;
    }

    public String getTier()
    {
        return tier;
    }
}
