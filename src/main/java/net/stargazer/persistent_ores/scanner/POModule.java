package net.stargazer.persistent_ores.scanner;

import li.cil.scannable.api.API;
import li.cil.scannable.api.scanning.BlockScannerModule;
import li.cil.scannable.api.scanning.ScanResultProvider;
import li.cil.scannable.client.scanning.filter.BlockCacheScanFilter;
import li.cil.scannable.client.scanning.filter.BlockScanFilter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryManager;
import net.stargazer.persistent_ores.block.PersistentOresBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class POModule implements BlockScannerModule
{
    private final int cost;

    private final Predicate<BlockState> filter;

    public POModule(String entry, int tier)
    {
        this.cost = switch (tier)
        {
            default -> 250;
            case 1 -> 500;
            case 2 -> 1000;
        };

        List<Predicate<BlockState>> filters = new ArrayList<>();

        if (tier >= 2)
        {
            filters.add(new BlockScanFilter(
                    PersistentOresBlocks.PERSISTENT_ORES.get("very_dense_persistent_" + entry).get()));
        }
        if (tier >= 1)
        {
            filters.add(new BlockScanFilter(
                    PersistentOresBlocks.PERSISTENT_ORES.get("dense_persistent_" + entry).get()));
        }
        if (tier >= 0)
        {
            filters.add(new BlockScanFilter(
                    PersistentOresBlocks.PERSISTENT_ORES.get("persistent_" + entry).get()));
        }

        filter = new BlockCacheScanFilter(filters);
    }

    @Override
    public Predicate<BlockState> getFilter(ItemStack itemStack)
    {
        return filter;
    }

    @Override
    public int getEnergyCost(ItemStack itemStack)
    {
        return cost;
    }

    @Nullable
    @Override
    public ScanResultProvider getResultProvider()
    {
        return RegistryManager.ACTIVE.getRegistry(ScanResultProvider.REGISTRY).getValue(API.SCAN_RESULT_PROVIDER_BLOCKS);
    }
}
