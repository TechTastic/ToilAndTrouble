package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.util.IForgeFlammable;

public class FlammableRotatedPillarBlock extends RotatedPillarBlock implements IForgeFlammable {
    private final int flammability;
    private final int firespreadspeed;
    public FlammableRotatedPillarBlock(Properties properties, int flammability, int firespreadspeed) {
        super(properties);
        this.flammability = flammability;
        this.firespreadspeed = firespreadspeed;
    }

    public FlammableRotatedPillarBlock(Properties properties) {
        this(properties, 5, 5);
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
