package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.util.IForgeFlammable;

public class FlammableSlabBlock extends SlabBlock implements IForgeFlammable {
    private final int flammability;
    private final int firespreadspeed;

    public FlammableSlabBlock(Properties properties, int flammability, int firespreadspeed) {
        super(properties);
        this.flammability = flammability;
        this.firespreadspeed = firespreadspeed;
    }

    public FlammableSlabBlock(Properties properties) {
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
