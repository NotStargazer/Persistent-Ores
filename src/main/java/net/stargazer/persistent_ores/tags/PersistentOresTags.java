package net.stargazer.persistent_ores.tags;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.stargazer.persistent_ores.PersistentOres;

import java.util.HashMap;
import java.util.Map;

public class PersistentOresTags
{
    public static final TagKey<Item> CLUSTER =
            ItemTags.create(new ResourceLocation(PersistentOres.MOD_ID, "clusters"));
    public static final TagKey<Item> SCAN_IMPURE =
            ItemTags.create(new ResourceLocation(PersistentOres.MOD_ID, "scanner/impure"));
    public static final TagKey<Item> SCAN_NORMAL =
            ItemTags.create(new ResourceLocation(PersistentOres.MOD_ID, "scanner/normal"));
    public static final TagKey<Item> SCAN_PURE =
            ItemTags.create(new ResourceLocation(PersistentOres.MOD_ID, "scanner/pure"));
    public static final TagKey<Block> PERSISTENT_ORE =
            ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation(PersistentOres.MOD_ID, "persistent_ore"));
    public static final TagKey<Block> IMPURE =
            ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation(PersistentOres.MOD_ID, "persistent_ore/impure"));
    public static final TagKey<Block> NORMAL =
            ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation(PersistentOres.MOD_ID, "persistent_ore/normal"));
    public static final TagKey<Block> PURE =
            ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation(PersistentOres.MOD_ID, "persistent_ore/pure"));

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
                    ItemTags.create(
                            new ResourceLocation(PersistentOres.MOD_ID, "clusters/"+entry)));
            SCAN_MATERIAL.put(entry,
                    ItemTags.create(
                            new ResourceLocation(PersistentOres.MOD_ID, "scanner/"+entry)));
            PERSISTENT_ORE_MATERIAL.put(entry,
                    ForgeRegistries.BLOCKS.tags().createTagKey(
                            new ResourceLocation(PersistentOres.MOD_ID, "persistent_ore/"+entry)));
        }
    }
}
