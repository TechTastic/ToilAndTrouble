package net.techtastic.tat.util;

import dev.architectury.fluid.FluidStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public abstract class FluidTank {
    protected final int capacity;
    protected final Fluid fluid;

    public FluidTank(int capacity, Fluid fluid) {
        this.capacity = capacity;
        this.fluid = fluid;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isFluidValid(FluidStack stack) {
        return this.fluid.equals(stack.getFluid());
    }

    public abstract void onContentsChanged();

    public abstract boolean hasEnoughFluid(FluidTank tank);

    public abstract boolean hasEnoughForBottling(FluidTank tank);

    public abstract boolean tryInsertFluid(FluidTank tank, ItemStack stack);

    public abstract boolean tryExtractFluid(FluidTank tank, ItemStack stack);

    public abstract void writeToNbt(CompoundTag compoundTag);

    public abstract void readFromNbt(CompoundTag compoundTag);

    public abstract long getRemainingFluid();

    public Fluid getFluid() {
        return this.fluid;
    }

    public abstract double getPercentage();

    public abstract void setFluid(FluidStack stack);

    public abstract FluidStack getFluidStack();
}
