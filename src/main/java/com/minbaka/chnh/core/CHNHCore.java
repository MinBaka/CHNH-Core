package com.minbaka.chnh.core;

import com.minbaka.chnh.core.init.ModItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(CHNHCore.MODID)
public class CHNHCore {
    public static final String MODID = "chnh_core";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CHNHCore(IEventBus modEventBus, ModContainer modContainer) {
        // 注册我们自己的物品（chnh_core 命名空间）
        ModItems.ITEMS.register(modEventBus);

        // 注册配置文件
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        LOGGER.info("CHNH Core 加载完成！齿轮开始制造弹药...");
    }
}