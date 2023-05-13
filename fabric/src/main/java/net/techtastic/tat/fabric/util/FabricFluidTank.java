package net.techtastic.tat.fabric.util;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.techtastic.tat.util.FluidTank;

public class FabricFluidTank extends FluidTank {
    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FabricFluidTank.this.getCapacity();
        }

        @Override
        protected void onFinalCommit() {
            onContentsChanged();
        }
    };

    public FabricFluidTank(int capacity, Fluid fluid) {
        super(capacity, fluid);
    }

    @Override
    public void onContentsChanged() {
    }

    @Override
    public boolean hasEnoughFluid(FluidTank tank) {
        return ((FabricFluidTank) tank).fluidStorage.amount >= FluidConstants.BUCKET;
    }

    @Override
    public boolean hasEnoughForBottling(FluidTank tank) {
        return ((FabricFluidTank) tank).fluidStorage.amount >= FluidConstants.BOTTLE;
    }

    @Override
    public boolean tryInsertFluid(FluidTank tank, ItemStack stack) {
        FabricFluidTank fabric = ((FabricFluidTank) tank);
        if (!stack.is(Items.WATER_BUCKET) && fabric.fluidStorage.amount + FluidConstants.BUCKET > fabric.fluidStorage.getCapacity())
            return false;
        try(Transaction transaction = Transaction.openOuter()) {
            fabric.fluidStorage.insert(FluidVariant.of(fabric.fluid), FluidConstants.BUCKET, transaction);
            transaction.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean tryExtractFluid(FluidTank tank, ItemStack stack) {
        FabricFluidTank fabric = ((FabricFluidTank) tank);
        if ((!stack.is(Items.BUCKET) && !hasEnoughFluid(tank)) ||
                (!stack.is(Items.GLASS_BOTTLE) && !hasEnoughForBottling(tank)))
            return false;
        try(Transaction transaction = Transaction.openOuter()) {
            fabric.fluidStorage.extract(FluidVariant.of(fabric.fluid), stack.is(Items.BUCKET) ? FluidConstants.BUCKET : FluidConstants.BOTTLE, transaction);
            transaction.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {
        compoundTag.put("ToilAndTrouble$variant", this.fluidStorage.variant.toNbt());
        compoundTag.putLong("ToilAndTrouble$amount", this.fluidStorage.amount);
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        this.fluidStorage.variant = FluidVariant.fromNbt(compoundTag.getCompound("ToilAndTrouble$variant"));
        this.fluidStorage.amount = compoundTag.getLong("ToilAndTrouble$amount");
    }
}
