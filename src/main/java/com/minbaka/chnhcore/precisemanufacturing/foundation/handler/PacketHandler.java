package com.minbaka.chnhcore.precisemanufacturing.foundation.handler;

import com.minbaka.chnhcore.precisemanufacturing.util.annotations.ClientSide;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PacketHandler {

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("chnhcore_prma");
        // Register packets here
    }

    @ClientSide
    public static <MSG extends CustomPacketPayload> void sendToServer(final MSG message) {
        PacketDistributor.sendToServer(message);
    }

    @ClientSide
    public static <MSG extends CustomPacketPayload> void sendToPlayer(MSG message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

    @ClientSide
    public static <MSG extends CustomPacketPayload> void sendToAllAround(final MSG message) {
        PacketDistributor.sendToAllPlayers(message);
    }
}
