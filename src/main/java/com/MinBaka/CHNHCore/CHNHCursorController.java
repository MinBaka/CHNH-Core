package com.minbaka.chnhcore;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class CHNHCursorController {
    private static CursorStyle currentStyle = CursorStyle.NORMAL;

    private static final int GLFW_CURSOR_ARROW = GLFW.GLFW_ARROW_CURSOR;
    private static final int GLFW_CURSOR_IBEAM = GLFW.GLFW_IBEAM_CURSOR;
    private static final int GLFW_CURSOR_CROSSHAIR = GLFW.GLFW_CROSSHAIR_CURSOR;
    private static final int GLFW_CURSOR_HAND = GLFW.GLFW_HAND_CURSOR;
    private static final int GLFW_CURSOR_HRESIZE = GLFW.GLFW_HRESIZE_CURSOR;
    private static final int GLFW_CURSOR_VRESIZE = GLFW.GLFW_VRESIZE_CURSOR;
    private static final int GLFW_CURSOR_NWSE_RESIZE = GLFW.GLFW_RESIZE_NWSE_CURSOR;
    private static final int GLFW_CURSOR_NESW_RESIZE = GLFW.GLFW_RESIZE_NESW_CURSOR;
    private static final int GLFW_CURSOR_NOT_ALLOWED = GLFW.GLFW_NOT_ALLOWED_CURSOR;

    private CHNHCursorController() {}

    public static void show() {
        setCursorStyle(CursorStyle.NORMAL);
    }

    public static void show(CursorStyle style) {
        setCursorStyle(style);
    }

    public static void hide() {
        setCursorStyle(CursorStyle.NORMAL);
    }

    public static void ensure() {
        if (currentStyle == null) setCursorStyle(CursorStyle.NORMAL);
    }

    public static void setCursorStyle(CursorStyle style) {
        if (style == null) style = CursorStyle.NORMAL;
        if (currentStyle == style) return;
        currentStyle = style;
        applyCursorStyle(style);
    }

    private static void applyCursorStyle(CursorStyle style) {
        Minecraft mc = Minecraft.getInstance();
        Window window = mc.getWindow();
        if (window == null) return;

        long handle = window.getWindow();
        int glfwCursor = style.toGlfwCursor();
        if (glfwCursor != -1) {
            GLFW.glfwSetCursor(handle, GLFW.glfwCreateStandardCursor(glfwCursor));
        }
        // HIDDEN 无法通过标准光标实现隐藏，需要额外处理，暂不实现
    }

    public enum CursorStyle {
        NORMAL, HELP, LOADING, BACKGROUND, LINK, MOVE, TEXT, PEN, BLOCK,
        AREA_SELECT, ALTERNATE_SELECT, RESIZE_NS, RESIZE_WE, RESIZE_DIAG1, RESIZE_DIAG2, HIDDEN;

        public int toGlfwCursor() {
            return switch (this) {
                case NORMAL, BACKGROUND, LOADING -> GLFW_CURSOR_ARROW;
                case HELP, LINK, MOVE, ALTERNATE_SELECT -> GLFW_CURSOR_HAND;
                case TEXT -> GLFW_CURSOR_IBEAM;
                case PEN, AREA_SELECT -> GLFW_CURSOR_CROSSHAIR;
                case BLOCK -> GLFW_CURSOR_NOT_ALLOWED;
                case RESIZE_NS -> GLFW_CURSOR_VRESIZE;
                case RESIZE_WE -> GLFW_CURSOR_HRESIZE;
                case RESIZE_DIAG1 -> GLFW_CURSOR_NWSE_RESIZE;
                case RESIZE_DIAG2 -> GLFW_CURSOR_NESW_RESIZE;
                case HIDDEN -> -1;
            };
        }
    }
}