package com.minbaka.chnh.core.init;

import com.minbaka.chnh.core.CHNHCore;
import com.minbaka.chnh.core.item.AmmoBlueprintItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    // 所有物品都注册到 chnh_core 命名空间下
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, CHNHCore.MODID);

    // 子弹蓝图（带动态名称）
    public static final Supplier<Item> AMMO_BLUEPRINT = ITEMS.register("ammo_blueprint",
            () -> new AmmoBlueprintItem(new Item.Properties()));

    // 弹壳
    public static final Supplier<Item> CASING = ITEMS.register("casing",
            Item::new);

    // 装药
    public static final Supplier<Item> GUNPOWDER_CHARGE = ITEMS.register("gunpowder_charge",
            Item::new);

    // 弹头
    public static final Supplier<Item> BULLET_HEAD = ITEMS.register("bullet_head",
            Item::new);

    // 如果你要加更多类型，照着上面一行一个往下写即可
}