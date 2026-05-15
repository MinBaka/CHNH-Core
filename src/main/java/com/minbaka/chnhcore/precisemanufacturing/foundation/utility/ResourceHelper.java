package com.minbaka.chnhcore.precisemanufacturing.foundation.utility;

import com.minbaka.chnhcore.precisemanufacturing.lib.Reference;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.ConcurrentHashMap;

public class ResourceHelper {
    private static ConcurrentHashMap<String, ResourceLocation> table = new ConcurrentHashMap<>();

    public static ResourceLocation find(String filePath){
        if(table.containsKey(filePath))
            return table.get(filePath);

        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, filePath);
        table.put(filePath, loc);
        return loc;
    }

    public static ResourceLocation find(String modid, String filePath){
        if(table.containsKey(modid + ":" + filePath))
            return table.get(filePath);

        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(modid, filePath);
        table.put(filePath, loc);
        return loc;
    }

    public static ResourceLocation findAndRegisterItemModel(String filePath){
        ResourceLocation result = find(filePath);

        return result;
    }
}
