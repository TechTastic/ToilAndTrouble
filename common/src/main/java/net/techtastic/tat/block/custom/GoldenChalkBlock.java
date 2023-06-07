package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.api.RitualType;
import org.jetbrains.annotations.Nullable;

public class GoldenChalkBlock extends ChalkBlock implements EntityBlock {
    public GoldenChalkBlock(Properties properties) {
        super(RitualType.GOLDEN, properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return EntityBlock.super.getTicker(level, blockState, blockEntityType);
    }
}
