package net.staraiz.persistent_ores;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PersistentOresItems
{
    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, PersistentOres.MOD_ID);

    public static final Map<String, RegistryObject<Item>> CLUSTERS;
    static
    {
        CLUSTERS = new HashMap<>(PersistentOres.PERSISTENT_ORES_ENTRIES.length);
        for (String entry : PersistentOres.PERSISTENT_ORES_ENTRIES)
        {
            CLUSTERS.put(entry, RegisterItem(entry+"_cluster", () -> new Item(new Item.Properties().tab(PersistentOres.CREATIVE_TAB))));
        }
    }

    private static <T extends Item> RegistryObject<T> RegisterItem(String name, Supplier<T> item)
    {
        return ITEM_REGISTER.register(name, item);
    }

    public static void Register(IEventBus eventBus)
    {
        ITEM_REGISTER.register(eventBus);
    }
}
