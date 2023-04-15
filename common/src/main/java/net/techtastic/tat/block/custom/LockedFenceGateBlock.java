package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.techtastic.tat.block.entity.BaseLockedBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LockedFenceGateBlock extends FlammableFenceGateBlock implements EntityBlock {
    public LockedFenceGateBlock(Properties properties, int flammability, int firespreadspeed) {
        super(properties, flammability, firespreadspeed);
    }

    public LockedFenceGateBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BaseLockedBlockEntity(blockPos, blockState, UUID.randomUUID());
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BaseLockedBlockEntity locked = (BaseLockedBlockEntity) level.getBlockEntity(blockPos);
        if (!locked.canUnlock(locked, player.getItemInHand(interactionHand))) return InteractionResult.FAIL;

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);

        BaseLockedBlockEntity locked = (BaseLockedBlockEntity) level.getBlockEntity(blockPos);
        locked.makeInitialKey(locked, blockPos);
    }
}
