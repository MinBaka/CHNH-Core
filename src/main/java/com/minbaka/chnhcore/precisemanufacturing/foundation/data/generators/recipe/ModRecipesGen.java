package com.minbaka.chnhcore.precisemanufacturing.foundation.data.generators.recipe;

import com.minbaka.chnhcore.precisemanufacturing.PreciseManufacturing;
import com.minbaka.chnhcore.precisemanufacturing.foundation.PrmaFluids;
import com.minbaka.chnhcore.precisemanufacturing.foundation.PrmaItems;
import com.minbaka.chnhcore.precisemanufacturing.foundation.PrmaTags;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.complex.bridge.TaczAPIBridge;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.complex.content.item.collection.StandardCartridgeComponents;
//import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.complex.content.processing.casting_basin.recipe.CastingRecipe;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.simple.item.SimpleAmmo;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.simple.item.SimpleCartridge;
import com.minbaka.chnhcore.precisemanufacturing.foundation.utility.ResourceHelper;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModRecipesGen {
    private static List<RecipeBuilder> recipeBuilders = new ArrayList<>();
    private static List<ProcessingRecipeBuilder<?, ?, ?>> createCompatRecipeBuilders = new ArrayList<>();
    private static List<SequencedAssemblyRecipeBuilder> sequencedAssemblyRecipeBuilders = new ArrayList<>();
    private static List<SimpleCartridge> simpleCartridges = new ArrayList<>();
    private static List<SimpleAmmo> simpleAmmos = new ArrayList<>();

    public static void register(RecipeOutput pFinishedRecipeConsumer){
        registerCreateRecipes();
        registerVanillaRecipes();
        PrmaItems.ALL_CARTRIDGE_COMPONENTS.forEach(StandardCartridgeComponents::registerRecipes);
        simpleCartridges.forEach(SimpleCartridge::registerStandardRecipes);
        simpleAmmos.forEach(SimpleAmmo::registerRecipes);

        PreciseManufacturing.LOGGER.debug("assemblies {}", sequencedAssemblyRecipeBuilders.size());

        recipeBuilders.forEach(i -> i.save(pFinishedRecipeConsumer));
        createCompatRecipeBuilders.forEach(i -> i.build(pFinishedRecipeConsumer));
        sequencedAssemblyRecipeBuilders.forEach(i -> i.build(pFinishedRecipeConsumer));
    }

    public static void registerCreateRecipes() {
//        registerMillingRecipes();
//        registerCrushingRecipes();
//        registerCuttingRecipes();
//        registerEmptyingRecipes();
//        registerFillingRecipes();
//        registerMixingRecipes();
//        registerCastingRecipes();
    }


    public static void registerVanillaRecipes() {
        // Craft Blank Blueprint from Paper and Dye
//        add(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PrmaItems.BLANK_BLUEPRINT.get(), 3)
//                .requires(Items.PAPER)
//                .requires(Items.WHITE_DYE)
//                .requires(Items.BLUE_DYE)
//                .unlockedBy(RegistrateRecipeProvider.getHasName(Items.PAPER), RegistrateRecipeProvider.has(Items.PAPER)));
    }

    public static void addCreateRecipe(ProcessingRecipeBuilder<?, ?, ?> generatedRecipe){
        createCompatRecipeBuilders.add(generatedRecipe);
    }

    public static void addSequencedAssemblyRecipe(SequencedAssemblyRecipeBuilder generatedRecipe){
        sequencedAssemblyRecipeBuilders.add(generatedRecipe);
    }

    public static void addSimpleCartridge(SimpleCartridge simpleCartridge){
        simpleCartridges.add(simpleCartridge);
    }

    public static void addSimpleAmmo(SimpleAmmo simpleAmmo){
        simpleAmmos.add(simpleAmmo);
    }

    public static void add(RecipeBuilder builder){
        recipeBuilders.add(builder);
    }
}
