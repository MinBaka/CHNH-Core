package com.minbaka.chnhcore.smoothswapping.mixin;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleContainer.class)
public interface SimpleContainerAccessor {

    @Accessor("items")
    NonNullList<ItemStack> getStacks();

}
