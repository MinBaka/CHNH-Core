package com.minbaka.chnhcore.colortooltips.compat;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.minbaka.chnhcore.raritycore.api.RarityCoreAPI;
import com.minbaka.chnhcore.raritycore.config.ClientConfigManager;

public final class RarityCoreProxy {

    public static final int FALLBACK_BORDER_COLOR = 0xFFFFFFFF;       // 白色
    public static final int FALLBACK_INNER_BORDER_COLOR = 0xFF000000;  // 黑色
    public static final int FALLBACK_BG_COLOR = 0xFF000000;            // 黑色
    public static final int FALLBACK_GRADIENT_BAR_COLOR = 0xFF808080; // 灰色

    private RarityCoreProxy() {}

    public static boolean isLoaded() {
        return true;
    }

    public static int getRarity(ItemStack stack) {
        return RarityCoreAPI.getRarity(stack);
    }

    public static int getNormalizedRarity(ItemStack stack) {
        return RarityCoreAPI.getNormalizedRarity(stack);
    }

    public static int getRarityArgbColor(int rarity) {
        int color = RarityCoreAPI.getRarityRgbColor(rarity);
        // Ensure alpha channel is present
        return color | 0xFF000000;
    }

    public static String getLocalizedRarityTooltip(ItemStack itemStack) {
        return RarityCoreAPI.getLocalizedTooltip(itemStack);
    }

    public static String getLocalizedRarityTooltip(Item item) {
        return RarityCoreAPI.getLocalizedTooltip(item);
    }

    public static void startTooltipItemRendering() {
        // No longer strictly needed or requires specific implementation based on context
    }

    public static void endTooltipItemRendering() {
        // No longer strictly needed
    }
}
