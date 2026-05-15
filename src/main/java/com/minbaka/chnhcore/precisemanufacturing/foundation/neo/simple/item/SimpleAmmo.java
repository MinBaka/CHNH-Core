package com.minbaka.chnhcore.precisemanufacturing.foundation.neo.simple.item;

import com.minbaka.chnhcore.precisemanufacturing.PreciseManufacturing;
import com.minbaka.chnhcore.precisemanufacturing.foundation.PrmaItems;
import com.minbaka.chnhcore.precisemanufacturing.foundation.PrmaTags;
import com.minbaka.chnhcore.precisemanufacturing.foundation.data.generators.recipe.ModRecipesGen;
import com.minbaka.chnhcore.precisemanufacturing.foundation.data.providers.ModItemModelProvider;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.complex.bridge.TaczAPIBridge;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.complex.content.item.type.standard.AmmoHeadType;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.complex.content.item.type.standard.AmmoMaterialType;
import com.minbaka.chnhcore.precisemanufacturing.foundation.utility.ResourceHelper;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.tacz.guns.api.item.builder.AmmoItemBuilder;
import com.tacz.guns.init.ModItems;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

// SimpleAmmo registers the recipes from the gunpowder_cartridges to the actual bullet ammo.
public class SimpleAmmo {
    protected final SimpleCartridge cartridge;
    protected final String ammoId;
    protected final AmmoHeadType headType;
    protected final AmmoMaterialType headMaterial;
    protected final ItemEntry<Item> bulletHead, transitionItem;
    protected final int resultAmount;

    public SimpleAmmo(SimpleCartridge cartridge, AmmoHeadType headType, AmmoMaterialType headMaterial, String ammoId, int resultAmount) {
        PreciseManufacturing.LOGGER.debug("cartridge null? {}", cartridge.item == null);
        this.cartridge = cartridge;
        this.ammoId = ammoId;
        this.headType = headType;
        this.headMaterial = headMaterial;
        this.resultAmount = resultAmount;

        this.bulletHead = PrmaItems.addToMaterials(PreciseManufacturing.REGISTRATE.item(String.format("%s_%s", ammoId, "head"), Item::new)
                .model(ModItemModelProvider.genericItemModel(true, "simple", "ammo", "head", "_"))
                .tag(PrmaTags.ItemTag.MATERIALS.tag, PrmaTags.ItemTag.AMMO_HEADS.tag, AllTags.AllItemTags.UPRIGHT_ON_BELT.tag).register());
        this.transitionItem = PreciseManufacturing.REGISTRATE.item(String.format("%s_%s", ammoId, "transition"), Item::new)
                .model(ModItemModelProvider.genericItemModel(true, "simple", "ammo", "ammo_transition", "_"))
                .tag(PrmaTags.ItemTag.AMMO_WASTE.tag, AllTags.AllItemTags.UPRIGHT_ON_BELT.tag).register();

        ModRecipesGen.addSimpleAmmo(this);
    }

    public static SimpleAmmo create(SimpleCartridge cartridge, AmmoHeadType headType, AmmoMaterialType headMaterial, String ammoId, int resultAmount){
        return new SimpleAmmo(cartridge, headType, headMaterial, ammoId, resultAmount);
    }

    public void registerRecipes() {
    }
}
