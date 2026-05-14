package com.minbaka.chnhcore;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@EventBusSubscriber(modid = CHNHCore.MODID, value = Dist.CLIENT)
public class CHNHSparkOverlay {
    private static final double BASE_FRAME_MS = 1000.0 / 60.0;
    private static final double MAX_DELTA_MS = 100.0;
    private static final double TRAIL_STEP = 0.5;
    private static final int MAX_TRAIL_POINTS = 40;
    private static final double EFFECT_RENDER_SCALE = 0.3;

    private static final List<WaveState> WAVES = new ArrayList<>();
    private static final List<SparkState> SPARKS = new ArrayList<>();
    private static final List<TrailPoint> TRAIL = new ArrayList<>();
    private static final List<WaveState> WAVE_POOL = new ArrayList<>();
    private static final List<SparkState> SPARK_POOL = new ArrayList<>();

    private static boolean mouseDown = false;
    private static boolean alwaysTrailEnabled = false;
    private static TrailPos lastTrailPos = null;
    private static long lastFrameMs = 0L;
    private static double effectScale = 1.5;
    private static double effectOpacity = 1.0;
    private static double effectSpeed = 1.0;
    private static final int EFFECT_R = 45;
    private static final int EFFECT_G = 175;
    private static final int EFFECT_B = 255;

    private static class TrailPos { double x, y; TrailPos(double x, double y) { this.x=x; this.y=y; } }

    @SubscribeEvent
    public static void onMouseButton(InputEvent.MouseButton.Pre event) {
        if (event.getButton() != 0) return;
        TrailPos pos = getMousePosition();
        if (event.getAction() == InputConstants.PRESS) {
            mouseDown = true;
            lastTrailPos = pos;
            if (pos != null) spawnBurst(pos.x, pos.y);
        } else if (event.getAction() == InputConstants.RELEASE) {
            mouseDown = false;
            lastTrailPos = null;
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        if (lastFrameMs == 0L) lastFrameMs = System.nanoTime();
    }

    @SubscribeEvent
    public static void drawScreen(ScreenEvent.Render.Post event) {
        if (Minecraft.getInstance().screen != null) {
            renderFrame(event.getGuiGraphics());
        }
    }

    private static void renderFrame(GuiGraphics guiGraphics) {
        long now = System.nanoTime();
        double deltaMs = Math.min((now - lastFrameMs) / 1_000_000.0, MAX_DELTA_MS);
        lastFrameMs = now;
        double frameScale = (deltaMs / BASE_FRAME_MS) * effectSpeed;

        updateTrailFromCursor();
        updateState(frameScale);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 400);

        guiGraphics.flush();
        com.mojang.blaze3d.systems.RenderSystem.enableBlend();
        com.mojang.blaze3d.systems.RenderSystem.blendFunc(com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA, com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE);
        com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
        com.mojang.blaze3d.systems.RenderSystem.depthMask(false);
        com.mojang.blaze3d.systems.RenderSystem.disableCull();

        render(guiGraphics);

        guiGraphics.flush();
        com.mojang.blaze3d.systems.RenderSystem.enableCull();
        com.mojang.blaze3d.systems.RenderSystem.depthMask(true);
        com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
        com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
        com.mojang.blaze3d.systems.RenderSystem.disableBlend();

        guiGraphics.pose().popPose();
    }

    private static void updateTrailFromCursor() {
        TrailPos pos = getMousePosition();
        if (pos == null) return;
        if (!mouseDown && !alwaysTrailEnabled) return;

        if (lastTrailPos == null) {
            lastTrailPos = pos;
            return;
        }

        double dx = pos.x - lastTrailPos.x;
        double dy = pos.y - lastTrailPos.y;
        if (Math.hypot(dx, dy) <= TRAIL_STEP) return;

        TRAIL.add(new TrailPoint(pos.x, pos.y, 1.0));
        if (TRAIL.size() > MAX_TRAIL_POINTS) TRAIL.remove(0);

        if (ThreadLocalRandom.current().nextDouble() < 0.6) {
            spawnDriftSpark(pos.x, pos.y);
        }
        lastTrailPos = pos;
    }

    private static void updateState(double frameScale) {
        Iterator<TrailPoint> trailIt = TRAIL.iterator();
        while (trailIt.hasNext()) {
            TrailPoint p = trailIt.next();
            double fade = alwaysTrailEnabled ? 0.085 : (mouseDown ? 0.085 : 0.18);
            p.life -= fade * frameScale;
            if (p.life <= 0) trailIt.remove();
        }

        Iterator<WaveState> waveIt = WAVES.iterator();
        while (waveIt.hasNext()) {
            WaveState wave = waveIt.next();
            wave.life += frameScale;
            wave.ring.life += frameScale;
            wave.ring.ang -= wave.ring.rs * frameScale;
            if (wave.life >= wave.max && wave.ring.life >= wave.ring.maxLife) {
                WAVE_POOL.add(wave);
                waveIt.remove();
            }
        }

        Iterator<SparkState> sparkIt = SPARKS.iterator();
        while (sparkIt.hasNext()) {
            SparkState s = sparkIt.next();
            s.x += s.vx * frameScale;
            s.y += s.vy * frameScale;
            s.vx *= Math.pow(s.f, frameScale);
            s.vy *= Math.pow(s.f, frameScale);
            s.rot += s.rs * frameScale;
            s.a -= s.fadeRate * frameScale;
            if (s.a <= 0) {
                SPARK_POOL.add(s);
                sparkIt.remove();
            }
        }
    }

    private static void render(GuiGraphics guiGraphics) {
        if (WAVES.isEmpty() && SPARKS.isEmpty() && TRAIL.isEmpty()) return;
        renderTrail(guiGraphics);
        renderWaves(guiGraphics);
        renderSparks(guiGraphics);
    }

    private static void renderTrail(GuiGraphics guiGraphics) {
        if (TRAIL.size() < 2) return;
        double visualScale = effectScale * EFFECT_RENDER_SCALE;

        boolean hasVertices = false;

        com.mojang.blaze3d.vertex.Tesselator tesselator = com.mojang.blaze3d.vertex.Tesselator.getInstance();
        com.mojang.blaze3d.vertex.BufferBuilder bufferbuilder = tesselator.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR);
        org.joml.Matrix4f matrix4f = guiGraphics.pose().last().pose();

        // Draw the outer glow (thicker, lower alpha)
        for (int i = 0; i < TRAIL.size() - 1; i++) {
            TrailPoint p1 = TRAIL.get(i);
            TrailPoint p2 = TRAIL.get(i + 1);
            int alpha1 = (int) (p1.life * effectOpacity * 100);
            int alpha2 = (int) (p2.life * effectOpacity * 100);
            if (alpha1 <= 0 && alpha2 <= 0) continue;

            int avgAlpha = (alpha1 + alpha2) / 2;
            double size = Math.max(1, 6.0 * visualScale * ((p1.life + p2.life) / 2.0));
            addThickLineVertices(bufferbuilder, matrix4f, p1.x, p1.y, p2.x, p2.y, size, EFFECT_R, EFFECT_G, EFFECT_B, avgAlpha);
            hasVertices = true;
        }

        // Draw the inner core (thinner, higher alpha)
        for (int i = 0; i < TRAIL.size() - 1; i++) {
            TrailPoint p1 = TRAIL.get(i);
            TrailPoint p2 = TRAIL.get(i + 1);
            int alpha1 = (int) (p1.life * effectOpacity * 255);
            int alpha2 = (int) (p2.life * effectOpacity * 255);
            if (alpha1 <= 0 && alpha2 <= 0) continue;

            int avgAlpha = (alpha1 + alpha2) / 2;
            double size = Math.max(1, 2.0 * visualScale * ((p1.life + p2.life) / 2.0));
            addThickLineVertices(bufferbuilder, matrix4f, p1.x, p1.y, p2.x, p2.y, size, 255, 255, 255, avgAlpha);
            hasVertices = true;
        }

        if (!hasVertices) {
            // Cancel the build explicitly if nothing was added
            return;
        }

        com.mojang.blaze3d.systems.RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getRendertypeGuiShader);
        com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
    }

    private static void renderWaves(GuiGraphics guiGraphics) {
        if (WAVES.isEmpty()) return;
        double visualScale = effectScale * EFFECT_RENDER_SCALE;

        boolean hasVertices = false;

        com.mojang.blaze3d.vertex.Tesselator tesselator = com.mojang.blaze3d.vertex.Tesselator.getInstance();
        com.mojang.blaze3d.vertex.BufferBuilder bufferbuilder = tesselator.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR);
        org.joml.Matrix4f matrix4f = guiGraphics.pose().last().pose();

        for (WaveState wave : WAVES) {
            double progress = Math.min(1.0, wave.life / wave.max);
            double ease = 1.0 - Math.pow(1.0 - progress, 3.0);
            double radius = 26.0 * visualScale * ease;
            int alpha = (int) ((1.0 - progress) * effectOpacity * 255);
            if (alpha > 0) {
                addCircleVertices(bufferbuilder, matrix4f, wave.x, wave.y, radius, EFFECT_R, EFFECT_G, EFFECT_B, alpha);
                hasVertices = true;
            }

            double ringProgress = Math.min(1.0, wave.ring.life / wave.ring.maxLife);
            int ringAlpha = (int) ((1.0 - ringProgress) * effectOpacity * 255);
            if (ringAlpha <= 0) continue;
            double ringRadius = radius + 3.0 * visualScale;
            for (RingSegment seg : wave.ring.segs) {
                double shrink = Math.max(0, 1.0 - ringProgress);
                double len = seg.len * shrink;
                double startAng = wave.ring.ang + seg.off;
                addArcVertices(bufferbuilder, matrix4f, wave.x, wave.y, ringRadius, startAng, startAng + len, 2.5f * visualScale, 245, 248, 252, ringAlpha);
                hasVertices = true;
            }
        }

        if (!hasVertices) {
            return;
        }

        com.mojang.blaze3d.systems.RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getRendertypeGuiShader);
        com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
    }

    private static void addArcVertices(com.mojang.blaze3d.vertex.BufferBuilder builder, org.joml.Matrix4f matrix, double cx, double cy, double r, double startRad, double endRad, double strokeWidth, int rC, int gC, int bC, int aC) {
        if (startRad >= endRad) return;
        int steps = Math.max(8, (int)((endRad - startRad) * 10));
        double prevX = cx + r * Math.cos(startRad);
        double prevY = cy + r * Math.sin(startRad);
        for (int i = 1; i <= steps; i++) {
            double t = i / (double) steps;
            double rad = startRad + (endRad - startRad) * t;
            double x = cx + r * Math.cos(rad);
            double y = cy + r * Math.sin(rad);
            addThickLineVertices(builder, matrix, prevX, prevY, x, y, strokeWidth, rC, gC, bC, aC);
            prevX = x; prevY = y;
        }
    }

    private static void addThickLineVertices(com.mojang.blaze3d.vertex.BufferBuilder builder, org.joml.Matrix4f matrix, double x1, double y1, double x2, double y2, double width, int r, int g, int b, int a) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double dx = width / 2 * -sin;
        double dy = width / 2 * cos;

        float x0 = (float) (x1 + dx);
        float y0 = (float) (y1 + dy);
        float x1p = (float) (x1 - dx);
        float y1p = (float) (y1 - dy);
        float x2p = (float) (x2 - dx);
        float y2p = (float) (y2 - dy);
        float x3p = (float) (x2 + dx);
        float y3p = (float) (y2 + dy);

        builder.addVertex(matrix, x0, y0, 0).setColor(r, g, b, a);
        builder.addVertex(matrix, x1p, y1p, 0).setColor(r, g, b, a);
        builder.addVertex(matrix, x2p, y2p, 0).setColor(r, g, b, a);
        builder.addVertex(matrix, x3p, y3p, 0).setColor(r, g, b, a);
    }

    private static void renderSparks(GuiGraphics guiGraphics) {
        if (SPARKS.isEmpty()) return;

        boolean hasVertices = false;

        com.mojang.blaze3d.vertex.Tesselator tesselator = com.mojang.blaze3d.vertex.Tesselator.getInstance();
        com.mojang.blaze3d.vertex.BufferBuilder bufferbuilder = tesselator.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLES, com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR);
        org.joml.Matrix4f matrix4f = guiGraphics.pose().last().pose();

        for (SparkState s : SPARKS) {
            int alpha = (int) (s.a * effectOpacity * 255);
            if (alpha <= 0) continue;
            double size = s.s * 0.8;

            double cos = Math.cos(s.rot);
            double sin = Math.sin(s.rot);

            double tx0 = 0;
            double ty0 = -size;
            double tx1 = size * 0.6;
            double ty1 = size * 0.6;
            double tx2 = -size * 0.6;
            double ty2 = size * 0.6;

            float rx0 = (float) (s.x + tx0 * cos - ty0 * sin);
            float ry0 = (float) (s.y + tx0 * sin + ty0 * cos);
            float rx1 = (float) (s.x + tx1 * cos - ty1 * sin);
            float ry1 = (float) (s.y + tx1 * sin + ty1 * cos);
            float rx2 = (float) (s.x + tx2 * cos - ty2 * sin);
            float ry2 = (float) (s.y + tx2 * sin + ty2 * cos);

            bufferbuilder.addVertex(matrix4f, rx0, ry0, 0).setColor(255, 255, 255, alpha);
            bufferbuilder.addVertex(matrix4f, rx1, ry1, 0).setColor(255, 255, 255, alpha);
            bufferbuilder.addVertex(matrix4f, rx2, ry2, 0).setColor(255, 255, 255, alpha);
            hasVertices = true;
        }

        if (!hasVertices) {
            return;
        }

        com.mojang.blaze3d.systems.RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getRendertypeGuiShader);
        com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
    }

    private static void addCircleVertices(com.mojang.blaze3d.vertex.BufferBuilder builder, org.joml.Matrix4f matrix, double cx, double cy, double r, int rc, int gc, int bc, int ac) {
        if (r <= 0) return;
        int segments = Math.max(12, (int)(r * 2));
        float doublePi = (float) (Math.PI * 2.0);

        // Use QUADS by treating center + 2 edge points + center as a quad
        for (int i = 0; i < segments; i++) {
            float angle1 = (i / (float) segments) * doublePi;
            float angle2 = ((i + 1) / (float) segments) * doublePi;

            float x1 = (float) (cx + r * Math.cos(angle1));
            float y1 = (float) (cy + r * Math.sin(angle1));
            float x2 = (float) (cx + r * Math.cos(angle2));
            float y2 = (float) (cy + r * Math.sin(angle2));

            builder.addVertex(matrix, (float) cx, (float) cy, 0).setColor(rc, gc, bc, ac);
            builder.addVertex(matrix, x1, y1, 0).setColor(rc, gc, bc, ac);
            builder.addVertex(matrix, x2, y2, 0).setColor(rc, gc, bc, ac);
            builder.addVertex(matrix, (float) cx, (float) cy, 0).setColor(rc, gc, bc, ac);
        }
    }

    // 鼠标位置获取（内嵌，不依赖外部类）
    private static TrailPos getMousePosition() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) return null;
        double x = mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth();
        double y = mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight();

        // 手动裁剪坐标
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        x = Math.max(0, Math.min(x, width - 1));
        y = Math.max(0, Math.min(y, height - 1));

        return new TrailPos(x, y);
    }

    private static void spawnBurst(double x, double y) {
        WaveState wave;
        if (!WAVE_POOL.isEmpty()) {
            wave = WAVE_POOL.remove(WAVE_POOL.size() - 1);
            wave.x = x; wave.y = y;
            wave.life = 0;
            wave.max = 18;
            wave.r = 0;
            wave.ring.ang = randomAngle();
            wave.ring.life = 0;
        } else {
            wave = new WaveState(x, y);
        }
        WAVES.add(wave);

        int particleCount = 4;
        double visualScale = effectScale * EFFECT_RENDER_SCALE;
        double speedAdjust = visualScale / 1.5;
        for (int i = 0; i < particleCount; i++) {
            double angle = randomAngle();
            double speed = (4.8 + ThreadLocalRandom.current().nextDouble() * 2.0) * speedAdjust;
            SparkState s = obtainSpark();
            s.x = x; s.y = y;
            s.vx = Math.cos(angle) * speed;
            s.vy = Math.sin(angle) * speed;
            s.rot = randomAngle();
            s.rs = (ThreadLocalRandom.current().nextDouble() - 0.5) * 0.28;
            s.s = (4.0 + ThreadLocalRandom.current().nextDouble() * 3.0) * visualScale;
            s.a = 1.0;
            s.f = 0.9;
            s.fadeRate = 0.032;
            SPARKS.add(s);
        }
    }

    private static void spawnDriftSpark(double x, double y) {
        double angle = randomAngle();
        double visualScale = effectScale * EFFECT_RENDER_SCALE;
        double speedAdjust = visualScale / 1.5;
        SparkState s = obtainSpark();
        s.x = x + Math.cos(angle) * 10 * visualScale;
        s.y = y + Math.sin(angle) * 10 * visualScale;
        s.vx = Math.cos(angle) * 1.3 * speedAdjust;
        s.vy = Math.sin(angle) * 1.3 * speedAdjust;
        s.rot = randomAngle();
        s.rs = 0.16;
        s.s = 9 * visualScale;
        s.a = 0.7;
        s.f = 0.95;
        s.fadeRate = 0.026;
        SPARKS.add(s);
    }

    private static SparkState obtainSpark() {
        if (!SPARK_POOL.isEmpty()) return SPARK_POOL.remove(SPARK_POOL.size() - 1);
        return new SparkState();
    }

    private static double randomAngle() {
        return ThreadLocalRandom.current().nextDouble() * Math.PI * 2;
    }

    // ---- 内部数据类 ----
    private static final class TrailPoint {
        double x, y, life;
        TrailPoint(double x, double y, double life) { this.x = x; this.y = y; this.life = life; }
    }

    private static final class WaveState {
        double x, y, life, max = 18.0, r;
        final RingState ring = new RingState();
        WaveState(double x, double y) { this.x = x; this.y = y; }
    }

    private static final class RingState {
        double ang = randomAngle();
        final RingSegment[] segs = {
                new RingSegment(-0.25 * Math.PI, 1.15 * Math.PI),
                new RingSegment(0.0, 1.15 * Math.PI),
                new RingSegment(0.25 * Math.PI, 1.15 * Math.PI)
        };
        double life, maxLife = 30.0, rs = 0.08;
    }

    private record RingSegment(double off, double len) {}

    private static final class SparkState {
        double x, y, vx, vy, rot, rs, s, a, f, fadeRate;
    }
}