package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.techtastic.tat.blockentity.BaseLockedBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LockedStoneButtonBlock extends StoneButtonBlock implements EntityBlock {
    private ItemStack[] handStacks;

    public LockedStoneButtonBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BaseLockedBlockEntity(blockPos, blockState, UUID.randomUUID());
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        this.handStacks = new ItemStack[] { player.getMainHandItem(), player.getOffhandItem() };
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void press(BlockState blockState, Level level, BlockPos blockPos) {
        if (this.handStacks != null) {
            BaseLockedBlockEntity locked = (BaseLockedBlockEntity) level.getBlockEntity(blockPos);

            boolean canUnlock = false;
            for (ItemStack potentialKey : this.handStacks) {
                if (canUnlock) continue;

                canUnlock = locked.canUnlock(locked, potentialKey);
                if (canUnlock) {
                    super.press(blockState, level, blockPos);
                }
            }
        }
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);

        BaseLockedBlockEntity locked = (BaseLockedBlockEntity) level.getBlockEntity(blockPos);
        locked.makeInitialKey(locked, blockPos);
    }
}
