package net.staraiz.persistent_ores.block;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class PersistentOreBlock extends Block
{
    private final ItemStack ItemYield;

    public PersistentOreBlock(Item yield, int density, Properties properties)
    {

        super(properties);

        ItemYield = yield.getDefaultInstance();
        ItemYield.setCount(density);
    }

    public void GetYield(Slot slot)
    {
        slot.safeInsert(ItemYield);
    }


}
