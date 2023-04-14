package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CandelabraBlock extends AbstractCandleBlock {
    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;

    public CandelabraBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(@NotNull BlockState blockState) {
        return List.of(
                new Vec3(0.5, 1.05, 0.5),
                new Vec3(0.5, 1.0, 0.2),
                new Vec3(0.5, 1.0, 0.8),
                new Vec3(0.2, 1.0, 0.5),
                new Vec3(0.8, 1.0, 0.5)
        );

        /*double d = (double) pos.getX() + 0.5;
        double e = (double) pos.getY() + 1.1;
        double f = (double) pos.getZ() + 0.5;
        world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
        world.addParticle(this.particle, d, e, f, 0.0, 0.0, 0.0);

        d = (double) pos.getX() + 0.5;
        e = (double) pos.getY() + 1.05;
        f = (double) pos.getZ() + 0.2;
        world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
        world.addParticle(this.particle, d, e, f, 0.0, 0.0, 0.0);

        d = (double) pos.getX() + 0.5;
        e = (double) pos.getY() + 1.05;
        f = (double) pos.getZ() + 0.8;
        world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
        world.addParticle(this.particle, d, e, f, 0.0, 0.0, 0.0);

        d = (double) pos.getX() + 0.2;
        e = (double) pos.getY() + 1.05;
        f = (double) pos.getZ() + 0.5;
        world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
        world.addParticle(this.particle, d, e, f, 0.0, 0.0, 0.0);

        d = (double) pos.getX() + 0.8;
        e = (double) pos.getY() + 1.05;
        f = (double) pos.getZ() + 0.5;
        world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
        world.addParticle(this.particle, d, e, f, 0.0, 0.0, 0.0);

        return null;*/
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(1, 0, 1, 15, 14, 15);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(LIT, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
