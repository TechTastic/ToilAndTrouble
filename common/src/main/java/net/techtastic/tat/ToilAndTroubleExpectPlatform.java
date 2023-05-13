package net.techtastic.tat;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.techtastic.tat.block.entity.CauldronBlockEntity;
import net.techtastic.tat.block.entity.KettleBlockEntity;
import net.techtastic.tat.util.FluidTank;

import java.nio.file.Path;

public class ToilAndTroubleExpectPlatform {
    /**
     * We can use {@link Platform#getConfigFolder()} but this is just an example of {@link ExpectPlatform}.
     * <p>
     * This must be a <b>public static</b> method. The platform-implemented solution must be placed under a
     * platform sub-package, with its class suffixed with {@code Impl}.
     * <p>
     * Example:
     * Expect: net.tat.ToilAndTroubleExpectPlatform#getConfigDirectory()
     * Actual Fabric: net.tat.fabric.ExampleExpectPlatformImpl#getConfigDirectory()
     * Actual Forge: net.tat.forge.ExampleExpectPlatformImpl#getConfigDirectory()
     * <p>
     * <a href="https://plugins.jetbrains.com/plugin/16210-architectury">You should also get the IntelliJ plugin to help with @ExpectPlatform.</a>
     */
    @ExpectPlatform
    public static Path getConfigDirectory() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }

    @ExpectPlatform
    public static BlockEntity getKettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<KettleBlockEntity> getKettleBlockEntitySupplier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static BlockEntityTicker<? super KettleBlockEntity> getKettleBlockEntityTicker() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static BlockEntity getCauldronBlockEntity(BlockPos blockPos, BlockState blockState) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<CauldronBlockEntity> getCauldronBlockEntitySupplier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static BlockEntityTicker<? super CauldronBlockEntity> getCauldronBlockEntityTicker() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static FluidTank createFluidTank(Fluid fluid, int capacity, BlockEntity be) {
        throw new AssertionError();
    }
}
