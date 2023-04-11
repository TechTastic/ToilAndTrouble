package net.techtastic.tat.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.block.TATBlockEntities;

public class KettleBlockEntity extends BlockEntity {
    public KettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TATBlockEntities.KETTLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, BlockEntity blockEntity) {
    }
}
