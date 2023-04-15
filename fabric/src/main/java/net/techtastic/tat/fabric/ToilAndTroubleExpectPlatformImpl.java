package net.techtastic.tat.fabric;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.ToilAndTroubleExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;
import net.techtastic.tat.block.entity.CauldronBlockEntity;
import net.techtastic.tat.block.entity.KettleBlockEntity;
import net.techtastic.tat.fabric.blockentity.CauldronBlockEntityFabric;
import net.techtastic.tat.fabric.blockentity.KettleBlockEntityFabric;

import java.nio.file.Path;

public class ToilAndTroubleExpectPlatformImpl {
    /**
     * This is our actual method to {@link ToilAndTroubleExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static BlockEntity getKettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new KettleBlockEntityFabric(blockPos, blockState);
    }

    public static BlockEntityType.BlockEntitySupplier<KettleBlockEntity> getKettleBlockEntitySupplier() {
        return KettleBlockEntityFabric::new;
    }

    public static BlockEntityTicker<? super KettleBlockEntity> getKettleBlockEntityTicker() {
        return KettleBlockEntityFabric::tick;
    }

    public static BlockEntity getCauldronBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CauldronBlockEntityFabric(blockPos, blockState);
    }

    public static BlockEntityType.BlockEntitySupplier<CauldronBlockEntity> getCauldronBlockEntitySupplier() {
        return CauldronBlockEntityFabric::new;
    }

    public static BlockEntityTicker<? super CauldronBlockEntity> getCauldronBlockEntityTicker() {
        return CauldronBlockEntityFabric::tick;
    }
}
