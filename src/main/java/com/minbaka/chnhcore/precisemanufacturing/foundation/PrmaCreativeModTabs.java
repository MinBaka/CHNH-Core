package com.minbaka.chnhcore.precisemanufacturing.foundation;

import com.minbaka.chnhcore.precisemanufacturing.lib.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PrmaCreativeModTabs {

    // A Note to future me:
    // The time that when this class is initialized, the RifleBase and CartridgeBases are not yet generated, thus no
    // stored sub-registry items for those corresponding items. One probable fix for this might be moving the items
    // registry in front of this registry, but that wouldn't make sense since the items registry are already dependent
    // on the mod tab at the start.

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("chnh_core_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(PrmaItems.SULFUR_POWDER.get()))
                    .title(Component.translatable("itemGroup.chnh_core_tab"))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_MATERIALS_TAB = CREATIVE_MODE_TABS.register("chnh_core_materials_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(PrmaItems.Ammo.SMALL_COPPER_MEDIUM_GUNPOWDER_CARTRIDGE.getItem().get()))
                    .title(Component.translatable("itemGroup.chnh_core_materials_tab"))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_HIDDEN_TAB = CREATIVE_MODE_TABS.register("chnh_core_hidden_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.AIR))
            .title(Component.translatable("itemGroup.chnh_core_hidden_tab"))
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
        eventBus.addListener(PrmaCreativeModTabs::buildContents);
    }

    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == MOD_MATERIALS_TAB.get()) {
            PrmaItems.ALL_MATERIALS.stream().map(java.util.function.Supplier::get).distinct().forEach(event::accept);
        }
        if (event.getTab() == MOD_TAB.get()) {
            // Registrate adds things here by default, but to be safe and ensure ordering if needed:
            // PrmaItems.ALL_ITEMS.stream().map(java.util.function.Supplier::get).distinct().forEach(event::accept);
            // We will let Registrate handle it to avoid duplicate exceptions.
        }
    }

    public static enum Tabs {
        Main,
        Materials
    }
}
