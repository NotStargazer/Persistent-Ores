package net.staraiz.persistent_ores.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.staraiz.persistent_ores.PersistentOres;

import java.util.HashMap;
import java.util.Map;

public class PersistentOresItems
{
    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, PersistentOres.MOD_ID);

    public static final RegistryObject<Item> EMPTY_MODULE = RegisterItem("empty_module");
    public static final RegistryObject<Item> RANK_MODULE_1 =
            RegisterModuleItem("rank_module_1", 4, 1, 0);
    public static final RegistryObject<Item> RANK_MODULE_2 =
            RegisterModuleItem("rank_module_2", 8, 2, 0);
    public static final RegistryObject<Item> EFFICIENCY_MODULE_1 =
            RegisterModuleItem("energy_module_1", 0.9f, 0, 0);
    public static final RegistryObject<Item> EFFICIENCY_MODULE_2 =
            RegisterModuleItem("energy_module_2", 0.75f, 0, 0);
    public static final RegistryObject<Item> PRODUCTIVITY_MODULE =
            RegisterModuleItem("productivity_module", 8, 0, 1);

    public static final Map<String, RegistryObject<Item>> CLUSTERS;
    static
    {
        CLUSTERS = new HashMap<>(PersistentOres.PERSISTENT_ORES_ENTRIES.length);
        for (String entry : PersistentOres.PERSISTENT_ORES_ENTRIES)
        {
            CLUSTERS.put(entry, RegisterItem(entry+"_cluster"));
        }
    }

    private static RegistryObject<Item> RegisterItem(String name)
    {
        return ITEM_REGISTER.register(name,
                () -> new Item(new Item.Properties().tab(PersistentOres.CREATIVE_TAB)));
    }
    private static RegistryObject<Item> RegisterModuleItem(String name, float multiplier, int rank, int productivity)
    {
        return ITEM_REGISTER.register(name,
                () -> new ModuleItem(multiplier, rank, productivity,
                        new Item.Properties().tab(PersistentOres.CREATIVE_TAB).stacksTo(1)));
    }

    public static void register(IEventBus eventBus)
    {
        ITEM_REGISTER.register(eventBus);
    }
}
