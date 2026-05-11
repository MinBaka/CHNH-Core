package com.minbaka.chnhcore;

import com.sighs.apricityui.instance.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = CHNHCore.MODID, value = Dist.CLIENT)
public final class CHNHCursorDispatcher {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        syncCursor();
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        syncCursor();
    }

    private static void syncCursor() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) {
            CHNHCursorController.hide();
            return;
        }
        CHNHCursorController.ensure();
        CHNHCursorController.setCursorStyle(resolveStyle(mc.screen));
    }

    private static CHNHCursorController.CursorStyle resolveStyle(Screen screen) {
        var hovered = screen.getChildAt(Client.getMousePositionDirectly().x, Client.getMousePositionDirectly().y).orElse(null);
        if (hovered instanceof EditBox) return CHNHCursorController.CursorStyle.TEXT;
        if (hovered instanceof AbstractButton btn) return btn.active ? CHNHCursorController.CursorStyle.LINK : CHNHCursorController.CursorStyle.BLOCK;
        return CHNHCursorController.CursorStyle.NORMAL;
    }
}