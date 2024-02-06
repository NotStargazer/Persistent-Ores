package net.stargazer.persistent_ores.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

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

    public String getYield()
    {
        return ItemYield;
    }
}
