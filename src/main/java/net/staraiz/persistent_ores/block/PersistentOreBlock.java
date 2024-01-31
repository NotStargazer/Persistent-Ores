package net.staraiz.persistent_ores.block;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.staraiz.persistent_ores.PersistentOresItems;

public class PersistentOreBlock extends Block
{
    private final String ItemYield;
    public final int DensityLevel;

    public PersistentOreBlock(String yield, int density, Properties properties)
    {

        super(properties);

        ItemYield = yield;
        DensityLevel = density;
    }

    public ItemStack GetYield()
    {
        return PersistentOresItems.CLUSTERS.get(ItemYield).get().getDefaultInstance();
    }
}
