package com.MinBaka.CHNHCore;

import com.sighs.apricityui.instance.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

// 喵！这里的 modid 指向了新的 CHNHCore
@EventBusSubscriber(modid = CHNHCore.MODID, value = Dist.CLIENT)
public final class CHNHCursorDispatcher {
    private static boolean itemTooltipVisibleThisFrame = false;

    private CHNHCursorDispatcher() {}

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        syncCursor();
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        syncCursor();
    }

    @SubscribeEvent
    public static void onScreenRenderPre(ScreenEvent.Render.Pre event) {
        itemTooltipVisibleThisFrame = false;
    }

    @SubscribeEvent
    public static void onTooltipRender(RenderTooltipEvent.Pre event) {
        itemTooltipVisibleThisFrame = !event.getItemStack().isEmpty();
    }

    private static void syncCursor() {
        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.screen;
        if (screen == null) {
            CHNHCursorController.hide();
            if (minecraft.level != null && !minecraft.mouseHandler.isMouseGrabbed()) {
                minecraft.mouseHandler.grabMouse();
            }
            return;
        }
        CHNHCursorController.ensure();
        CHNHCursorController.setCursorStyle(resolveCursorStyle(screen));
    }

    private static CHNHCursorController.CursorStyle resolveCursorStyle(Screen screen) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && !minecraft.player.containerMenu.getCarried().isEmpty()) {
            return CHNHCursorController.CursorStyle.MOVE;
        }
        if (itemTooltipVisibleThisFrame) return CHNHCursorController.CursorStyle.LINK;

        GuiEventListener hovered = findHovered(screen);
        if (hovered instanceof EditBox) return CHNHCursorController.CursorStyle.TEXT;
        if (hovered instanceof AbstractButton button) {
            return button.active ? CHNHCursorController.CursorStyle.LINK : CHNHCursorController.CursorStyle.BLOCK;
        }
        return CHNHCursorController.CursorStyle.NORMAL;
    }

    private static GuiEventListener findHovered(Screen screen) {
        var mouse = Client.getMousePositionDirectly();
        if (mouse == null) return null;
        return screen.getChildAt(mouse.x, mouse.y).orElse(null);
    }
}