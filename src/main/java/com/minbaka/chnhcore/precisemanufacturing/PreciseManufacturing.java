package com.minbaka.chnhcore.precisemanufacturing;

import com.minbaka.chnhcore.precisemanufacturing.foundation.*;
import com.minbaka.chnhcore.precisemanufacturing.foundation.handler.PacketHandler;
import com.minbaka.chnhcore.precisemanufacturing.foundation.utility.PreciseManufacturingRegistrate;
import com.minbaka.chnhcore.precisemanufacturing.lib.Reference;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.slf4j.Logger;

import java.util.stream.Collectors;

public class PreciseManufacturing {

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final PreciseManufacturingRegistrate REGISTRATE = PreciseManufacturingRegistrate.create(Reference.MOD_ID);

    public PreciseManufacturing(IEventBus eventBus) {
        registerEntries(eventBus);

        // Register the setup method for modloading
        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);
    }

    private void registerEntries(final IEventBus eventBus) {

        PrmaBlocks.register();
        PrmaBlockEntities.register(eventBus);
        PrmaContainers.register(eventBus);
        PrmaItems.register();
        PrmaFluids.register();
        PrmaTags.register();
        PrmaRecipes.register(eventBus);
        PrmaCreativeModTabs.register(eventBus);
        PrmaRecipeTypes.register(eventBus);

        REGISTRATE.registerEventListeners(eventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Some preinit code
        LOGGER.info("HELLO FROM PREINIT");
//        event.enqueueWork(PacketHandler::register);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Some example code to dispatch IMC to another mod
//        InterModComms.sendTo("prma", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event) {
        // Some example code to receive and process InterModComms from other mods
//        LOGGER.info("Got IMC {}", event.getIMCStream().
//                map(m->m.messageSupplier().get()).
//                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @EventBusSubscriber(modid = com.minbaka.chnhcore.CHNHCore.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(final FMLClientSetupEvent event) {
//            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DECOMPONENTALIZER.get(), RenderType.translucent());
//            event.enqueueWork(() -> MenuScreens.register(PrmaContainers.DECOMPONENTALIZER_CONTAINER_MENU.get(), DecomponentalizerScreen::new));
        }
    }
}
