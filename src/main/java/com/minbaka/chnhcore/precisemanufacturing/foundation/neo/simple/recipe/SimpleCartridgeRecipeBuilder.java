package com.minbaka.chnhcore.precisemanufacturing.foundation.neo.simple.recipe;

import com.minbaka.chnhcore.precisemanufacturing.PreciseManufacturing;
import com.minbaka.chnhcore.precisemanufacturing.foundation.PrmaItems;
import com.minbaka.chnhcore.precisemanufacturing.foundation.data.generators.recipe.ModRecipesGen;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.complex.content.item.type.standard.AmmoCasingType;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.complex.content.item.type.standard.AmmoMaterialType;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.simple.content.type.standard.SimpleAmmoGunpowderAmountStandard;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.simple.item.SimpleCartridge;
import com.minbaka.chnhcore.precisemanufacturing.lib.Reference;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

// Simple Cartridge Recipe Builder builds the recipes from casing to gunpowder_cartridges.
public class SimpleCartridgeRecipeBuilder {
    protected final SequencedAssemblyRecipeBuilder builder;
    protected final SimpleCartridge cartridge;
    protected SimpleCartridgeRecipeBuilder(String namespaceId, String recipeName, SimpleCartridge resultingCartridge) {
        this.cartridge = resultingCartridge;
        this.builder = new SequencedAssemblyRecipeBuilder(ResourceLocation.fromNamespaceAndPath(namespaceId, String.format("simple/cartridge/%s", recipeName)))
                .require(cartridge.getBaseCasing().get())
                .transitionTo(resultingCartridge.getTransition().get())
                .loops(1)
                .addOutput(resultingCartridge.getItem().get(), 1);
    }

    protected SimpleCartridgeRecipeBuilder(String namespaceId, String recipeName, SimpleCartridge resultingCartridge, ItemLike baseItem){
        this.cartridge = resultingCartridge;
        this.builder = new SequencedAssemblyRecipeBuilder(ResourceLocation.fromNamespaceAndPath(namespaceId, String.format("simple/cartridge/%s", recipeName)))
                .require(baseItem)
                .transitionTo(resultingCartridge.getTransition().get())
                .loops(1)
                .addOutput(resultingCartridge.getItem().get(), 1);
    }

    public static SimpleCartridgeRecipeBuilder create(SimpleCartridge resultingCartridge) {
        return new SimpleCartridgeRecipeBuilder(Reference.MOD_ID, resultingCartridge.getCartridgeName(), resultingCartridge);
    }

    public static SimpleCartridgeRecipeBuilder createWithCustomBase(SimpleCartridge resultingCartridge, ItemLike itemLike) {
        return new SimpleCartridgeRecipeBuilder(Reference.MOD_ID, resultingCartridge.getCartridgeName(), resultingCartridge, itemLike);
    }

    public SimpleCartridgeRecipeBuilder deployerApply(ItemLike item, int times) {
        for (int i = 0; i < times; i++){
            builder.addStep(DeployerApplicationRecipe::new, d -> d.require(item));
        }
        return this;
    }

    public SimpleCartridgeRecipeBuilder deployerApply(ItemLike item) {
        return deployerApply(item, 1);
    }

    public SimpleCartridgeRecipeBuilder pressingApply(int times) {
        for (int i = 0; i < times; i++){
            builder.addStep(PressingRecipe::new, p -> p);
        }
        return this;
    }

    public SimpleCartridgeRecipeBuilder pressingApply() {
        return pressingApply(1);
    }

    public SimpleCartridgeRecipeBuilder applyPrimer() {
        return deployerApply(PrmaItems.Ammo.CARTRIDGE_PRIMER.get());
    }

    public SimpleCartridgeRecipeBuilder applyGunpowder(int amount) {
        return deployerApply(Items.GUNPOWDER, amount);
    }

    public SimpleCartridgeRecipeBuilder applyGunpowder() {
        return applyGunpowder(1);
    }

    public SimpleCartridgeRecipeBuilder applyShotgunBearings(int times) {
        return deployerApply(PrmaItems.Ammo.SHOTGUN_BEARING.get(), times);
    }

    public SimpleCartridgeRecipeBuilder applyShotgunShellBase() {
        return deployerApply(PrmaItems.Ammo.SHOTGUN_SHELL_BASE.get());
    }

    public SimpleCartridgeRecipeBuilder applyShotgunShell() {
        return deployerApply(PrmaItems.Ammo.SHOTGUN_SHELL.get());
    }



    public final SimpleCartridgeRecipeBuilder build(){
        ModRecipesGen.addSequencedAssemblyRecipe(builder);
        return this;
    }
}
