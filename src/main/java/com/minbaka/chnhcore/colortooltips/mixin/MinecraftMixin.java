package com.minbaka.chnhcore.colortooltips.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "destroy", at = @At("HEAD"))
    private void onDestroy(CallbackInfo ci) {
        try {
            Class<?> ixerisClass = Class.forName("me.decce.ixeris.core.Ixeris");
            java.lang.reflect.Field shouldExitField = ixerisClass.getDeclaredField("shouldExit");
            shouldExitField.setAccessible(true);
            shouldExitField.set(null, true);
            
            Class<?> dispatcherClass = Class.forName("me.decce.ixeris.core.threading.MainThreadDispatcher");
            java.lang.reflect.Field pollEventsField = dispatcherClass.getDeclaredField("pollEvents");
            pollEventsField.setAccessible(true);
            pollEventsField.set(null, false);
            
            java.lang.reflect.Field mainThreadField = ixerisClass.getDeclaredField("mainThread");
            mainThreadField.setAccessible(true);
            Thread mainThread = (Thread) mainThreadField.get(null);
            if (mainThread != null) {
                mainThread.interrupt();
            }
        } catch (Throwable t) {
            // Ignore if Ixeris is not installed
        }
    }
}
