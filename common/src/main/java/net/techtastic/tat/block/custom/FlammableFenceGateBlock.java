package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.util.IForgeFlammable;

public class FlammableFenceGateBlock extends FenceGateBlock implements IForgeFlammable {
    private final int flammability;
    private final int firespreadspeed;

    public FlammableFenceGateBlock(Properties properties, int flammability, int firespreadspeed) {
        super(properties);
        this.flammability = flammability;
        this.firespreadspeed = firespreadspeed;
    }

    public FlammableFenceGateBlock(Properties properties) {
        this(properties, 20, 5);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return this.flammability;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return this.firespreadspeed;
    }
}
