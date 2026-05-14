package com.minbaka.chnhcore;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderStateShard;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

public class CheckRenderType extends RenderStateShard {
    public CheckRenderType() {
        super("check", null, null);
    }
    public static final RenderType SPARK_QUADS = RenderType.create(
        "spark_quads",
        DefaultVertexFormat.POSITION_COLOR,
        VertexFormat.Mode.QUADS,
        256,
        false,
        false,
        RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_GUI_SHADER)
            .setTransparencyState(LIGHTNING_TRANSPARENCY)
            .setDepthTestState(LEQUAL_DEPTH_TEST)
            .createCompositeState(false)
    );
}
