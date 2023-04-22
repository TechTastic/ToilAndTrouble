package net.techtastic.tat.fabric.blockentity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.block.entity.KettleBlockEntity;

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

    public void setFluidLevel(FluidVariant fluidVariant, long fluidLevel) {
        this.fluidStorage.variant = fluidVariant;
        this.fluidStorage.amount = fluidLevel;
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

    @Override
    public boolean hasEnoughFluid(KettleBlockEntity kettle) {
        return ((KettleBlockEntityFabric) kettle).fluidStorage.amount >= FluidConstants.BUCKET;
    }

    @Override
    public boolean hasEnoughForBottling(KettleBlockEntity kettle) {
        return ((KettleBlockEntityFabric) kettle).fluidStorage.amount >= FluidConstants.BOTTLE;
    }
}
