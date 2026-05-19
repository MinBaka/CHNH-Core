package com.minbaka.chnhcore.smoothswapping.mixin;

import appeng.client.gui.AEBaseScreen;
import com.minbaka.chnhcore.smoothswapping.SmoothSwapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AEBaseScreen.class, remap = false)
public class AEBaseScreenMixin {
    @Inject(method = "renderSlot(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;)V", at = @At("HEAD"))
    public void onRenderSlotHead(GuiGraphics guiGraphics, Slot s, CallbackInfo ci) {
        SmoothSwapping.currentlyRenderingSlotIndex = s.index;
    }

    @Inject(method = "renderSlot(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;)V", at = @At("TAIL"))
    public void onRenderSlotTail(GuiGraphics guiGraphics, Slot s, CallbackInfo ci) {
        SmoothSwapping.currentlyRenderingSlotIndex = -1;
    }
}
