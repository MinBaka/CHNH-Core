import os

def fix_tooltip_renderer():
    path = "src/main/java/com/minbaka/chnhcore/colortooltips/util/TooltipRenderer.java"
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()

    # We need to map consumer.vertex(matrix, X, Y, Z).color(r, g, b, a).endVertex();
    # To consumer.addVertex(matrix, X, Y, Z).setColor((int)(r*255), (int)(g*255), (int)(b*255), (int)(a*255));
    # Using regex
    import re
    content = re.sub(r'consumer\.vertex\(([^)]+)\)\.color\(([^,]+),\s*([^,]+),\s*([^,]+),\s*([^)]+)\)\.endVertex\(\);',
                     r'consumer.addVertex(\1).setColor((int)(\2 * 255), (int)(\3 * 255), (int)(\4 * 255), (int)(\5 * 255));',
                     content)

    content = content.replace("consumer.vertex", "consumer.addVertex")
    content = content.replace(".color(", ".setColor(")

    with open(path, "w", encoding="utf-8") as f:
        f.write(content)

def fix_gui_graphics_mixin():
    path = "src/main/java/com/minbaka/chnhcore/colortooltips/mixin/GuiGraphicsMixin.java"
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()

    content = content.replace("stack.hasTag() &&", "stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA) &&")
    content = content.replace("stack.getTag().contains(SEARCH_TIME_REMAINING)", "stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA).contains(SEARCH_TIME_REMAINING)")

    with open(path, "w", encoding="utf-8") as f:
        f.write(content)

fix_tooltip_renderer()
fix_gui_graphics_mixin()
