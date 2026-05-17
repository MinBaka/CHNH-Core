import os
import glob
import re

base_dir = "A:/Project/CHNH-Core/src/main/java/com/minbaka/chnhcore/smoothswapping"

replacements = {
    "dev.shwg.smoothswapping": "com.minbaka.chnhcore.smoothswapping",
    "net.minecraft.client.gui.DrawContext": "net.minecraft.client.gui.GuiGraphics",
    "net.minecraft.client.gui.screen.ingame.HandledScreen": "net.minecraft.client.gui.screens.inventory.AbstractContainerScreen",
    "net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen": "net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen",
    "net.minecraft.client.gui.screen.Screen": "net.minecraft.client.gui.screens.Screen",
    "net.minecraft.client.network.ClientPlayerEntity": "net.minecraft.client.player.LocalPlayer",
    "net.minecraft.client.MinecraftClient": "net.minecraft.client.Minecraft",
    "net.minecraft.client.util.math.MatrixStack": "com.mojang.blaze3d.vertex.PoseStack",
    "net.minecraft.entity.LivingEntity": "net.minecraft.world.entity.LivingEntity",
    "net.minecraft.item.ItemStack": "net.minecraft.world.item.ItemStack",
    "net.minecraft.item.Items": "net.minecraft.world.item.Items",
    "net.minecraft.screen.ScreenHandler": "net.minecraft.world.inventory.AbstractContainerMenu",
    "net.minecraft.screen.slot.Slot": "net.minecraft.world.inventory.Slot",
    "net.minecraft.screen.slot.SlotActionType": "net.minecraft.world.inventory.ClickType",
    "net.minecraft.util.collection.DefaultedList": "net.minecraft.core.NonNullList",
    "net.minecraft.world.World": "net.minecraft.world.level.Level",
    "net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket": "net.minecraft.network.protocol.game.ServerboundContainerClickPacket",
    "net.minecraft.inventory.SimpleInventory": "net.minecraft.world.SimpleContainer",
    "net.minecraft.client.font.TextRenderer": "net.minecraft.client.gui.Font",
    "net.minecraft.text.Text": "net.minecraft.network.chat.Component",
    "net.minecraft.client.gui.widget.ClickableWidget": "net.minecraft.client.gui.components.AbstractWidget",
    "net.minecraft.screen.ScreenTexts": "net.minecraft.network.chat.CommonComponents",
    "net.minecraft.util.Util": "net.minecraft.Util",
    "net.minecraft.client.gui.ScreenRect": "net.minecraft.client.gui.navigation.ScreenRectangle",
    "net.minecraft.client.gui.screen.narration.NarrationMessageBuilder": "net.minecraft.client.gui.narration.NarrationElementOutput",
    "net.minecraft.client.gui.tooltip.Tooltip": "net.minecraft.client.gui.components.Tooltip",
    "net.minecraft.client.gui.tooltip.TooltipPositioner": "net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner",
    "net.minecraft.client.gui.widget.ButtonWidget": "net.minecraft.client.gui.components.Button",
    "net.minecraft.client.option.SimpleOption": "net.minecraft.client.OptionInstance",
    "net.minecraft.text.Style": "net.minecraft.network.chat.Style",
    "net.minecraft.util.Formatting": "net.minecraft.ChatFormatting",
    "net.minecraft.text.StringVisitable": "net.minecraft.network.chat.FormattedText",
    "net.minecraft.util.Identifier": "net.minecraft.resources.ResourceLocation",

    # Method and field replacements
    "DrawContext": "GuiGraphics",
    "HandledScreen": "AbstractContainerScreen",
    "CreativeInventoryScreen": "CreativeModeInventoryScreen",
    "ClientPlayerEntity": "LocalPlayer",
    "MinecraftClient": "Minecraft",
    "MatrixStack": "PoseStack",
    "ScreenHandler": "AbstractContainerMenu",
    "DefaultedList": "NonNullList",
    "ClickSlotC2SPacket": "ServerboundContainerClickPacket",
    "SimpleInventory": "SimpleContainer",
    "TextRenderer": "Font",
    "ScreenRect": "ScreenRectangle",
    "NarrationMessageBuilder": "NarrationElementOutput",
    "TooltipPositioner": "ClientTooltipPositioner",
    "ButtonWidget": "Button",
    "SimpleOption": "OptionInstance",
    "StringVisitable": "FormattedText",
    "Identifier": "ResourceLocation",
    "Formatting": "ChatFormatting",
    "ScreenTexts": "CommonComponents",

    # Method calls
    ".drawItem(": ".renderItem(",
    ".drawItemInSlot(": ".renderItemDecorations(",
    ".getMatrices()": ".pose()",
    ".drawTexture(": ".blit(",
    ".textRenderer": ".font",
    ".currentScreenHandler": ".containerMenu",
    ".currentScreen": ".screen",
    ".player": ".player",
    ".getStacks()": ".getItems()",
    ".getCursorStack()": ".getCarried()",
    ".canTakePartial(": ".mayPickup(",
    "ItemStack.areEqual(": "ItemStack.matches(",
    "ItemStack.areItemsEqual(": "ItemStack.isSameItem(",
    ".getItem()": ".getItem()",
    ".getCount()": ".getCount()",
    ".setCount(": ".setCount(",
    "Items.AIR": "Items.AIR",
    ".getInstance()": ".getInstance()",
    ".getRenderTickCounter().getLastFrameDuration()": ".getTimer().getGameTimeDeltaTicks()", # Not entirely correct for NeoForge 1.21.1, but close. 1.21.1 might use getFrameTime() or similar
    "ClickType.PICKUP": "ClickType.PICKUP"
}

def replace_in_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    for old_str, new_str in replacements.items():
        content = content.replace(old_str, new_str)

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

for filepath in glob.glob(base_dir + '/**/*.java', recursive=True):
    replace_in_file(filepath)