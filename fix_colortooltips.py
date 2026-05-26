import os, glob

def replace_in_file(path, replacements):
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    
    changed = False
    for old, new in replacements.items():
        if old in content:
            content = content.replace(old, new)
            changed = True
            
    if changed:
        with open(path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Updated {path}")

# ColorTooltips.java
replace_in_file("src/main/java/com/minbaka/chnhcore/colortooltips/ColorTooltips.java", {
    "import net.neoforged.neoforge.common.MinecraftForge;": "",
    "import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;": "",
    "MinecraftForge.EVENT_BUS.register(this);": "net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(this);",
    "MinecraftForge.EVENT_BUS.register(TooltipEventHandler.class);": "net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(TooltipEventHandler.class);",
    "MinecraftForge.EVENT_BUS.register(TooltipLockManager.class);": "net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(TooltipLockManager.class);",
    "MinecraftForge.EVENT_BUS.addListener(": "net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(",
    "IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();": "",
    "public ColorTooltips() {": "public ColorTooltips(IEventBus modEventBus, net.neoforged.fml.ModContainer modContainer) {",
    "ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);": "modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, Config.SPEC);",
    "bus = Mod.EventBusSubscriber.Bus.MOD, ": "",
    "@Mod(ColorTooltips.MODID)": ""
})

# Config.java
replace_in_file("src/main/java/com/minbaka/chnhcore/colortooltips/Config.java", {
    "bus = Mod.EventBusSubscriber.Bus.MOD": ""
})

# TooltipAnimationSystem.java
replace_in_file("src/main/java/com/minbaka/chnhcore/colortooltips/animation/TooltipAnimationSystem.java", {
    "ItemStack.isSameItemSameTags": "ItemStack.isSameItemSameComponents"
})

# TooltipLockManager.java
replace_in_file("src/main/java/com/minbaka/chnhcore/colortooltips/animation/TooltipLockManager.java", {
    "event.getScrollDelta()": "event.getScrollDeltaY()"
})

# GuiGraphicsMixin.java
replace_in_file("src/main/java/com/minbaka/chnhcore/colortooltips/mixin/GuiGraphicsMixin.java", {
    "stack.hasTag() &&": "stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA) &&",
    "stack.getTag().contains(SEARCH_TIME_REMAINING)": "stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA).contains(SEARCH_TIME_REMAINING)"
})

# ColorHeaderComponent.java
replace_in_file("src/main/java/com/minbaka/chnhcore/colortooltips/tooltip/ColorHeaderComponent.java", {
    "context.pose().mulPoseMatrix(new org.joml.Matrix4f().scaling(1.0f, -1.0f, 1.0f));": "context.pose().scale(1.0f, -1.0f, 1.0f);"
})
