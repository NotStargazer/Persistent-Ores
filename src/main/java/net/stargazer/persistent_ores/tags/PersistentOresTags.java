package net.stargazer.persistent_ores.tags;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.stargazer.persistent_ores.PersistentOres;

import java.util.HashMap;
import java.util.Map;

public class PersistentOresTags
{
    public static final TagKey<Item> CLUSTER =
            TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(PersistentOres.MOD_ID, "clusters"));
    public static final TagKey<Item> SCAN_IMPURE =
            TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(PersistentOres.MOD_ID, "scanner/impure"));
    public static final TagKey<Item> SCAN_NORMAL =
            TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(PersistentOres.MOD_ID, "scanner/normal"));
    public static final TagKey<Item> SCAN_PURE =
            TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(PersistentOres.MOD_ID, "scanner/pure"));
    public static final TagKey<Block> PERSISTENT_ORE =
            TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(PersistentOres.MOD_ID, "persistent_ore"));
    public static final TagKey<Block> IMPURE =
            TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(PersistentOres.MOD_ID, "persistent_ore/impure"));
    public static final TagKey<Block> NORMAL =
            TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(PersistentOres.MOD_ID, "persistent_ore/normal"));
    public static final TagKey<Block> PURE =
            TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(PersistentOres.MOD_ID, "persistent_ore/pure"));

    public static final Map<String, TagKey<Item>> CLUSTER_MATERIAL;
    public static final Map<String, TagKey<Item>> SCAN_MATERIAL;
    public static final Map<String, TagKey<Block>> PERSISTENT_ORE_MATERIAL;

    static
    {
        CLUSTER_MATERIAL = new HashMap<>(PersistentOres.PERSISTENT_ORES_ENTRIES.length);
        SCAN_MATERIAL = new HashMap<>(PersistentOres.PERSISTENT_ORES_ENTRIES.length);
        PERSISTENT_ORE_MATERIAL = new HashMap<>(PersistentOres.PERSISTENT_ORES_ENTRIES.length);

        for (String entry : PersistentOres.PERSISTENT_ORES_ENTRIES)
        {
            CLUSTER_MATERIAL.put(entry,
                    TagKey.create(Registry.ITEM_REGISTRY,
                            new ResourceLocation(PersistentOres.MOD_ID, "clusters/"+entry)));
            SCAN_MATERIAL.put(entry,
                    TagKey.create(Registry.ITEM_REGISTRY,
                            new ResourceLocation(PersistentOres.MOD_ID, "scanner/"+entry)));
            PERSISTENT_ORE_MATERIAL.put(entry,
                    TagKey.create(Registry.BLOCK_REGISTRY,
                            new ResourceLocation(PersistentOres.MOD_ID, "persistent_ore/"+entry)));
        }
    }
}
