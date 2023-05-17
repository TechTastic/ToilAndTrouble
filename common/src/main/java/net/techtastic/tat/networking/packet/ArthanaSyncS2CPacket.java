package net.techtastic.tat.networking.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.techtastic.tat.block.entity.ArthanaBlockEntity;

import java.util.function.Supplier;

public class ArthanaSyncS2CPacket {
    private final ItemStack stack;
    private final BlockPos pos;

    public ArthanaSyncS2CPacket(ItemStack stack, BlockPos pos) {
        this.stack = stack;
        this.pos = pos;
    }

    public ArthanaSyncS2CPacket(FriendlyByteBuf buf) {
        this(buf.readItem(), buf.readBlockPos());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeBlockPos(pos);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> {
            assert Minecraft.getInstance().level != null;
            System.err.println("Applying!");
            System.err.println("Block: " + Minecraft.getInstance().level.getBlockState(pos));
            System.err.println("BlockEntity: " + Minecraft.getInstance().level.getBlockEntity(pos));
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof ArthanaBlockEntity blockEntity) {
                System.err.println("Block at Pos is Arthana!");
                blockEntity.arthana = stack;
            }
        });
    }
}
