package net.staraiz.persistent_ores.gui;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.staraiz.persistent_ores.PersistentOres;

public class PersistentOresMenuTypes
{
    public static final DeferredRegister<MenuType<?>> MENU_TYPE_REGISTER =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, PersistentOres.MOD_ID);

    public static final RegistryObject<MenuType<ArdentDrillMenu>> ARDENT_DRILL_MENU =
            registerMenuType(ArdentDrillMenu::new, "ardent_drill_menu");


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name)
    {
        return MENU_TYPE_REGISTER.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus)
    {
        MENU_TYPE_REGISTER.register(eventBus);
    }
}
