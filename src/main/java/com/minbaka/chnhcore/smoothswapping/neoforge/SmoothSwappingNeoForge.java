package com.minbaka.chnhcore.smoothswapping.neoforge;

import com.minbaka.chnhcore.smoothswapping.SmoothSwapping;
import com.minbaka.chnhcore.smoothswapping.config.ConfigScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

@Mod(SmoothSwapping.MOD_ID)
public class SmoothSwappingNeoForge {
    public SmoothSwappingNeoForge() {
        SmoothSwapping.init();
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, parent) -> new ConfigScreen(parent)
        );
    }
}