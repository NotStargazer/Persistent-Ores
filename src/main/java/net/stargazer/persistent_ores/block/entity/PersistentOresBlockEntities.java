package net.stargazer.persistent_ores.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.stargazer.persistent_ores.PersistentOres;
import net.stargazer.persistent_ores.block.PersistentOresBlocks;

public class PersistentOresBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PersistentOres.MOD_ID);

    public static final RegistryObject<BlockEntityType<PersistentDrillBlockEntity>> ARDENT_DRILL =
            BLOCK_ENTITY_REGISTER.register("ardent_drill_be", () ->
                    BlockEntityType.Builder.of(PersistentDrillBlockEntity::new,
                            PersistentOresBlocks.ARDENT_DRILL.get()).build(null));

    public static void register(IEventBus eventBus)
    {
        BLOCK_ENTITY_REGISTER.register(eventBus);
    }
}
