package net.techtastic.tat.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.techtastic.tat.ToilAndTroubleExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;
import net.techtastic.tat.block.entity.KettleBlockEntity;
import net.techtastic.tat.forge.blockentity.KettleBlockEntityForge;
import net.techtastic.tat.forge.util.ForgeFluidTank;
import net.techtastic.tat.util.FluidTank;

import java.nio.file.Path;

public class ToilAndTroubleExpectPlatformImpl {
    /**
     * This is our actual method to {@link ToilAndTroubleExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static BlockEntity getKettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new KettleBlockEntityForge(blockPos, blockState);
    }

    public static BlockEntityType.BlockEntitySupplier<KettleBlockEntity> getKettleBlockEntitySupplier() {
        return KettleBlockEntityForge::new;
    }

    /*public static BlockEntityTicker<? super KettleBlockEntity> getKettleBlockEntityTicker() {
        return KettleBlockEntityForge::tick;
    }*/

    public static FluidTank createFluidTank(Fluid fluid, int capacity, BlockEntity be) {
        return new ForgeFluidTank(capacity, fluid) {
            @Override
            public void onContentsChanged() {
                be.setChanged();
            }
        };
    }
}
