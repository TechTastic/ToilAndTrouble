package net.techtastic.tat.networking.packet;

import dev.architectury.fluid.FluidStack;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluid;
import net.techtastic.tat.block.entity.KettleBlockEntity;

import java.util.function.Supplier;

public class FluidSyncS2CPacket {
    private final FluidStack stack;
    private final BlockPos pos;

    public FluidSyncS2CPacket(FluidStack stack, BlockPos pos) {
        this.stack = stack;
        this.pos = pos;
    }

    public FluidSyncS2CPacket(FriendlyByteBuf buf) {
        this(FluidStack.read(buf), buf.readBlockPos());
    }

    public void toBytes(FriendlyByteBuf buf) {
        this.stack.write(buf);
        buf.writeBlockPos(pos);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> {
            assert Minecraft.getInstance().level != null;
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof KettleBlockEntity blockEntity) {
                blockEntity.tank.setFluid(this.stack);
            }
        });
    }
}
