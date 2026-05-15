package com.minbaka.chnhcore.precisemanufacturing.foundation.data.builders.recipe;

import com.minbaka.chnhcore.precisemanufacturing.foundation.utility.ResourceHelper;
import com.minbaka.chnhcore.precisemanufacturing.lib.Reference;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.jetbrains.annotations.Nullable;

public class DecomponentalizingRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private final Ingredient ingredient;
    private final int processingTime;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public DecomponentalizingRecipeBuilder(ItemStack ingredient, ItemLike result, int processingTime) {
        this.ingredient = DataComponentIngredient.of(false, ingredient);
        this.result = result.asItem();
        this.processingTime = processingTime;
    }

    public DecomponentalizingRecipeBuilder(TagKey<Item> tag, ItemLike result, int processingTime) {
        this.ingredient = Ingredient.of(tag);
        this.result = result.asItem();
        this.processingTime = processingTime;
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, Criterion<?> pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(RecipeOutput pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        // Commenting out implementation as DecomponentalizingRecipe does not exist yet.
    }

    @Override
    public void save(RecipeOutput pFinishedRecipeConsumer) {
        this.save(pFinishedRecipeConsumer, ResourceHelper.find("decomponentalizing"));
    }
}