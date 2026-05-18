package com.minbaka.chnhcore.smoothswapping.mixin;

import com.minbaka.chnhcore.smoothswapping.SmoothSwapping;
import com.minbaka.chnhcore.smoothswapping.SwapUtil;
import com.minbaka.chnhcore.smoothswapping.config.ConfigManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ServerboundContainerClickPacket.class)
public class ClickSlotPacketMixin {

    @Shadow
    @Final
    private ClickType clickType;

    @Shadow
    @Final
    private Int2ObjectMap<ItemStack> changedSlots;

    //id of slot that got clicked/hovered over
    @Shadow
    @Final
    private int slotNum;

    @Inject(method = "<init>(IIIILnet/minecraft/world/inventory/ClickType;Lnet/minecraft/world/item/ItemStack;Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;)V", at = @At("TAIL"))
    public void onInit(CallbackInfo cbi) {
        if (!ConfigManager.getConfig().getToggleMod())
            return;
        //remove swap when stack gets moved before it arrived
        SmoothSwapping.swaps.remove(slotNum);

        if ((clickType == ClickType.QUICK_MOVE || clickType == ClickType.SWAP) && changedSlots.size() > 1 && Minecraft.getInstance().screen instanceof AbstractContainerScreen) {
            assert Minecraft.getInstance().player != null;
            LocalPlayer player = Minecraft.getInstance().player;
            AbstractContainerMenu screenHandler = player.containerMenu;

            if (slotNum >= 0 && slotNum < screenHandler.slots.size()) {
                Slot mouseHoverSlot = screenHandler.getSlot(slotNum);

                if (clickType == ClickType.QUICK_MOVE && !mouseHoverSlot.mayPickup(player)) {

                    ItemStack newMouseStack = changedSlots.get(slotNum);
                    ItemStack oldMouseStack = smooth_Swapping$getSafeOldStack(slotNum);

                    //only if new items are less or equal (crafting table output for example)
                    if (newMouseStack != null && oldMouseStack != null && newMouseStack.getCount() - oldMouseStack.getCount() <= 0) {
                        SmoothSwapping.clickSwapStack = slotNum;
                    }
                } else if (clickType == ClickType.SWAP) {
                    SmoothSwapping.clickSwap = true;

                    for (Map.Entry<Integer, ItemStack> stackEntry : changedSlots.int2ObjectEntrySet()) {
                        int destinationSlotID = stackEntry.getKey();

                        if (destinationSlotID >= 0 && destinationSlotID < screenHandler.slots.size() && destinationSlotID != slotNum) {
                            Slot destinationSlot = screenHandler.getSlot(destinationSlotID);

                            ItemStack destinationOldStack = smooth_Swapping$getSafeOldStack(destinationSlotID);

                            if (!mouseHoverSlot.mayPickup(player) && destinationSlot.mayPickup(player)) {
                                if (destinationOldStack.isEmpty()) {
                                    SwapUtil.addI2IInventorySwap(destinationSlotID, mouseHoverSlot, destinationSlot, false, destinationSlot.getItem().getCount());
                                }
                            } else if (mouseHoverSlot.mayPickup(player) && destinationSlot.mayPickup(player)) {
                                if (destinationSlot.hasItem()) {
                                    SwapUtil.addI2IInventorySwap(destinationSlotID, mouseHoverSlot, destinationSlot, false, destinationSlot.getItem().getCount());
                                }
                                if (mouseHoverSlot.hasItem()) {
                                    SwapUtil.addI2IInventorySwap(slotNum, destinationSlot, mouseHoverSlot, false, mouseHoverSlot.getItem().getCount());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Unique
    private ItemStack smooth_Swapping$getSafeOldStack(int slot) {
        NonNullList<ItemStack> oldStacks = SmoothSwapping.oldStacks;
        if (oldStacks == null) {
            oldStacks = NonNullList.create();
            SmoothSwapping.oldStacks = oldStacks;
        }
        if (slot < 0 || slot >= oldStacks.size()) {
            return ItemStack.EMPTY;
        }
        return oldStacks.get(slot);
    }
}
