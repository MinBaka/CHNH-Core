package com.minbaka.chnhcore.smoothswapping.config;

public class ConfigManager {
    private static final Config CONFIG = new Config();

    public static void initializeConfig() {
    }

    public static Config getConfig() {
        return CONFIG;
    }
}