package com.minbaka.chnhcore.colortooltips.compat;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class RarityCoreProxy {

    public static final int FALLBACK_BORDER_COLOR = 0xFFFFFFFF;       // 白色
    public static final int FALLBACK_INNER_BORDER_COLOR = 0xFF000000;  // 黑色
    public static final int FALLBACK_BG_COLOR = 0xFF000000;            // 黑色
    public static final int FALLBACK_GRADIENT_BAR_COLOR = 0xFF808080; // 灰色

    private RarityCoreProxy() {}

    public static boolean isLoaded() {
        return false;
    }

    public static int getRarity(ItemStack stack) {
        return 1;
    }

    public static int getNormalizedRarity(ItemStack stack) {
        return 1;
    }

    public static int getRarityArgbColor(int rarity) {
        return FALLBACK_BORDER_COLOR;
    }

    public static String getLocalizedRarityTooltip(ItemStack itemStack) {
        return "";
    }

    public static String getLocalizedRarityTooltip(Item item) {
        return "";
    }

    public static void startTooltipItemRendering() {
    }

    public static void endTooltipItemRendering() {
    }
}
