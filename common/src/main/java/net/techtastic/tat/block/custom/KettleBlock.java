package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.techtastic.tat.ToilAndTroubleExpectPlatform;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.block.entity.DistilleryBlockEntity;
import net.techtastic.tat.block.entity.KettleBlockEntity;
import org.jetbrains.annotations.Nullable;

public class KettleBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public KettleBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState()
                .setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
                .setValue(LIT, false);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        Direction rotated = rotation.rotate(blockState.getValue(FACING));
        return blockState.setValue(FACING, rotated);
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(LIT);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(FACING)) {
            case NORTH, SOUTH -> Block.box(0, 0, 4, 16, 16, 12);
            default -> Block.box(4, 0, 0, 12, 16, 16);
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ToilAndTroubleExpectPlatform.getKettleBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, TATBlockEntities.KETTLE_BLOCK_ENTITY.get(), KettleBlockEntity::tick);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        super.entityInside(blockState, level, blockPos, entity);

        if (!(entity instanceof ItemEntity item)) return;

        BlockEntity be = level.getBlockEntity(blockPos);
        if (!(be instanceof KettleBlockEntity kettle)) return;

        ItemStack stack = item.getItem();
        ItemStack test = new ItemStack(stack.getItem(), 1);

        if (!kettle.getItem(kettle.getContainerSize() - 1).isEmpty() &&
                kettle.hasEnoughFluid(kettle) &&
                kettle.testForNextIngredient(level, test))
            return;

        if (stack.getCount() == 1)
            item.remove(Entity.RemovalReason.DISCARDED);
        else {
            stack.shrink(1);
            item.setItem(stack);
        }

        kettle.setItem(kettle.inventory.indexOf(ItemStack.EMPTY), test);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack stack = player.getItemInHand(interactionHand);

        BlockEntity be = level.getBlockEntity(blockPos);
        if (!(be instanceof KettleBlockEntity kettle))
            return InteractionResult.FAIL;

        ItemStack output = KettleBlockEntity.getRecipeOutput(kettle);

        if (stack.is(Items.WATER_BUCKET)) {
            if (!kettle.tryInsertFluid(kettle, stack))
                return InteractionResult.FAIL;
            player.setItemInHand(interactionHand, new ItemStack(Items.BUCKET));
            return InteractionResult.SUCCESS;
        } else if (stack.is(Items.BUCKET) || stack.is(Items.GLASS_BOTTLE)) {
            if (!kettle.tryExtractFluid(kettle, stack))
                return InteractionResult.FAIL;

            if (stack.is(Items.BUCKET)) {
                player.setItemInHand(interactionHand, new ItemStack(Items.WATER_BUCKET));
                return InteractionResult.SUCCESS;
            }
            if (stack.is(Items.GLASS_BOTTLE) && !output.isEmpty()) {
                if (output.getCount() == 1)
                    kettle.clearOutput();
                else
                    kettle.shrinkOutput();
                player.setItemInHand(interactionHand, new ItemStack(output.getItem(), 1));
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }
}
