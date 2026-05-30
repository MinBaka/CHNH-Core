package com.minbaka.chnhcore.colortooltips;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import org.slf4j.Logger;
import com.minbaka.chnhcore.colortooltips.client.MissingRarityScreen;
import com.minbaka.chnhcore.colortooltips.client.TooltipEventHandler;
import com.minbaka.chnhcore.colortooltips.compat.RarityCoreProxy;
import com.minbaka.chnhcore.colortooltips.tooltip.ColorHeaderClientTooltipComponent;
import com.minbaka.chnhcore.colortooltips.tooltip.ColorHeaderComponent;
import com.minbaka.chnhcore.colortooltips.animation.TooltipLockManager;


public class ColorTooltips {

    public static final String MODID = com.minbaka.chnhcore.CHNHCore.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public ColorTooltips(IEventBus modEventBus, net.neoforged.fml.ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, Config.SPEC, "chnh_core/common.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("ColorTooltips commonSetup");
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {

        private static boolean hasShownRarityCoreWarning = false;

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("ColorTooltips clientSetup");
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(TooltipEventHandler.class);
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(TooltipLockManager.class);

            // 注册 RarityCore 缺失警告监听器
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener((ScreenEvent.Opening openingEvent) -> {
                // 只在标题画面打开时检查
                if (!(openingEvent.getNewScreen() instanceof TitleScreen)) return;
                // 已在本会话中显示过，不再重复弹出
                if (hasShownRarityCoreWarning) return;
                // 已安装 RarityCore，不需要警告
                if (RarityCoreProxy.isLoaded()) return;
                // 配置中已禁用警告
                if (!Config.SHOW_RARITY_CORE_WARNING.get()) return;

                hasShownRarityCoreWarning = true;
                openingEvent.setNewScreen(new MissingRarityScreen());
            });
        }

        @SubscribeEvent
        public static void onRegisterTooltipFactories(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(ColorHeaderComponent.class, ColorHeaderClientTooltipComponent::new);
        }
    }
}