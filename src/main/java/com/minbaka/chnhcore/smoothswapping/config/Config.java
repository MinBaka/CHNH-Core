package com.minbaka.chnhcore.smoothswapping.config;

public class Config {
    public boolean getToggleMod() {
        return true;
    }

    public double getAnimationSpeedFormatted() {
        return 1.0;
    }

    // Default Catmull-Rom splines, mapped to simple values or we can just simplify CatmullRomWidget progress check
    // Wait, the progress check is used in GuiGraphicsMixin
}