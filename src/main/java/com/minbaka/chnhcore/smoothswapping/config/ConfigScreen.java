package com.minbaka.chnhcore.smoothswapping.config;

import com.mojang.serialization.Codec;
import com.minbaka.chnhcore.smoothswapping.Vec2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatChatFormatting;

import java.util.List;

public class ConfigScreen extends Screen {

    CatmullRomWidget catmullRomWidget;
    InventoryWidget inventoryWidget;
    Config config;
    OptionInstance<Integer> animationSpeedOption;
    OptionInstance<Boolean> toggleOption;
    private final int oldAnimationSpeed;
    Screen parentScreen;
    List<Vec2> oldPoints;

    public ConfigScreen(Screen parent) {
        super(Text.translatable("smoothswapping.config.menu"));
        config = ConfigManager.getConfig();
        this.toggleOption = OptionInstance.ofBoolean("smoothswapping.config.toggle",
                OptionInstance.emptyTooltip(),
                (optionText, value) -> {
                    if (value)
                        return Text.translatable("smoothswapping.config.toggle.on").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));
                    return Text.translatable("smoothswapping.config.toggle.off").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
                },
                config.getToggleMod(),
                (value) -> config.setToggleMod(value)
        );

        this.animationSpeedOption = new OptionInstance<>("smoothswapping.config.option.animationspeed",
                OptionInstance.emptyTooltip(),
                (optionText, value) -> Text.translatable("smoothswapping.config.option.animationspeed.speed").append(": ").append(Text.literal(value + "%")),
                (new OptionInstance.ValidatingIntSliderCallbacks(1, 50)).withModifier(
                        (value) -> value * 10,
                        (value) -> value / 10),
                Codec.intRange(10, 500),
                config.getAnimationSpeed(),
                (value) -> config.setAnimationSpeed(value));
        this.oldAnimationSpeed = config.getAnimationSpeed();
        this.parentScreen = parent;
        this.oldPoints = config.getCurvePoints();
    }

    @Override
    protected void init() {
        this.addDrawableChild(toggleOption.createWidget(Minecraft.getInstance().options, this.width / 2 - 94, height / 5 - 20, 188));
        this.addDrawableChild(animationSpeedOption.createWidget(Minecraft.getInstance().options, this.width / 2 - 94, this.height / 5 + 5, 188));
        this.catmullRomWidget = new CatmullRomWidget(this.width / 2 - 84 - 10, this.height / 3, 64, 64, 12, 4, 4, config.getCurvePoints());
        this.inventoryWidget = new InventoryWidget(this.width / 2 + 10, this.height / 3, 3, 4, Text.translatable("smoothswapping.config.testinventory"));
        this.addDrawableChild(this.catmullRomWidget);
        this.addDrawableChild(this.inventoryWidget);

        Button resetButton = Button.builder(Text.translatable("smoothswapping.config.option.animationspeed.reset"), button ->
                catmullRomWidget.reset()).dimensions(this.width / 2 - 84 - 10, this.height / 3 + 86 + 4, 88, 20).build();
        this.addDrawableChild(resetButton);

        Button saveButton = Button.builder(Text.translatable("smoothswapping.config.save"), button -> {
            ConfigManager.save();
            Minecraft.getInstance().setScreen(parentScreen);
        }).dimensions(this.width / 2 + 10, this.height - 30, 88, 20).build();
        this.addDrawableChild(saveButton);

        Button exitButton = Button.builder(Text.translatable("smoothswapping.config.exit"), button -> this.close())
                .dimensions(this.width / 2 - 84 - 10, this.height - 30, 88, 20).build();
        this.addDrawableChild(exitButton);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, this.width / 2, 10, 0xFFFFFFFF);
        config.setCurvePoints(catmullRomWidget.getPoints());
    }

    @Override
    public void close() {
        config.setCurvePoints(oldPoints);
        config.setAnimationSpeed(oldAnimationSpeed);
        Minecraft.getInstance().setScreen(parentScreen);
    }
}
