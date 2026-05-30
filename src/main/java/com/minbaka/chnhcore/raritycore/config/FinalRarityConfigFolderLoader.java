package com.minbaka.chnhcore.raritycore.config;



import com.minbaka.chnhcore.raritycore.util.ConfigLoaderUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * FinalRarityConfig文件夹加载器
 * 负责加载config/raritycore/FinalRarityConfig文件夹中的所有JSON配置文件
 */
public class FinalRarityConfigFolderLoader {


    /**
     * 加载FinalRarityConfig文件夹中的所有JSON文件
     * 文件按字母顺序加载,后加载的会覆盖先加载的同名物品配置
     */
    public static void loadFinalRarityConfigFolder() {
        Path configFolder = ConfigManager.getFinalRarityConfigFolderPath();
        
        // 确保目录存在
        try {
            Files.createDirectories(configFolder);
        } catch (IOException e) {
            org.slf4j.LoggerFactory.getLogger("RarityCore").error("Cannot create FinalRarityConfig directory: {}", configFolder, e);
            return;
        }

        // 检查目录是否存在且是目录
        if (!Files.exists(configFolder) || !Files.isDirectory(configFolder)) {
            org.slf4j.LoggerFactory.getLogger("RarityCore").info("FinalRarityConfig folder not found, skipping: {}", configFolder);
            return;
        }

        try {
            // 获取所有JSON文件并按名称排序
            Path[] jsonFiles = Files.list(configFolder)
                .filter(path -> path.toString().endsWith(".json"))
                .sorted()
                .toArray(Path[]::new);
            
            org.slf4j.LoggerFactory.getLogger("RarityCore").info("Found {} JSON files in FinalRarityConfig folder", jsonFiles.length);
            
            // 按顺序加载所有JSON文件
            for (Path jsonFile : jsonFiles) {
                org.slf4j.LoggerFactory.getLogger("RarityCore").info("Loading FinalRarityConfig file: {}", jsonFile.getFileName());
                loadRarityDataFromFile(jsonFile);
            }
            
        } catch (IOException e) {
            org.slf4j.LoggerFactory.getLogger("RarityCore").error("Error reading FinalRarityConfig folder: {}", configFolder, e);
        }
    }

    /**
     * 从单个JSON文件加载稀有度数据
     */
    private static void loadRarityDataFromFile(Path configFile) {
        ConfigLoaderUtils.loadJsonConfigFileWithBatch(configFile, configFile.getFileName().toString(), true);
    }
}