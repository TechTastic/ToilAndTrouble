package net.techtastic.tat.forge.util;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.techtastic.tat.util.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class ForgeFluidTank extends FluidTank {
    private final net.minecraftforge.fluids.capability.templates.FluidTank fluidStorage = new net.minecraftforge.fluids.capability.templates.FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            ForgeFluidTank.this.onContentsChanged();
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.hasTag() ?
                    ForgeFluidTank.this.isFluidValid(dev.architectury.fluid.FluidStack.create(
                            stack.getFluid(), stack.getAmount(), stack.getTag())) :
                    ForgeFluidTank.this.isFluidValid(dev.architectury.fluid.FluidStack.create(
                            stack.getFluid(), stack.getAmount()));
        }
    };

    LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    public ForgeFluidTank(int capacity, Fluid fluid) {
        super(capacity, fluid);
    }

    @Override
    public void onContentsChanged() {
    }

    @Override
    public boolean hasEnoughFluid(FluidTank tank) {
        return ((ForgeFluidTank) tank).fluidStorage.getFluidAmount() >= 1000;
    }

    @Override
    public boolean hasEnoughForBottling(FluidTank tank) {
        return ((ForgeFluidTank) tank).fluidStorage.getFluidAmount() >= 334;
    }

    @Override
    public boolean tryInsertFluid(FluidTank tank, ItemStack stack) {
        AtomicReference<Boolean> bool = new AtomicReference<>(false);
        ForgeFluidTank forge = (ForgeFluidTank) tank;
        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(handler -> {
            int drainAmount = Math.min(forge.fluidStorage.getSpace(), 1000);

            FluidStack sim = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if (forge.fluidStorage.isFluidValid(sim)) {
                fillTankWithFluid(forge, handler.getContainer());
                bool.set(true);
            }
        });
        return bool.get();
    }

    public void fillTankWithFluid(ForgeFluidTank forge, ItemStack container) {
        container.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(handler -> {
            int drainAmount = 1000;
            FluidStack sim = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if (forge.fluidStorage.isFluidValid(sim)) {
                sim = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                forge.fluidStorage.fill(sim, IFluidHandler.FluidAction.EXECUTE);
            }
        });
    }

    @Override
    public boolean tryExtractFluid(FluidTank tank, ItemStack stack) {
        ForgeFluidTank forge = (ForgeFluidTank) tank;
        if (!((stack.is(Items.BUCKET) && !hasEnoughFluid(forge)) ||
                (stack.is(Items.GLASS_BOTTLE) && hasEnoughForBottling(forge))))
            return false;
        drainFluidFromTank(forge, stack.is(Items.BUCKET) ? 1000 : forge.fluidStorage.getFluidAmount() == 334 ? 334 : 333);
        return true;
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {
        compoundTag.put("ToilAndTrouble$tank", this.fluidStorage.writeToNBT(new CompoundTag()));
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        this.fluidStorage.readFromNBT(compoundTag.getCompound("ToilAndTrouble$tank"));
    }

    @Override
    public long getRemainingFluid() {
        return this.fluidStorage.getFluidAmount();
    }

    @Override
    public double getPercentage() {
        return (double) this.fluidStorage.getFluidAmount() / this.fluidStorage.getCapacity();
    }

    @Override
    public void setFluid(dev.architectury.fluid.FluidStack stack) {
        this.fluidStorage.setFluid(stack.hasTag() ?
                new FluidStack(stack.getFluid(), (int) stack.getAmount(), stack.getTag()) :
                new FluidStack(stack.getFluid(), (int) stack.getAmount())
        );
    }

    public void drainFluidFromTank(ForgeFluidTank forge, int amount) {
        forge.fluidStorage.drain(amount, IFluidHandler.FluidAction.EXECUTE);
    }

    public LazyOptional<IFluidHandler> getHandler() {
        return lazyFluidHandler;
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return lazyFluidHandler.cast();
    }

    public void onLoad() {
        lazyFluidHandler = LazyOptional.of(() -> this.fluidStorage);
    }

    public void invalidateCaps() {
        lazyFluidHandler.invalidate();
    }
}
