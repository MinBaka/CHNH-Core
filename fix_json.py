import json

filepath = "A:/Project/CHNH-Core/src/main/resources/data/chnh_core/key_prompts/parcool.json"
with open(filepath, "r", encoding="utf-8") as f:
    data = json.load(f)

# Change rayTraceMiss to use pick
data["vars"]["rayTraceMiss"] = "player().pick(4.0, 0.0, false).getType().name() == 'MISS'"

with open(filepath, "w", encoding="utf-8") as f:
    json.dump(data, f, indent=2)
