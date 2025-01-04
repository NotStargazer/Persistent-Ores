package net.stargazer.persistent_ores.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.stargazer.persistent_ores.PersistentOres;

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
            RegisterModuleItem("productivity_module", 8, 0, 0.5f);

    public static final Map<String, RegistryObject<Item>> CLUSTERS;
    public static final Map<String, RegistryObject<Item>> MODULES;
    static
    {
        CLUSTERS = new HashMap<>(PersistentOres.PERSISTENT_ORES_ENTRIES.length);
        MODULES = new HashMap<>(PersistentOres.PERSISTENT_ORES_ENTRIES.length * 3);
        for (String entry : PersistentOres.PERSISTENT_ORES_ENTRIES)
        {
            CLUSTERS.put(entry, RegisterItem(entry+"_cluster"));

            MODULES.put(entry+"_impure", ITEM_REGISTER.register(entry+"_module_impure",
                    () -> new PersistentOreScannerModule(entry, 0,
                            new Item.Properties().stacksTo(1))));
            MODULES.put(entry+"_normal", ITEM_REGISTER.register(entry+"_module_normal",
                    () -> new PersistentOreScannerModule(entry, 1,
                            new Item.Properties().stacksTo(1))));
            MODULES.put(entry+"_pure", ITEM_REGISTER.register(entry+"_module_pure",
                    () -> new PersistentOreScannerModule(entry, 2,
                            new Item.Properties().stacksTo(1))));
        }
    }

    private static RegistryObject<Item> RegisterItem(String name)
    {
        return ITEM_REGISTER.register(name,
                () -> new Item(new Item.Properties()));
    }
    private static RegistryObject<Item> RegisterModuleItem(String name, float multiplier, int rank, float productivity)
    {
        return ITEM_REGISTER.register(name,
                () -> new ModuleItem(multiplier, rank, productivity,
                        new Item.Properties().stacksTo(1)));
    }

    public static void register(IEventBus eventBus)
    {
        ITEM_REGISTER.register(eventBus);
    }
}
