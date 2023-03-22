package net.techtastic.tat.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.techtastic.tat.TATBlocks;
import net.techtastic.tat.blockentity.AltarBlockEntity;

public class AltarMenu extends AbstractContainerMenu {
    private final AltarBlockEntity altar;
    private final Level level;
    private final ContainerData data;

    protected AltarMenu(int i, Inventory inv, FriendlyByteBuf extraData) {
        this(i, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public AltarMenu(int i, Inventory inv, BlockEntity entity, ContainerData data) {
        super(TATMenuTypes.ALTAR_MENU.get(), i);
        checkContainerSize(inv, 5);
        this.altar = (AltarBlockEntity) entity;
        this.data = data;
        this.level = inv.player.getLevel();
        addDataSlots(data);
    }

    public int getCurrentPower() {
        return this.data.get(0);
    }

    public int getMaxPower() {
        return this.data.get(1);
    }

    public int getRate() {
        return this.data.get(2);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, altar.getBlockPos()),
                pPlayer, TATBlocks.ALTAR.get());
    }
}
