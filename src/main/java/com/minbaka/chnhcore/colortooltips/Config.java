package com.minbaka.chnhcore.colortooltips;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = ColorTooltips.MODID)
public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLED;
    public static final ModConfigSpec.BooleanValue CUSTOM_HEADER_ENABLED;
    public static final ModConfigSpec.DoubleValue BG_ALPHA;
    public static final ModConfigSpec.DoubleValue BG_DARKEN;
    public static final ModConfigSpec.BooleanValue BORDER_GRADIENT_ENABLED;
    public static final ModConfigSpec.BooleanValue TITLEBAR_GRADIENT_ENABLED;
    public static final ModConfigSpec.DoubleValue HUE_VARIATION;
    public static final ModConfigSpec.DoubleValue VALUE_VARIATION;
    public static final ModConfigSpec.DoubleValue SATURATION_VARIATION;
    public static final ModConfigSpec.IntValue SWITCH_TAIL_SEGMENTS;
    public static final ModConfigSpec.DoubleValue SWITCH_TAIL_LENGTH;
    public static final ModConfigSpec.DoubleValue SWITCH_TAIL_ALPHA_EXPONENT;
    public static final ModConfigSpec.IntValue FADE_OUT_DELAY;
    public static final ModConfigSpec.IntValue FADE_IN_DURATION;
    public static final ModConfigSpec.IntValue SWITCH_FLASH_DURATION;
    public static final ModConfigSpec.DoubleValue ITEM_SCALE_MIN;
    public static final ModConfigSpec.DoubleValue GRADIENT_PERIOD;
    public static final ModConfigSpec.DoubleValue SCROLL_SPEED;
    public static final ModConfigSpec.DoubleValue LOCK_SCROLL_SENSITIVITY;
    public static final ModConfigSpec.BooleanValue SHOW_RARITY_CORE_WARNING;

    static {
        BUILDER.push("options");

        ENABLED = BUILDER.comment("启用彩色提示框 / Enable colored tooltips").define("enabled", true);
        CUSTOM_HEADER_ENABLED = BUILDER.comment("启用自定义标题栏（物品图标+白色名称+稀有度文字），关闭则保留原版物品名称。未安装 RarityCore 时强制禁用 / Enable custom header (item icon + white name + rarity text), disable to keep vanilla item name. Forced disabled without RarityCore").define("customHeaderEnabled", true);
        BG_ALPHA = BUILDER.comment("背景透明度 (0.0-1.0) / Background alpha").defineInRange("bgAlpha", 0.97, 0.0, 1.0);
        BG_DARKEN = BUILDER.comment("背景暗度 / Background darken factor").defineInRange("bgDarken", 0.7, 0.0, 1.0);
        BORDER_GRADIENT_ENABLED = BUILDER.comment("启用边框渐变效果 / Enable border gradient effect").define("borderGradientEnabled", true);
        TITLEBAR_GRADIENT_ENABLED = BUILDER.comment("启用标题栏渐变效果 / Enable titlebar gradient effect").define("titlebarGradientEnabled", true);

        BUILDER.pop();

        BUILDER.push("color_variation");

        HUE_VARIATION = BUILDER.comment("色相变化范围 (0.0-1.0) / Hue variation range").defineInRange("hueVariation", 0.1, 0.0, 1.0);
        VALUE_VARIATION = BUILDER.comment("明度变化范围 (0.0-1.0) / Value variation range").defineInRange("valueVariation", 0.4, 0.0, 1.0);
        SATURATION_VARIATION = BUILDER.comment("饱和度变化范围 (0.0-1.0) / Saturation variation range").defineInRange("saturationVariation", 0.1, 0.0, 1.0);

        BUILDER.pop();

        BUILDER.push("switch_animation");

        SWITCH_TAIL_SEGMENTS = BUILDER.comment("拖尾分段数 (10-100) / Trail segments").defineInRange("switchTailSegments", 50, 10, 100);
        SWITCH_TAIL_LENGTH = BUILDER.comment("拖尾长度因子 (0.1-2.0) / Trail length factor").defineInRange("switchTailLength", 1.6, 0.1, 2.0);
        SWITCH_TAIL_ALPHA_EXPONENT = BUILDER.comment("拖尾透明度衰减指数 (0.1-1.0, 越小越明显) / Trail alpha decay exponent").defineInRange("switchTailAlphaExponent", 0.5, 0.1, 1.0);
        FADE_OUT_DELAY = BUILDER.comment("淡出延迟/毫秒 (0-1000) / Fade out delay in ms").defineInRange("fadeOutDelay", 100, 0, 1000);
        FADE_IN_DURATION = BUILDER.comment("淡入时长/毫秒 (0-500) / Fade in duration in ms").defineInRange("fadeInDuration", 100, 0, 500);
        SWITCH_FLASH_DURATION = BUILDER.comment("闪光动画时长/毫秒 (0-1000) / Switch flash duration in ms").defineInRange("switchFlashDuration", 500, 0, 1000);
        ITEM_SCALE_MIN = BUILDER.comment("物品出现最小缩放 (0.1-1.0) / Item scale minimum").defineInRange("itemScaleMin", 0.5, 0.1, 1.0);

        BUILDER.pop();

        BUILDER.push("color_flow");

        GRADIENT_PERIOD = BUILDER.comment("颜色渐变周期 (50-500) / Gradient period").defineInRange("gradientPeriod", 200.0, 50.0, 500.0);
        SCROLL_SPEED = BUILDER.comment("颜色滚动速度 (0.01-0.2) / Scroll speed").defineInRange("scrollSpeed", 0.05, 0.01, 0.2);

        BUILDER.pop();

        BUILDER.push("tooltip_lock");

        LOCK_SCROLL_SENSITIVITY = BUILDER.comment("滚轮移动提示框灵敏度 (1.0-20.0) / Scroll sensitivity for tooltip movement").defineInRange("lockScrollSensitivity", 10.0, 1.0, 20.0);

        BUILDER.pop();

        SHOW_RARITY_CORE_WARNING = BUILDER.comment("未安装 RarityCore 时显示警告 / Show warning when RarityCore is not installed").define("showRarityCoreWarning", true);
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }
}