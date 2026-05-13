package com.minbaka.chnhcore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = CHNHCore.MODID, value = Dist.CLIENT)
public final class CHNHCursorDispatcher {
    private static boolean itemTooltipVisibleThisFrame = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        syncCursor();
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        syncCursor();
    }

    @SubscribeEvent
    public static void onScreenRenderPre(ScreenEvent.Render.Pre event) {
        itemTooltipVisibleThisFrame = false;
    }

    @SubscribeEvent
    public static void onTooltipRender(RenderTooltipEvent.Pre event) {
        if (!event.getItemStack().isEmpty()) {
            itemTooltipVisibleThisFrame = true;
        }
    }

    private static void syncCursor() {
        Minecraft mc = Minecraft.getInstance();
        Screen screen = mc.screen;
        if (screen == null) {
            CHNHCursorController.hide();
            if (mc.level != null && mc.mouseHandler != null && !mc.mouseHandler.isMouseGrabbed()) {
                mc.mouseHandler.grabMouse();
            }
            return;
        }
        CHNHCursorController.ensure();
        CHNHCursorController.setCursorStyle(resolveCursorStyle(screen));
    }

    private static CHNHCursorController.CursorStyle resolveCursorStyle(Screen screen) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null && !mc.player.containerMenu.getCarried().isEmpty())
            return CHNHCursorController.CursorStyle.MOVE;

        if (itemTooltipVisibleThisFrame)
            return CHNHCursorController.CursorStyle.LINK;

        GuiEventListener hovered = findHovered(screen);
        if (hovered instanceof EditBox)
            return CHNHCursorController.CursorStyle.TEXT;
        if (hovered instanceof AbstractButton button)
            return button.isActive() ? CHNHCursorController.CursorStyle.LINK : CHNHCursorController.CursorStyle.BLOCK;
        if (hovered instanceof AbstractWidget widget && !widget.isActive())
            return CHNHCursorController.CursorStyle.BLOCK;

        GuiEventListener focused = screen.getFocused();
        if (focused instanceof EditBox)
            return CHNHCursorController.CursorStyle.TEXT;
        if (focused instanceof AbstractButton button)
            return button.isActive() ? CHNHCursorController.CursorStyle.ALTERNATE_SELECT : CHNHCursorController.CursorStyle.BLOCK;
        if (focused instanceof AbstractWidget widget && !widget.isActive())
            return CHNHCursorController.CursorStyle.BLOCK;

        return CHNHCursorController.CursorStyle.NORMAL;
    }

    private static GuiEventListener findHovered(Screen screen) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) return null;
        double mouseX = mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth();
        double mouseY = mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        mouseX = Math.max(0, Math.min(mouseX, width - 1));
        mouseY = Math.max(0, Math.min(mouseY, height - 1));
        return screen.getChildAt(mouseX, mouseY).orElse(null);
    }
}