package com.minbaka.chnhcore.smoothswapping.swaps;

import com.minbaka.chnhcore.smoothswapping.Vec2;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class ItemToItemInventorySwap extends InventorySwap {
    public ItemToItemInventorySwap(Slot fromSlot, Slot toSlot, boolean checked, int amount, ItemStack swapStack) {
        super(new Vec2(fromSlot.x, fromSlot.y), new Vec2(toSlot.x, toSlot.y), swapStack, checked, amount);
    }
}
