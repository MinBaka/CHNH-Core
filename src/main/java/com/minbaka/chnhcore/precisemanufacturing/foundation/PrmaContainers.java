package com.minbaka.chnhcore.precisemanufacturing.foundation;

import com.minbaka.chnhcore.precisemanufacturing.lib.Reference;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IForgeMenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PrmaContainers {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Reference.MOD_ID);

//    public static final RegistryObject<MenuType<DecomponentalizerContainerMenu>> DECOMPONENTALIZER_CONTAINER_MENU = registerMenuType(DecomponentalizerContainerMenu::new, "decomponentalizer_container");
//
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        // TODO: Soft delete. removing everything related to Decomponentalizers.
//        MENUS.register(eventBus);
    }
}
