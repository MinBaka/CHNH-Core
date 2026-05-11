package com.sighs.baeffect;

import com.sighs.apricityui.render.Graph;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = Baeffect.MODID, value = Dist.CLIENT)
public class BASparkOverlay {
    private static final List<Spark> SPARKS = new ArrayList<>();

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (SPARKS.isEmpty()) return;

        Matrix4f matrix = event.getGuiGraphics().pose().last().pose();
        Iterator<Spark> it = SPARKS.iterator();
        while (it.hasNext()) {
            Spark s = it.next();
            // 使用 Graph 类里的 drawFillRect 喵！
            Graph.drawFillRect(matrix, (float)s.x, (float)s.y, 4f, 4f, 0xFF2DAFFF);
            s.life--;
            if (s.life <= 0) it.remove();
        }
    }

    private static class Spark {
        double x, y;
        int life = 20;
    }
}