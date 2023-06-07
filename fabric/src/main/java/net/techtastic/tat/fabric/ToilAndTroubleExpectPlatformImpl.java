package net.techtastic.tat.fabric;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.techtastic.tat.ToilAndTroubleExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;
import net.techtastic.tat.block.entity.CauldronBlockEntity;
import net.techtastic.tat.block.entity.KettleBlockEntity;
import net.techtastic.tat.fabric.util.FabricFluidTank;
import net.techtastic.tat.util.FluidTank;

import java.nio.file.Path;

public class ToilAndTroubleExpectPlatformImpl {
    /**
     * This is our actual method to {@link ToilAndTroubleExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static BlockEntity getKettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new KettleBlockEntity(blockPos, blockState);
    }

    public static BlockEntityType.BlockEntitySupplier<KettleBlockEntity> getKettleBlockEntitySupplier() {
        return KettleBlockEntity::new;
    }

    /*public static BlockEntityTicker<? super KettleBlockEntity> getKettleBlockEntityTicker() {
        return KettleBlockEntity::tick;
    }*/

    public static BlockEntity getCauldronBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CauldronBlockEntity(blockPos, blockState);
    }

    public static BlockEntityType.BlockEntitySupplier<CauldronBlockEntity> getCauldronBlockEntitySupplier() {
        return CauldronBlockEntity::new;
    }

    public static BlockEntityTicker<? super CauldronBlockEntity> getCauldronBlockEntityTicker() {
        return CauldronBlockEntity::tick;
    }

    public static FluidTank createFluidTank(Fluid fluid, int capacity, BlockEntity be) {
        return new FabricFluidTank(capacity * 81, fluid) {
            @Override
            public void onContentsChanged() {
                be.setChanged();
            }
        };
    }
}
