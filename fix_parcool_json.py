import json

filepath = "A:/Project/CHNH-Core/src/main/resources/data/smartkeyprompts/key_prompts/parcool.json"
with open(filepath, "r", encoding="utf-8") as f:
    data = json.load(f)

# The exception log says dev.latvian.mods.rhino.EcmaError: ReferenceError: "SKP$CommonUtils" is not defined.
# This means the KJS script was still running and crashing, maybe because you didn't delete it?
# Let's write code to verify if kjs folder exists.
