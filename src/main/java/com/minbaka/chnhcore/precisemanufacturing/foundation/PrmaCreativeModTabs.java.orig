package com.minbaka.chnhcore.precisemanufacturing.foundation;

import com.minbaka.chnhcore.precisemanufacturing.lib.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PrmaCreativeModTabs {

    // A Note to future me:
    // The time that when this class is initialized, the RifleBase and CartridgeBases are not yet generated, thus no
    // stored sub-registry items for those corresponding items. One probable fix for this might be moving the items
    // registry in front of this registry, but that wouldn't make sense since the items registry are already dependent
    // on the mod tab at the start.

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("prma_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(PrmaItems.SULFUR_POWDER.get()))
                    .displayItems(((itemDisplayParameters, output) -> {
                        PrmaItems.ALL_ITEMS.forEach(i -> output.accept(i.get()));
                    }))
                    .title(Component.translatable("itemGroup.prma_tab"))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_MATERIALS_TAB = CREATIVE_MODE_TABS.register("prma_materials_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(PrmaItems.Ammo.SMALL_COPPER_MEDIUM_GUNPOWDER_CARTRIDGE.getItem().get()))
                    .displayItems(((itemDisplayParameters, output) -> {
                        PrmaItems.ALL_MATERIALS.forEach(i -> output.accept(i.get()));
                    }))
                    .title(Component.translatable("itemGroup.prma_materials_tab"))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_HIDDEN_TAB = CREATIVE_MODE_TABS.register("prma_hidden_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.AIR))
            .title(Component.translatable("itemGroup.prma_hidden_tab"))
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static enum Tabs {
        Main,
        Materials
    }
}
