package com.minbaka.chnhcore.smoothswapping.config;

public class Config {
    private boolean toggleMod = true;
    private double animationSpeed = 1.0;

    public boolean getToggleMod() {
        return toggleMod;
    }

    public double getAnimationSpeedFormatted() {
        return animationSpeed;
    }

    public void setToggleMod(boolean toggleMod) {
        this.toggleMod = toggleMod;
    }

    public void setAnimationSpeed(double animationSpeed) {
        this.animationSpeed = animationSpeed;
    }
}