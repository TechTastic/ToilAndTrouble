package net.techtastic.tat.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.techtastic.tat.block.TATBlocks;
import net.techtastic.tat.block.entity.CastIronOvenBlockEntity;
import net.techtastic.tat.screen.slot.TATFuelSlot;
import net.techtastic.tat.screen.slot.TATJarSlot;
import net.techtastic.tat.screen.slot.TATOvenInput;
import net.techtastic.tat.screen.slot.TATResultSlot;

public class CastIronOvenMenu extends AbstractContainerMenu {
    private final CastIronOvenBlockEntity cio;
    private final Level level;
    private final ContainerData data;

    protected CastIronOvenMenu(int i, Inventory inv, FriendlyByteBuf extraData) {
        this(i, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public CastIronOvenMenu(int i, Inventory inv, BlockEntity entity, ContainerData data) {
        super(TATMenuTypes.CAST_IRON_OVEN_MENU.get(), i);
        checkContainerSize(inv, 5);
        this.cio = (CastIronOvenBlockEntity) entity;
        this.data = data;
        this.level = inv.player.getLevel();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.addSlot(new TATOvenInput(cio, 0, 44, 19));
        this.addSlot(new TATFuelSlot(cio, 1, 44, 54));
        this.addSlot(new TATJarSlot(cio, 2, 80, 54));
        this.addSlot(new TATResultSlot(cio, 3, 116, 19));
        this.addSlot(new TATResultSlot(cio, 4, 116, 54));

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = 48; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public boolean isConsumingFuel() {
        return data.get(2) > 0;
    }

    public int getScaledFuelProgress() {
        int fuelTime = this.data.get(2);
        int maxFuelTime = this.data.get(3);  // Max Progress
        int progressFlameSize = 14; // This is the height in pixels of your arrow

        return maxFuelTime != 0 ? (int)(((float)fuelTime / (float)maxFuelTime) * progressFlameSize) : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < 36) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, 36, 40, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index > 36) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, 0, 35, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, cio.getBlockPos()),
                pPlayer, TATBlocks.CAST_IRON_OVEN.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
