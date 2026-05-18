package com.minbaka.chnhcore.smoothswapping.config;

import com.minbaka.chnhcore.smoothswapping.SmoothSwapping;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static Config CONFIG = new Config();
    private static File getConfigFile() {
        return new File(Minecraft.getInstance().gameDirectory, "config/chnh-smoothswapping.json");
    }

    public static void initializeConfig() {
        File file = getConfigFile();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                CONFIG = SmoothSwapping.GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                SmoothSwapping.LOGGER.error("Failed to read smoothswapping config", e);
            }
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        File file = getConfigFile();
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            SmoothSwapping.GSON.toJson(CONFIG, writer);
        } catch (IOException e) {
            SmoothSwapping.LOGGER.error("Failed to write smoothswapping config", e);
        }
    }

    public static Config getConfig() {
        if (CONFIG == null) CONFIG = new Config();
        return CONFIG;
    }
}