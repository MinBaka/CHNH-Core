package com.minbaka.chnhcore.smoothswapping.mixin;

import com.minbaka.chnhcore.smoothswapping.ItemStackAccessor;
import com.minbaka.chnhcore.smoothswapping.SmoothSwapping;
import com.minbaka.chnhcore.smoothswapping.SwapUtil;
import com.minbaka.chnhcore.smoothswapping.config.CatmullRomWidget;
import com.minbaka.chnhcore.smoothswapping.config.Config;
import com.minbaka.chnhcore.smoothswapping.config.ConfigManager;
import com.minbaka.chnhcore.smoothswapping.config.ConfigScreen;
import com.minbaka.chnhcore.smoothswapping.swaps.InventorySwap;
import com.minbaka.chnhcore.smoothswapping.swaps.ItemToCursorInventorySwap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.minbaka.chnhcore.smoothswapping.SmoothSwapping.ASSUME_CURSOR_STACK_SLOT_INDEX;
import static com.minbaka.chnhcore.smoothswapping.SwapUtil.swapListIndexOf;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Final
    @Shadow
    private PoseStack matrices;
    @Final
    @Shadow
    private Minecraft client;

    @Unique
    private static boolean smooth_Swapping$isRendering = false;

    @Shadow
    public abstract void drawItem(ItemStack item, int x, int y);
    @Shadow public abstract void drawItemInSlot(Font textRenderer, ItemStack stack, int x, int y, @Nullable String countOverride);

    @Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At("HEAD"), cancellable = true)
    public void onItemDraw(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, int z, CallbackInfo cbi) {
        if (smooth_Swapping$isRendering) return;

        try {
            smooth_Swapping$isRendering = true;

            if (smooth_Swapping$isHotbar() && !(client.screen instanceof ConfigScreen)) return;
            if (((ItemStackAccessor) (Object) stack).smooth_Swapping$isSwapStack()) return;

            smooth_Swapping$doSwap(stack, x, y, cbi);
        } catch (Exception e) {
            SwapUtil.reset();
        } finally {
            smooth_Swapping$isRendering = false;
        }
    }

    @Unique
    private void smooth_Swapping$doSwap(ItemStack stack, int x, int y, CallbackInfo cbi) throws Error {
        int index = SwapUtil.getSlotIndex(stack);

        if (SmoothSwapping.swaps.containsKey(index)) {
            //Get all swaps for one slot
            List<InventorySwap> swapList = SmoothSwapping.swaps.get(index);

            boolean renderDestinationSlot = true;

            //render all swaps for one slot
            for (int i = 0; i < swapList.size(); i++) {
                InventorySwap swap = swapList.get(i);

                swap.setRenderDestinationSlot(swap.isChecked());

                if (!swap.renderDestinationSlot()) {
                    renderDestinationSlot = false;
                }

                //LOGGER.info("render i2i swap, stack hash: " + stack.hashCode());
                smooth_Swapping$renderSwap(swap, x, y, swap.getSwapItem());

                if (SwapUtil.hasArrived(swap)) {
                    SwapUtil.setRenderToTrue(swapList);
                    swapList.remove(swap);
                }
            }

            //whether the destination slot should be rendered
            if (renderDestinationSlot) {
                drawItem(stack.copy(), x, y);
            }
            if (swapList.isEmpty())
                SmoothSwapping.swaps.remove(index);

            cbi.cancel();
        } else if (SmoothSwapping.swaps.containsKey(ASSUME_CURSOR_STACK_SLOT_INDEX)) {
            List<InventorySwap> swapList = SmoothSwapping.swaps
                    .get(ASSUME_CURSOR_STACK_SLOT_INDEX)
                    .stream().filter(swap -> !((ItemToCursorInventorySwap) swap).isArrived())
                    .toList();

            if (!swapList.isEmpty()) {
                if (swapListIndexOf(swapList, (swap) -> ((ItemToCursorInventorySwap) swap).getCopiedStackHash() == stack.hashCode()) == -1) {
                    LocalPlayer player = Minecraft.getInstance().player;
                    AbstractContainerMenu handler = null;
                    if (player != null) handler = player.currentAbstractContainerMenu;
                    //LOGGER.info("cursor stack hash: " + handler.getCarried().hashCode());

                    for (InventorySwap inventorySwap : swapList) { // assign initial renders
                        ItemToCursorInventorySwap swap = (ItemToCursorInventorySwap) inventorySwap;

                        if (!swap.isStartedRender()) {
                            //LOGGER.info("i2c start render " + swap.getSwapItem() +  ", hash=" + swap.getSwapItem().hashCode());
                            swap.setStartedRender(true);
                        } else if (!swap.isArrived()) {
                            if (handler != null) {
                                NonNullList<ItemStack> inventoryStacks = handler.getItems();
                                //LOGGER.info("render stack: [" + stack +  ", " + stack.hashCode() + "], inventory stacks:" + sb);
                                //LOGGER.info("target stack hash: " + swap.getTargetStackHash() + ", cursor stack hash: " + handler.getCarried().hashCode());
                                if (swap.getTargetStackHash() == -1 || handler.getCarried().hashCode() == swap.getTargetStackHash()) {
                                    if (!inventoryStacks.contains(stack)) { // now rendering cursor stack from parent
                                        ItemStack copiedStack = swap.getSwapItem().copy();
                                        swap.setCopiedStackHash(copiedStack.hashCode());
                                        if (swap.getTargetStackHash() == -1)
                                            swap.setTargetStackHash(stack.hashCode());
                                        //LOGGER.info("i2c insert render on " + stack + " to render " + swap.getSwapItem() + ", hash=" + swap.getSwapItem().hashCode());
                                        smooth_Swapping$renderSwap(swap, x, y, copiedStack);

                                        if (SwapUtil.hasArrived(swap)) swap.setArrived(true);
                                    }
                                } else {
                                    swap.setArrived(true);
                                    //LOGGER.info("cursor stack has changed, enforcing to arrive");
                                }
                            }
                        }
                    }
                } /*else LOGGER.info("i2c real render: " + stack + ", hash=" + stack.hashCode());*/
            }

            if (swapList.stream().allMatch(swap -> ((ItemToCursorInventorySwap) swap).isArrived()))
                SmoothSwapping.swaps.remove(ASSUME_CURSOR_STACK_SLOT_INDEX);
        }
    }

    @Unique
    private void smooth_Swapping$renderSwap(InventorySwap swap, int x, int y, ItemStack copiedStack) {
        float lastFrameDuration = client.getTimer().getGameTimeDeltaTicks();
        Config config = ConfigManager.getConfig();

        double swapX = swap.getX();
        double swapY = swap.getY();
        double angle = swap.getAngle();

        double progress = 1D - SwapUtil.map(Math.hypot(swapX, swapY), 0, swap.getDistance(), 1D, 0D);

        List<CatmullRomWidget.CatmullRomSpline> splines = config.getSplines();

        double ease = CatmullRomWidget.getProgress(progress, splines);

        double renderX = -swap.getStartX() - Math.cos(angle) * swap.getDistance() * ease;
        double renderY = swap.getStartY() + Math.sin(angle) * swap.getDistance() * ease;

        matrices.push();
        matrices.translate(renderX, -renderY, 350);

        drawItem(copiedStack, x, y);

        double speed = swap.getDistance() / 10 * config.getAnimationSpeedFormatted();

        swap.setX(swapX + lastFrameDuration * speed * Math.cos(angle));
        swap.setY(swapY + lastFrameDuration * speed * Math.sin(angle));
        matrices.pop();
    }

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/Font;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    public void onDrawItemInSlot(Font textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo cbi) {
        if (smooth_Swapping$isHotbar() && !(client.screen instanceof ConfigScreen)) return;

        if (((ItemStackAccessor) (Object) stack).smooth_Swapping$isSwapStack()) return;

        try {
            smooth_Swapping$doOverlayRender(stack, x, y, cbi);
        } catch (Exception e) {
            SwapUtil.reset();
        }
    }

    @Unique
    private void smooth_Swapping$doOverlayRender(ItemStack stack, int x, int y, CallbackInfo cbi) throws StackOverflowError {
        int index = SwapUtil.getSlotIndex(stack);

        if (SmoothSwapping.swaps.containsKey(index)) {
            List<InventorySwap> swapList = SmoothSwapping.swaps.get(index);
            Config config = ConfigManager.getConfig();
            int stackCount = stack.getCount();
            boolean renderToSlot = true;

            for (InventorySwap swap : swapList) {
                if (!ItemStack.isSameItem(stack, swap.getSwapItem())) {
                    SmoothSwapping.swaps.remove(index);
                    return;
                }

                stackCount -= swap.getAmount();
                if (!swap.renderDestinationSlot()) {
                    renderToSlot = false;
                }

                if (swap.getAmount() > 1 || stack.isItemBarVisible()) {
                    String amount = String.valueOf(swap.getAmount());
                    double swapX = swap.getX();
                    double swapY = swap.getY();
                    double angle = swap.getAngle();

                    double progress = 1D - SwapUtil.map(Math.hypot(swapX, swapY), 0, swap.getDistance(), 1D, 0D);

                    List<CatmullRomWidget.CatmullRomSpline> splines = config.getSplines();

                    double ease = CatmullRomWidget.getProgress(progress, splines);

                    double renderX = -swap.getStartX() - (Math.cos(angle) * swap.getDistance() * ease);
                    double renderY = swap.getStartY() + (Math.sin(angle) * swap.getDistance() * ease);

                    matrices.push();
                    matrices.translate(renderX, -renderY, 350);

                    if (stack.isItemBarVisible())
                        drawItemInSlot(client.font, stack.copy(), x, y, null);
                    else
                        drawItemInSlot(client.font, stack.copy(), x, y, amount);

                    matrices.pop();
                }

            }

            if (renderToSlot && stackCount > 1) {
                drawItemInSlot(client.font, stack.copy(), x, y, String.valueOf(stackCount));
            }
            cbi.cancel();
        }
    }


    @Unique
    private boolean smooth_Swapping$isHotbar() {
        Vector3f zOffset = new Vector3f();
        matrices.peek().getPositionMatrix().getColumn(3, zOffset);
        return zOffset.round().x <= 0;
    }
}
