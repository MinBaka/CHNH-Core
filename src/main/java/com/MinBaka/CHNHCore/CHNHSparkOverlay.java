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
    private static final double TRAIL_STEP = 2.0;
    private static final int MAX_TRAIL_POINTS = 16;
    private static final double EFFECT_RENDER_SCALE = 0.3;

    private static final List<WaveState> WAVES = new ArrayList<>();
    private static final List<SparkState> SPARKS = new ArrayList<>();
    private static final List<TrailPoint> TRAIL = new ArrayList<>();
    private static final List<WaveState> WAVE_POOL = new ArrayList<>();
    private static final List<SparkState> SPARK_POOL = new ArrayList<>();

    private static boolean mouseDown = false;
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
        render(guiGraphics);
        guiGraphics.pose().popPose();
    }

    private static void updateTrailFromCursor() {
        TrailPos pos = getMousePosition();
        if (pos == null) return;
        if (!mouseDown) return;

        if (lastTrailPos == null) {
            lastTrailPos = pos;
            return;
        }

        double dx = pos.x - lastTrailPos.x;
        double dy = pos.y - lastTrailPos.y;
        if (Math.hypot(dx, dy) <= TRAIL_STEP) return;

        TRAIL.add(new TrailPoint(pos.x, pos.y, 1.0));
        if (TRAIL.size() > MAX_TRAIL_POINTS) TRAIL.remove(0);

        if (ThreadLocalRandom.current().nextDouble() < 0.3) {
            spawnDriftSpark(pos.x, pos.y);
        }
        lastTrailPos = pos;
    }

    private static void updateState(double frameScale) {
        Iterator<TrailPoint> trailIt = TRAIL.iterator();
        while (trailIt.hasNext()) {
            TrailPoint p = trailIt.next();
            p.life -= 0.085 * frameScale;
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
        for (TrailPoint p : TRAIL) {
            int alpha = (int) (p.life * effectOpacity * 255);
            if (alpha <= 0) continue;
            int color = (alpha << 24) | (EFFECT_R << 16) | (EFFECT_G << 8) | EFFECT_B;
            int size = (int) Math.max(1, 3.0 * visualScale * p.life);
            guiGraphics.fill((int)p.x - size, (int)p.y - size,
                    (int)p.x + size, (int)p.y + size, color);
        }
    }

    private static void renderWaves(GuiGraphics guiGraphics) {
        double visualScale = effectScale * EFFECT_RENDER_SCALE;
        for (WaveState wave : WAVES) {
            double progress = Math.min(1.0, wave.life / wave.max);
            double ease = 1.0 - Math.pow(1.0 - progress, 3.0);
            double radius = 26.0 * visualScale * ease;
            int alpha = (int) ((1.0 - progress) * effectOpacity * 255);
            if (alpha <= 0) continue;

            int fillColor = (alpha << 24) | (EFFECT_R << 16) | (EFFECT_G << 8) | EFFECT_B;
            guiGraphics.fill((int)(wave.x - radius), (int)(wave.y - radius),
                    (int)(wave.x + radius), (int)(wave.y + radius), fillColor);

            double ringProgress = Math.min(1.0, wave.ring.life / wave.ring.maxLife);
            int ringAlpha = (int) ((1.0 - ringProgress) * effectOpacity * 255);
            if (ringAlpha <= 0) continue;
            int ringColor = (ringAlpha << 24) | 0xF5F8FC;
            double ringRadius = radius + 3.0 * visualScale;
            for (RingSegment seg : wave.ring.segs) {
                double shrink = Math.max(0, 1.0 - ringProgress);
                double len = seg.len * shrink;
                double startAng = wave.ring.ang + seg.off;
                drawArc(guiGraphics, wave.x, wave.y, ringRadius, startAng, startAng + len, 2.5f * visualScale, ringColor);
            }
        }
    }

    private static void drawArc(GuiGraphics gg, double cx, double cy, double r, double startRad, double endRad, double strokeWidth, int color) {
        if (startRad >= endRad) return;
        int steps = Math.max(8, (int)((endRad - startRad) * 10));
        double prevX = cx + r * Math.cos(startRad);
        double prevY = cy + r * Math.sin(startRad);
        for (int i = 1; i <= steps; i++) {
            double t = i / (double) steps;
            double rad = startRad + (endRad - startRad) * t;
            double x = cx + r * Math.cos(rad);
            double y = cy + r * Math.sin(rad);
            drawThickLine(gg, prevX, prevY, x, y, strokeWidth, color);
            prevX = x; prevY = y;
        }
    }

    private static void drawThickLine(GuiGraphics gg, double x1, double y1, double x2, double y2, double width, int color) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double dx = width / 2 * -sin;
        double dy = width / 2 * cos;
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        xPoints[0] = (int)(x1 + dx); yPoints[0] = (int)(y1 + dy);
        xPoints[1] = (int)(x1 - dx); yPoints[1] = (int)(y1 - dy);
        xPoints[2] = (int)(x2 - dx); yPoints[2] = (int)(y2 - dy);
        xPoints[3] = (int)(x2 + dx); yPoints[3] = (int)(y2 + dy);
        for (int i = 0; i < 4; i++) {
            int j = (i + 1) % 4;
            gg.fill(xPoints[i], yPoints[i], xPoints[j], yPoints[j], color);
        }
    }

    private static void renderSparks(GuiGraphics guiGraphics) {
        for (SparkState s : SPARKS) {
            int alpha = (int) (s.a * effectOpacity * 255);
            if (alpha <= 0) continue;
            int color = (alpha << 24) | 0xFFFFFF;
            double size = s.s * 0.8;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(s.x, s.y, 0);
            guiGraphics.pose().mulPose(com.mojang.math.Axis.ZP.rotation((float) s.rot));
            int x0 = 0, y0 = (int) -size;
            int x1 = (int)(size * 0.6), y1 = (int)(size * 0.6);
            int x2 = (int)(-size * 0.6), y2 = (int)(size * 0.6);
            fillTriangle(guiGraphics, x0, y0, x1, y1, x2, y2, color);
            guiGraphics.pose().popPose();
        }
    }

    private static void fillTriangle(GuiGraphics gg, int x0, int y0, int x1, int y1, int x2, int y2, int color) {
        int minX = Math.min(x0, Math.min(x1, x2));
        int maxX = Math.max(x0, Math.max(x1, x2));
        for (int x = minX; x <= maxX; x++) {
            List<Integer> ys = new ArrayList<>();
            for (int[] edge : new int[][]{{x0,y0,x1,y1},{x1,y1,x2,y2},{x2,y2,x0,y0}}) {
                int xA = edge[0], yA = edge[1], xB = edge[2], yB = edge[3];
                if ((xA <= x && xB > x) || (xB <= x && xA > x)) {
                    double t = (x - xA) / (double)(xB - xA);
                    int y = (int)(yA + t * (yB - yA));
                    ys.add(y);
                }
            }
            if (ys.size() >= 2) {
                int yMin = Math.min(ys.get(0), ys.get(1));
                int yMax = Math.max(ys.get(0), ys.get(1));
                gg.fill(x, yMin, x + 1, yMax, color);
            }
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