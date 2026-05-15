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
import net.createmod.catnip.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public class SimpleCartridgeBuilder {
    public static SimpleCartridge create(AmmoCasingType baseCasingType, AmmoMaterialType baseCasingMaterialType, SimpleAmmoGunpowderAmountStandard amountStandard, String namespaceId) {
        return new SimpleCartridge(baseCasingType, baseCasingMaterialType, amountStandard);
    }

    public static SimpleCartridge create(AmmoCasingType baseCasingType, AmmoMaterialType baseCasingMaterialType, SimpleAmmoGunpowderAmountStandard amountStandard) {
        return create(baseCasingType, baseCasingMaterialType, amountStandard, Reference.MOD_ID);
    }
}
