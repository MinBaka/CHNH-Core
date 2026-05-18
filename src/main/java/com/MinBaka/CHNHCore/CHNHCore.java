package com.minbaka.chnhcore;

import com.minbaka.chnhcore.precisemanufacturing.PreciseManufacturing;
import com.minbaka.chnhcore.smoothswapping.SmoothSwapping;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CHNHCore.MODID)
public class CHNHCore {
    public static final String MODID = "chnh_core";

    public CHNHCore(IEventBus modEventBus) {
        new PreciseManufacturing(modEventBus);
        SmoothSwapping.init();
    }
}