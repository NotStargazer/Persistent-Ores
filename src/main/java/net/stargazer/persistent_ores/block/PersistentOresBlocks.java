package net.stargazer.persistent_ores.block;

import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.stargazer.persistent_ores.PersistentOres;
import net.stargazer.persistent_ores.item.PersistentOresItems;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PersistentOresBlocks
{
    public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, PersistentOres.MOD_ID);

    public static final Map<String, RegistryObject<Block>> PERSISTENT_ORES;
    public static final RegistryObject<Block> ARDENT_DRILL =
            RegisterBlock("persistent_drill_block",
                    () -> new PersistentDrillBlock(BlockBehaviour.Properties.of(Material.PISTON).noOcclusion()),
                    PersistentOres.CREATIVE_TAB);

    static
    {
        PERSISTENT_ORES = new HashMap<>(PersistentOres.PERSISTENT_ORES_ENTRIES.length * 3);
        for (String entry : PersistentOres.PERSISTENT_ORES_ENTRIES)
        {
            var name = "persistent_" + entry;
            var nameDense = "dense_persistent_" + entry;
            var nameVeryDense = "very_dense_persistent_" + entry;
            PERSISTENT_ORES.put(name,
                    RegisterBlock(name + "_block",
                            () -> new PersistentOreBlock(entry, 1, BlockBehaviour.Properties.copy(Blocks.BEDROCK).noLootTable()),
                            PersistentOres.CREATIVE_TAB));
            PERSISTENT_ORES.put(nameDense,
                    RegisterBlock(nameDense + "_block",
                            () -> new PersistentOreBlock(entry, 2, BlockBehaviour.Properties.copy(Blocks.BEDROCK).noLootTable()),
                            PersistentOres.CREATIVE_TAB));
            PERSISTENT_ORES.put(nameVeryDense,
                    RegisterBlock(nameVeryDense + "_block",
                            () -> new PersistentOreBlock(entry, 3, BlockBehaviour.Properties.copy(Blocks.BEDROCK).noLootTable()),
                            PersistentOres.CREATIVE_TAB));
        }
    }

    private static <T extends Block> RegistryObject<T> RegisterBlock(String name, Supplier<T> block, CreativeModeTab tab)
    {
        RegistryObject<T> registeredBlock = BLOCK_REGISTER.register(name, block);
        RegisterBlockItem(name, registeredBlock, tab);
        return registeredBlock;
    }

    private static <T extends Block> RegistryObject<Item> RegisterBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab)
    {
        return PersistentOresItems.ITEM_REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus)
    {
        BLOCK_REGISTER.register(eventBus);
    }
}
