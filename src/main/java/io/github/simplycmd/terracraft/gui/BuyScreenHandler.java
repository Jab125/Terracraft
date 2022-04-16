package io.github.simplycmd.terracraft.gui;

import io.github.simplycmd.terracraft.items.util.Value;
import io.github.simplycmd.terracraft.registry.ScreenHandlerRegistry;
import io.github.simplycmd.terracraft.util.Offer;
import io.github.simplycmd.terracraft.util.OfferList;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.MerchantInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static io.github.simplycmd.terracraft.items.util.Value.trySpend;
import static io.github.simplycmd.terracraft.items.util.Value.trySpend2;

public class BuyScreenHandler extends ScreenHandler {
    private int recipeIndex;
    private final SimpleInventory simpleInventory;
    private final PlayerInventory playerInventory;
    public BuyScreenHandler(PlayerInventory playerInventory, int syncId) {
        super(ScreenHandlerRegistry.BUY_SCREEN_HANDLER, syncId);
        this.simpleInventory = new SimpleInventory(1);
        this.playerInventory = playerInventory;
        this.addSlot(new Slot(this.simpleInventory, 0, 220, 37){
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                this.onCrafted(stack);
                super.onTakeItem(player, stack);
                take();
            }
        });

        int i;
        int j;

        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 108 + j * 18, 84 + i * 18){
                    @Override
                    public void onTakeItem(PlayerEntity player, ItemStack stack) {
                        super.onTakeItem(player, stack);
                        BuyScreenHandler.this.update();
                    }
                });
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 108 + i * 18, 142) {
                @Override
                public void onTakeItem(PlayerEntity player, ItemStack stack) {
                    super.onTakeItem(player, stack);
                    BuyScreenHandler.this.update();
                }
            });
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public void setRecipeIndex(int selectedIndex) {
        recipeIndex = selectedIndex;
        update();
    }

    public void switchTo(int selectedIndex) {
        recipeIndex = selectedIndex;
    }

    private OfferList oferu = null;
    public OfferList getRecipes() {
        if (oferu != null) return oferu;
        var offers = new OfferList();
        for (int l = 0; l < 15; l++) {
            offers.add(new Offer(Registry.ITEM.get(l+1), new Value(l)));
        }
        this.oferu = offers;
        return oferu;
    }

    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 0) {
                if (!this.insertItem(itemStack2, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (!this.insertItem(itemStack2, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    public boolean update() {
        if (trySpend2(this.playerInventory, getRecipes().get(this.recipeIndex).getValue().getValue()))
        this.simpleInventory.setStack(0, getRecipes().get(this.recipeIndex).getItem().getDefaultStack());
        else this.simpleInventory.setStack(0, ItemStack.EMPTY);
        return trySpend2(this.playerInventory, getRecipes().get(this.recipeIndex).getValue().getValue());
    }

    public void take() {
        trySpend(this.playerInventory, getRecipes().get(this.recipeIndex).getValue().getValue());
        this.playerInventory.markDirty();
    }
}
