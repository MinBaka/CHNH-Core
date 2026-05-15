package com.minbaka.chnhcore.precisemanufacturing.foundation.utility;

import com.minbaka.chnhcore.precisemanufacturing.lib.ExtendedRegistrate;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.VirtualFluidBuilder;
import net.createmod.catnip.registrate.builders.FluidBuilder;
import net.createmod.catnip.registrate.util.nullness.NonNullFunction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.ForgeFlowingFluid;

import java.util.function.Consumer;

public class PreciseManufacturingRegistrate extends ExtendedRegistrate {
    /**
     * Construct a new Registrate for the given mod ID.
     *
     * @param modid The mod ID for which objects will be registered
     */
    protected PreciseManufacturingRegistrate(String modid) {
        super(modid);
//        set(ModCreativeModTabs.MOD_TAB.get());
    }

    public static PreciseManufacturingRegistrate create(String modid) {
        return new PreciseManufacturingRegistrate(modid);
    }
}
