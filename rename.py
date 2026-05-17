import os
import glob

def replace_in_file(filepath, old_str, new_str):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    content = content.replace(old_str, new_str)
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

base_dir = "A:/Project/CHNH-Core/src/main/java/com/minbaka/chnhcore/smoothswapping"
for filepath in glob.glob(base_dir + '/**/*.java', recursive=True):
    replace_in_file(filepath, "dev.shwg.smoothswapping", "com.minbaka.chnhcore.smoothswapping")
