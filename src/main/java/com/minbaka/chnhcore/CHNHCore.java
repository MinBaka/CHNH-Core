package com.minbaka.chnhcore;

import com.minbaka.chnhcore.precisemanufacturing.PreciseManufacturing;
import com.minbaka.chnhcore.smoothswapping.SmoothSwapping;
import com.minbaka.chnhcore.colortooltips.ColorTooltips;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(CHNHCore.MODID)
public class CHNHCore {
    public static final String MODID = "chnh_core";

    public CHNHCore(IEventBus modEventBus, ModContainer modContainer) {
        new PreciseManufacturing(modEventBus);
        new ColorTooltips(modEventBus, modContainer);
        SmoothSwapping.init();
        com.minbaka.chnhcore.raritycore.api.RarityCoreAPI.init();
    }
}