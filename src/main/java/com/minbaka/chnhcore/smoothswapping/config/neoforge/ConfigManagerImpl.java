package com.minbaka.chnhcore.smoothswapping.config.neoforge;

import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ConfigManagerImpl {

    public static Path getConfigPath(){
        return FMLPaths.CONFIGDIR.get();
    }

}
