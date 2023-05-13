package net.techtastic.tat.forge.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.techtastic.tat.block.entity.KettleBlockEntity;
import net.techtastic.tat.forge.ForgeFluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KettleBlockEntityForge extends KettleBlockEntity {
    public KettleBlockEntityForge(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return ((ForgeFluidTank) this.tank).getCapability(cap, side);
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        ((ForgeFluidTank) this.tank).onLoad();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        ((ForgeFluidTank) this.tank).invalidateCaps();
    }
}
