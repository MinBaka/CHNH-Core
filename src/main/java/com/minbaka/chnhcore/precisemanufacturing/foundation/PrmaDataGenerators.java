package com.minbaka.chnhcore.precisemanufacturing.foundation;

import com.minbaka.chnhcore.precisemanufacturing.foundation.data.providers.ModRecipeProvider;
import com.minbaka.chnhcore.precisemanufacturing.lib.Reference;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PrmaDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(true, new ModRecipeProvider(gen));

    }
}
