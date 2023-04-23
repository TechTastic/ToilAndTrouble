package net.techtastic.tat.fabric.blockentity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.techtastic.tat.block.entity.KettleBlockEntity;
import org.lwjgl.system.CallbackI;

public class KettleBlockEntityFabric extends KettleBlockEntity {
    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidConstants.BUCKET;
        }

        @Override
        protected void onFinalCommit() {
            setChanged();
        }
    };

    public KettleBlockEntityFabric(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.put("ToilAndTrouble$variant", this.fluidStorage.variant.toNbt());
        compoundTag.putLong("ToilAndTrouble$amount", this.fluidStorage.amount);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        this.fluidStorage.variant = FluidVariant.fromNbt(compoundTag.getCompound("ToilAndTrouble$variant"));
        this.fluidStorage.amount = compoundTag.getLong("ToilAndTrouble$amount");
    }

    public void setFluidLevel(FluidVariant fluidVariant, long fluidLevel) {
        this.fluidStorage.variant = fluidVariant;
        this.fluidStorage.amount = fluidLevel;
    }

    @Override
    public boolean hasEnoughFluid(KettleBlockEntity kettle) {
        return ((KettleBlockEntityFabric) kettle).fluidStorage.amount >= FluidConstants.BUCKET;
    }

    @Override
    public boolean hasEnoughForBottling(KettleBlockEntity kettle) {
        return ((KettleBlockEntityFabric) kettle).fluidStorage.amount >= FluidConstants.BOTTLE;
    }

    @Override
    public boolean tryInsertFluid(KettleBlockEntity kettle, ItemStack stack) {
        KettleBlockEntityFabric fabric = ((KettleBlockEntityFabric) kettle);
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
        KettleBlockEntityFabric fabric = ((KettleBlockEntityFabric) kettle);
        if ((!stack.is(Items.BUCKET) && fabric.fluidStorage.amount - FluidConstants.BUCKET < 0) ||
                (!stack.is(Items.GLASS_BOTTLE) && fabric.fluidStorage.amount - FluidConstants.BOTTLE < 0 && hasRecipe(kettle)))
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
