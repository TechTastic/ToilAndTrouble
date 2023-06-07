package net.techtastic.tat.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface IForgeFlammable {
    boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction);

    int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction);

    int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face);
}
