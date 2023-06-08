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
import net.minecraft.world.item.context.UseOnContext;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KettleBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty NORTH_EXT = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH_EXT = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST_EXT = BlockStateProperties.EAST;
    public static final BooleanProperty WEST_EXT = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty FULL = BooleanProperty.create("full");


    public KettleBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Level level = blockPlaceContext.getLevel();
        BlockPos pos = blockPlaceContext.getClickedPos();
        Direction facing = blockPlaceContext.getNearestLookingDirection().getOpposite();

        return getNewState(level, pos, facing);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, NORTH, SOUTH, EAST, WEST, NORTH_EXT, SOUTH_EXT, EAST_EXT, WEST_EXT, UP, FULL);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return ;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return ToilAndTroubleExpectPlatform.getKettleBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
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

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);

        level.setBlockAndUpdate(blockPos, getNewState(level, blockPos, blockState.getValue(FACING)));
    }

    private BlockState getNewState(Level level, BlockPos pos, Direction facing) {
        BlockState state = this.defaultBlockState();
        BlockState north = level.getBlockState(pos.relative(Direction.NORTH));
        BlockState south = level.getBlockState(pos.relative(Direction.SOUTH));
        BlockState east = level.getBlockState(pos.relative(Direction.EAST));
        BlockState west = level.getBlockState(pos.relative(Direction.WEST));

        BlockState origState = level.getBlockState(pos);

        return state
                .setValue(FACING, facing)
                .setValue(UP, state.isFaceSturdy(level, pos.relative(Direction.UP), Direction.DOWN, SupportType.CENTER))
                .setValue(NORTH, state.isFaceSturdy(level, pos.relative(Direction.NORTH), Direction.SOUTH, SupportType.FULL))
                .setValue(SOUTH, state.isFaceSturdy(level, pos.relative(Direction.SOUTH), Direction.NORTH, SupportType.FULL))
                .setValue(EAST, state.isFaceSturdy(level, pos.relative(Direction.EAST), Direction.WEST, SupportType.FULL))
                .setValue(WEST, state.isFaceSturdy(level, pos.relative(Direction.WEST), Direction.EAST, SupportType.FULL))
                .setValue(NORTH_EXT, north.getBlock() instanceof WallBlock)
                .setValue(SOUTH_EXT, south.getBlock() instanceof WallBlock)
                .setValue(EAST_EXT, east.getBlock() instanceof WallBlock)
                .setValue(WEST_EXT, west.getBlock() instanceof WallBlock)
                .setValue(FULL, origState.hasProperty(FULL) ? origState.getValue(FULL) : false);
    }
}
