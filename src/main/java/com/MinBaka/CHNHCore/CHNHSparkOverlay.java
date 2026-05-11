package com.minbaka.chnhcore;

import com.sighs.apricityui.render.Graph;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = CHNHCore.MODID, value = Dist.CLIENT)
public class CHNHSparkOverlay {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        // 喵！这里利用 ApricityUI 的 Graph 画粒子喵
        // 暂时留空确保编译，主人可以在这里加 SPARKS 的逻辑喵
    }
}