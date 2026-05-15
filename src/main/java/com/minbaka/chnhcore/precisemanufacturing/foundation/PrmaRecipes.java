package com.minbaka.chnhcore.precisemanufacturing.foundation;

import com.minbaka.chnhcore.precisemanufacturing.lib.Reference;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

public class PrmaRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

//    public static final RegistryObject<RecipeSerializer<DecomponentalizingRecipe>> DECOMPONENTALIZING_SERIALIZER = SERIALIZERS.register(DecomponentalizingRecipe.Type.ID, () -> DecomponentalizingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
