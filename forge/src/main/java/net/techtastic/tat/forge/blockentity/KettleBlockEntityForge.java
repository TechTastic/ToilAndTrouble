package net.techtastic.tat.forge.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.techtastic.tat.block.entity.KettleBlockEntity;
import org.jetbrains.annotations.Nullable;

public class KettleBlockEntityForge extends KettleBlockEntity {
    private final FluidTank fluidStorage = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == Fluids.WATER;
        }
    };

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    public KettleBlockEntityForge(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag = fluidStorage.writeToNBT(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        fluidStorage.readFromNBT(compoundTag);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return lazyFluidHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        lazyFluidHandler = LazyOptional.of(() -> fluidStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
    }

    public void setFluid(FluidStack stack) {
        this.fluidStorage.setFluid(stack);
    }

    public FluidStack getFluidStack() {
        return this.fluidStorage.getFluid();
    }

    @Override
    public boolean hasEnoughFluid(KettleBlockEntity kettle) {
        return ((KettleBlockEntityForge) kettle).fluidStorage.getFluidAmount() == 1000;
    }

    @Override
    public boolean hasEnoughForBottling(KettleBlockEntity kettle) {
        return ((KettleBlockEntityForge) kettle).fluidStorage.getFluidAmount() == 333;
    }

    @Override
    public boolean tryInsertFluid(KettleBlockEntity kettle, ItemStack stack) {
        KettleBlockEntityForge fabric = ((KettleBlockEntityForge) kettle);
        if (!stack.is(Items.WATER_BUCKET) && fabric.fluidStorage.amount + FluidConstants.BUCKET > fabric.fluidStorage.getCapacity())
            return false;
        try(Transaction transaction = Transaction.openOuter()) {
            fabric.fluidStorage.insert(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET, transaction);
            transaction.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean tryExtractFluid(KettleBlockEntity kettle, ItemStack stack) {
        KettleBlockEntityForge fabric = ((KettleBlockEntityForge) kettle);
        if ((!stack.is(Items.BUCKET) && !hasEnoughFluid(kettle)) ||
                (!stack.is(Items.GLASS_BOTTLE) && !hasEnoughForBottling(kettle) && !hasRecipe(kettle)))
            return false;
        try(Transaction transaction = Transaction.openOuter()) {
            fabric.fluidStorage.extract(FluidVariant.of(Fluids.WATER), stack.is(Items.BUCKET) ? FluidConstants.BUCKET : FluidConstants.BOTTLE, transaction);
            transaction.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
