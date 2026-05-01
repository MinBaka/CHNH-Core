package com.minbaka.chnh.core.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AmmoBlueprintItem extends Item {

    public AmmoBlueprintItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getDisplayName(ItemStack stack) {
        String ammoName = "未知";
        if (stack.hasTag() && stack.getTag().contains("AmmoId")) {
            String ammoId = stack.getTag().getString("AmmoId");
            String[] parts = ammoId.split(":", 2);
            if (parts.length == 2) {
                String translateKey = parts[0] + ".ammo." + parts[1] + ".name";
                ammoName = Component.translatable(translateKey).getString();
            }
        }
        return Component.literal("子弹蓝图[" + ammoName + "]");
    }
}