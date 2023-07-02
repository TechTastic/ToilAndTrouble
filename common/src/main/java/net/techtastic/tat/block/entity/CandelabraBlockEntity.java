package net.techtastic.tat.block.entity;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.networking.TATNetworking;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CandelabraBlockEntity extends BlockEntity {
    int northCandleColor = -1;
    int southCandleColor = -1;
    int eastCandleColor = -1;
    int westCandleColor = -1;
    int centerCandleColor = -1;

    public CandelabraBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TATBlockEntities.CANDELABRA_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);

        this.northCandleColor = compoundTag.getInt("ToilAndTrouble$northCandle");
        this.southCandleColor = compoundTag.getInt("ToilAndTrouble$southCandle");
        this.eastCandleColor = compoundTag.getInt("ToilAndTrouble$eastCandle");
        this.westCandleColor = compoundTag.getInt("ToilAndTrouble$westCandle");
        this.centerCandleColor = compoundTag.getInt("ToilAndTrouble$centerCandle");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("ToilAndTrouble$northCandle", this.northCandleColor);
        compoundTag.putInt("ToilAndTrouble$southCandle", this.southCandleColor);
        compoundTag.putInt("ToilAndTrouble$eastCandle", this.eastCandleColor);
        compoundTag.putInt("ToilAndTrouble$westCandle", this.westCandleColor);
        compoundTag.putInt("ToilAndTrouble$centerCandle", this.centerCandleColor);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        assert level != null;
        if (level.isClientSide)
            return;

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(this.northCandleColor);
        buf.writeInt(this.southCandleColor);
        buf.writeInt(this.eastCandleColor);
        buf.writeInt(this.westCandleColor);
        buf.writeInt(this.centerCandleColor);
        buf.writeBlockPos(this.worldPosition);

        NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(), TATNetworking.ARTHANA_SYNC_S2C_PACKET_ID, buf);
    }

    public int getNorthCandleColor() {
        return this.northCandleColor;
    }

    public int getSouthCandleColor() {
        return this.southCandleColor;
    }

    public int getEastCandleColor() {
        return this.eastCandleColor;
    }

    public int getWestCandleColor() {
        return this.westCandleColor;
    }

    public int getCenterCandleColor() {
        return this.centerCandleColor;
    }

    public void setNorthCandleColor(int color) {
        this.northCandleColor = color;
        this.setChanged();
    }

    public void setSouthCandleColor(int color) {
        this.southCandleColor = color;
        this.setChanged();
    }

    public void setEastCandleColor(int color) {
        this.eastCandleColor = color;
        this.setChanged();
    }

    public void setWestCandleColor(int color) {
        this.westCandleColor = color;
        this.setChanged();
    }

    public void setCenterCandleColor(int color) {
        this.centerCandleColor = color;
        this.setChanged();
    }
}
