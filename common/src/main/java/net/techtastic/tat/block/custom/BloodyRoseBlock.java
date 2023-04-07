package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.techtastic.tat.block.entity.BaseTaglockedBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BloodyRoseBlock extends BaseEntityBlock {
    private static final BooleanProperty BLOOD = BooleanProperty.create("blood");
    public BloodyRoseBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(BLOOD, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BLOOD);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        super.entityInside(blockState, level, blockPos, entity);

        if (!(entity instanceof LivingEntity)) return;
        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof BaseTaglockedBlockEntity taglocked) {
            if (taglocked.getTaggedEntity() != null && taglocked.getTaggedEntity().equals(entity)) return;
            entity.hurt(DamageSource.CACTUS, 0.0f);
            taglocked.setTaggedEntity(entity);
            level.setBlockAndUpdate(blockPos, blockState.setValue(BLOOD, true));
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BaseTaglockedBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
