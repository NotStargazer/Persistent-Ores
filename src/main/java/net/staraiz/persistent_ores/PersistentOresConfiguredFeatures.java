package net.staraiz.persistent_ores;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.staraiz.persistent_ores.PersistentOres;

public class PersistentOresConfiguredFeatures
{
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, PersistentOres.MOD_ID);



    public static void Register(IEventBus eventBus)
    {
        CONFIGURED_FEATURES.register(eventBus);
    }
}
