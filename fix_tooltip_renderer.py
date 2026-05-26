import os, glob

def replace_in_file(path):
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    
    replacements = {
        "ForgeConfigSpec": "ModConfigSpec",
        "consumer.vertex(": "consumer.addVertex(",
        ".color(": ".setColor(",
        ".endVertex()": ""
    }
    
    changed = False
    for old, new in replacements.items():
        if old in content:
            content = content.replace(old, new)
            changed = True
            
    if changed:
        with open(path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Updated {path}")

for f in glob.glob("src/main/java/com/minbaka/chnhcore/colortooltips/**/*.java", recursive=True):
    replace_in_file(f)
