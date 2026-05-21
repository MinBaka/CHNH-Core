package com.minbaka.chnhcore;

import cc.sighs.oelib.data.api.ExpressionFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class SkpFunctions {

    @ExpressionFunction(description = "Check sprint", category = "mod")
    public static boolean isSprinting() {
        Player player = Minecraft.getInstance().player;
        return player != null && player.isSprinting();
    }

    @ExpressionFunction(description = "Check crouch", category = "mod")
    public static boolean isCrouching() {
        Player player = Minecraft.getInstance().player;
        return player != null && player.isCrouching();
    }

    @ExpressionFunction(description = "Check fall", category = "mod")
    public static float fallDistance() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return 0f;
        return player.fallDistance;
    }

    @ExpressionFunction(description = "Check ground", category = "mod")
    public static boolean onGround() {
        Player player = Minecraft.getInstance().player;
        return player != null && player.onGround();
    }

    @ExpressionFunction(description = "Check gun", category = "mod")
    public static boolean holdingTaczGun() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return false;
        String id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem()).toString();
        return id.startsWith("tacz:");
    }
}
