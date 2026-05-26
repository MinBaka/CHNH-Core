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

replace_in_file("src/main/java/com/minbaka/chnhcore/colortooltips/ColorTooltips.java", {
    "@Mod.EventBusSubscriber": "@EventBusSubscriber",
    "import net.neoforged.fml.common.Mod;": "import net.neoforged.fml.common.Mod;\nimport net.neoforged.fml.common.EventBusSubscriber;",
    "IEventBus modEventBus = modEventBus;": ""
})

replace_in_file("src/main/java/com/minbaka/chnhcore/colortooltips/Config.java", {
    "@Mod.EventBusSubscriber": "@EventBusSubscriber",
    "import net.neoforged.fml.common.Mod;": "import net.neoforged.fml.common.Mod;\nimport net.neoforged.fml.common.EventBusSubscriber;"
})
