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
import net.techtastic.tat.block.entity.DistilleryBlockEntity;
import net.techtastic.tat.screen.slot.TATFuelSlot;
import net.techtastic.tat.screen.slot.TATJarSlot;
import net.techtastic.tat.screen.slot.TATOvenInput;
import net.techtastic.tat.screen.slot.TATResultSlot;
import org.jetbrains.annotations.NotNull;

public class DistilleryMenu extends AbstractContainerMenu {
    private final DistilleryBlockEntity cio;
    private final Level level;
    private final ContainerData data;

    protected DistilleryMenu(int i, Inventory inv, FriendlyByteBuf extraData) {
        this(i, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(5));
    }

    public DistilleryMenu(int i, Inventory inv, BlockEntity entity, ContainerData data) {
        super(TATMenuTypes.DISTILLERY_MENU.get(), i);
        checkContainerSize(inv, 7);
        this.cio = (DistilleryBlockEntity) entity;
        this.data = data;
        this.level = inv.player.getLevel();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.addSlot(new Slot(cio, 0, 48, 18));
        this.addSlot(new Slot(cio, 1, 48, 36));
        this.addSlot(new TATJarSlot(cio, 2, 48, 56));
        this.addSlot(new TATResultSlot(cio, 3, 109, 18));
        this.addSlot(new TATResultSlot(cio, 4, 127, 18));
        this.addSlot(new TATResultSlot(cio, 5, 109, 36));
        this.addSlot(new TATResultSlot(cio, 6, 127, 36));

        addDataSlots(data);
    }

    public boolean hasPower() {
        return data.get(4) == 1;
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledPowerProgress() {
        int progress = this.data.get(2);
        int maxProgress = this.data.get(3);  // Max Progress
        int progressArrowSize = 29; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledCraftingProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = 37; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
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
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, cio.getBlockPos()),
                pPlayer, TATBlocks.DISTILLERY.get());
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
