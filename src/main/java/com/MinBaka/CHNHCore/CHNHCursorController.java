package com.minbaka.chnhcore;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryUtil;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CHNHCursorController {
    private static CursorStyle currentStyle = null;
    private static final Map<CursorStyle, CursorData> CURSORS = new HashMap<>();
    private static long currentTick = 0;

    private CHNHCursorController() {}

    public static void show() {
        setCursorStyle(CursorStyle.NORMAL);
    }

    public static void hide() {
        setCursorStyle(CursorStyle.HIDDEN);
    }

    public static void ensure() {
        if (currentStyle == null) setCursorStyle(CursorStyle.NORMAL);
    }

    public static void tick() {
        currentTick++;
        if (currentStyle != null && currentStyle != CursorStyle.HIDDEN) {
            CursorData data = CURSORS.get(currentStyle);
            if (data != null && data.handles.length > 1) {
                // animate: 1 tick = 50ms, assume ~100ms per frame = 2 ticks per frame
                int frame = (int) ((currentTick / 2) % data.handles.length);
                long handle = data.handles[frame];
                if (handle != 0) {
                    long windowHandle = Minecraft.getInstance().getWindow().getWindow();
                    GLFW.glfwSetCursor(windowHandle, handle);
                }
            }
        }
    }

    public static void setCursorStyle(CursorStyle style) {
        if (style == null) style = CursorStyle.NORMAL;
        if (currentStyle == style) return;

        long windowHandle = Minecraft.getInstance().getWindow().getWindow();
        if (style == CursorStyle.HIDDEN) {
            currentStyle = style;
            // hidden cursor logic...
            return;
        }

        CursorData data = getOrLoadCursor(style);
        if (data != null && data.handles.length > 0) {
            long handle = data.handles[0];
            if (handle != 0) {
                GLFW.glfwSetCursor(windowHandle, handle);
                currentStyle = style;
            }
        } else {
            // fallback
            GLFW.glfwSetCursor(windowHandle, GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR));
            // Don't set currentStyle to style if it's a fallback, so we retry loading next time
        }
    }

    private static CursorData getOrLoadCursor(CursorStyle style) {
        if (CURSORS.containsKey(style)) return CURSORS.get(style);

        Minecraft mc = Minecraft.getInstance();
        if (mc.getResourceManager() == null) return null; // not ready

        CursorData data = new CursorData();
        if (style.frames == 1) {
            data.handles = new long[]{ loadCursorHandle(style.name, -1, style.hotX, style.hotY) };
        } else {
            data.handles = new long[style.frames];
            for (int i = 0; i < style.frames; i++) {
                data.handles[i] = loadCursorHandle(style.name, i, style.hotX, style.hotY);
            }
        }
        CURSORS.put(style, data);
        return data;
    }

    private static long loadCursorHandle(String name, int frame, int hotX, int hotY) {
        String path = "textures/cursor/" + name + (frame >= 0 ? "_" + frame : "") + ".png";
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(CHNHCore.MODID, path);
        try {
            Optional<Resource> res = Minecraft.getInstance().getResourceManager().getResource(loc);
            if (res.isPresent()) {
                try (InputStream is = res.get().open()) {
                    NativeImage originalImage = NativeImage.read(is);
                    int origW = originalImage.getWidth();
                    int origH = originalImage.getHeight();

                    GLFWImage glfwImage = GLFWImage.malloc();
                    glfwImage.width(origW);
                    glfwImage.height(origH);

                    ByteBuffer buffer = MemoryUtil.memAlloc(origW * origH * 4);
                    for (int y = 0; y < origH; y++) {
                        for (int x = 0; x < origW; x++) {
                            int rgba = originalImage.getPixelRGBA(x, y);
                            byte a = (byte) ((rgba >> 24) & 0xFF);
                            byte b = (byte) ((rgba >> 16) & 0xFF);
                            byte g = (byte) ((rgba >> 8) & 0xFF);
                            byte r = (byte) (rgba & 0xFF);
                            buffer.put(r).put(g).put(b).put(a);
                        }
                    }
                    buffer.flip();
                    glfwImage.pixels(buffer);

                    long handle = GLFW.glfwCreateCursor(glfwImage, (int)(hotX * 1.25f), (int)(hotY * 1.25f));
                    glfwImage.free();
                    MemoryUtil.memFree(buffer);
                    originalImage.close();
                    return handle;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static class CursorData {
        long[] handles;
    }

    public enum CursorStyle {
        NORMAL("normal", 1, 4, 1),
        HELP("help", 1, 4, 1),
        LOADING("loading", 12, 11, 11),
        BACKGROUND("background", 41, 4, 1),
        LINK("link", 1, 4, 1),
        MOVE("move", 1, 11, 11),
        TEXT("text", 21, 11, 11),
        PEN("pen", 1, 4, 19),
        BLOCK("block", 1, 11, 11),
        AREA_SELECT("areaselect", 1, 11, 11),
        ALTERNATE_SELECT("alternativeselect", 1, 11, 0),
        RESIZE_NS("resizeNS", 1, 11, 11),
        RESIZE_WE("resizeWE", 1, 11, 11),
        RESIZE_DIAG1("resizeDIAG1", 1, 11, 11),
        RESIZE_DIAG2("resizeDIAG2", 1, 11, 11),
        HIDDEN("hidden", 1, 0, 0);

        public final String name;
        public final int frames;
        public final int hotX;
        public final int hotY;

        CursorStyle(String name, int frames, int hotX, int hotY) {
            this.name = name;
            this.frames = frames;
            this.hotX = hotX;
            this.hotY = hotY;
        }
    }
}
