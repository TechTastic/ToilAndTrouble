package net.techtastic.tat.networking.packet;

import dev.architectury.fluid.FluidStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.techtastic.tat.ToilAndTroubleExpectPlatform;
import net.techtastic.tat.util.FluidTank;

public class FluidSyncS2CPacket {
    private final FluidTank tank;

    public FluidSyncS2CPacket(Fluid fluid, int capacity, int amount, BlockEntity be) {
        this.tank = ToilAndTroubleExpectPlatform.createFluidTank(fluid, capacity, be);
        this.tank.setFluid(FluidStack.create(fluid, amount));
    }

    public FluidSyncS2CPacket(Fluid fluid, int capacity, int amount, BlockEntity be, CompoundTag tag) {
        this(fluid, capacity, amount, be);
        this.tank.setFluid(FluidStack.create(fluid, amount, tag));
    }

    public FluidSyncS2CPacket(FriendlyByteBuf buf) {
        FluidStack stack = FluidStack.read(buf);
        int capacity = buf.readInt();

        stack.hasTag() ?
                this(stack.getFluid(), capacity, stack.getAmount(), )
    }
}
