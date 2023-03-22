package net.techtastic.tat.screen;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.techtastic.tat.ToilAndTrouble;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.menu.MenuRegistry.ExtendedMenuTypeFactory;

public class TATMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ToilAndTrouble.MOD_ID, Registry.MENU_REGISTRY);

    public static final RegistrySupplier<MenuType<CastIronOvenMenu>> CAST_IRON_OVEN_MENU = registerMenuType("cast_iron_oven", CastIronOvenMenu::new);
    public static final RegistrySupplier<MenuType<AltarMenu>> ALTAR_MENU = registerMenuType("altar", AltarMenu::new);

    private static <T extends AbstractContainerMenu> RegistrySupplier<MenuType<T>> registerMenuType(String name, ExtendedMenuTypeFactory<T> factory) {
        return MENU_TYPES.register(name, () -> MenuRegistry.ofExtended(factory));
    }

    public static void register() {
        MENU_TYPES.register();
    }
}
