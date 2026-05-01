package com.minbaka.chnh.core;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.IntValue EXAMPLE_INT;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        EXAMPLE_INT = builder
                .comment("一个示例整数配置")
                .defineInRange("exampleInt", 42, 0, 100);
        SPEC = builder.build();
    }
}