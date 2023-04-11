package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.ToilAndTroubleExpectPlatform;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.block.entity.DistilleryBlockEntity;
import org.jetbrains.annotations.Nullable;

public class KettleBlock extends BaseEntityBlock {
    public KettleBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ToilAndTroubleExpectPlatform.getKettleBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, TATBlockEntities.KETTLE_BLOCK_ENTITY.get(), ToilAndTroubleExpectPlatform.getKettleBlockEntityTicker());
    }
}
