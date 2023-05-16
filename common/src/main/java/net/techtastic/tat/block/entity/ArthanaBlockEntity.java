package net.techtastic.tat.block.entity;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.networking.TATNetworking;
import org.jetbrains.annotations.NotNull;

public class ArthanaBlockEntity extends BlockEntity {
    private ItemStack arthana = ItemStack.EMPTY;

    public ArthanaBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TATBlockEntities.ARTHANA_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        ContainerHelper.saveAllItems(compoundTag, NonNullList.of(ItemStack.EMPTY, this.arthana));

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);

        NonNullList<ItemStack> temp = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, temp);
        this.arthana = temp.get(0);
    }

    public ItemStack getArthana() {
        return this.arthana;
    }

    public void setArthana(ItemStack arthana) {
        this.arthana = arthana;
        this.setChanged();
    }

    @Override
    public void setChanged() {
        super.setChanged();

        assert level != null;
        if (level.isClientSide) return;

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeItem(this.arthana);
        buf.writeBlockPos(this.worldPosition);

        NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(), TATNetworking.ARTHANA_SYNC_S2C_PACKET_ID, buf);
    }
}
