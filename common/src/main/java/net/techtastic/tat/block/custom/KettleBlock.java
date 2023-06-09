package net.techtastic.tat.block.custom;

import com.mojang.authlib.minecraft.TelemetrySession;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.Containers;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.techtastic.tat.ToilAndTroubleExpectPlatform;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.block.entity.DistilleryBlockEntity;
import net.techtastic.tat.block.entity.KettleBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

import java.util.Random;

public class KettleBlock extends BaseEntityBlock {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty NORTH_EXT = BooleanProperty.create("north_ext");
    public static final BooleanProperty SOUTH_EXT = BooleanProperty.create("south_ext");
    public static final BooleanProperty EAST_EXT = BooleanProperty.create("east_ext");
    public static final BooleanProperty WEST_EXT = BooleanProperty.create("west_ext");
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

        return getNewState(level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, NORTH_EXT, SOUTH_EXT, EAST_EXT, WEST_EXT, UP);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape = Block.box(4, 1, 4, 12, 6, 12);

        if (blockState.getValue(UP))
            shape = Shapes.join(shape, Block.box(4, 6, 4, 12, 16, 12), BooleanOp.OR);
        if (blockState.getValue(NORTH))
            shape = Shapes.join(shape, Block.box(4, 6, 0, 12, 16, 4), BooleanOp.OR);
        if (blockState.getValue(SOUTH))
            shape = Shapes.join(shape, Block.box(4, 6, 12, 12, 16, 16), BooleanOp.OR);
        if (blockState.getValue(EAST))
            shape = Shapes.join(shape, Block.box(12, 6, 4, 16, 16, 12), BooleanOp.OR);
        if (blockState.getValue(WEST))
            shape = Shapes.join(shape, Block.box(0, 6, 4, 4, 16, 12), BooleanOp.OR);

        return shape.optimize();
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

        boolean openSlots = kettle.inventory.stream().anyMatch(ItemStack::isEmpty);
        boolean full = kettle.hasEnoughFluid(kettle);
        boolean nextIng = kettle.testForNextIngredient(level, test);

        System.err.println("Any Empty Slots? " + openSlots);
        System.err.println("Full Tank? " + full);
        System.err.println("Is Correct Next Ingredient? " + nextIng);

        System.err.println("Does this Check Pass? " +
                (!openSlots || full || nextIng)
        );

        if (!openSlots || full || nextIng)
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
        if (level.isClientSide)
            return InteractionResult.sidedSuccess(level.isClientSide);

        ItemStack stack = player.getItemInHand(interactionHand);

        BlockEntity be = level.getBlockEntity(blockPos);
        if (!(be instanceof KettleBlockEntity kettle))
            return InteractionResult.FAIL;

        if (stack.is(Items.WATER_BUCKET)) {
            if (!kettle.tryInsertFluid(kettle, stack))
                return InteractionResult.FAIL;

            player.setItemInHand(interactionHand, new ItemStack(Items.BUCKET));

            return InteractionResult.SUCCESS;
        } else if (stack.is(Items.BUCKET)) {
            if (!kettle.tryExtractFluid(kettle, stack))
                return InteractionResult.FAIL;

            player.setItemInHand(interactionHand, new ItemStack(Items.WATER_BUCKET));

            return InteractionResult.SUCCESS;
        } else if (stack.is(Items.GLASS_BOTTLE)) {
            for (ItemStack temp : kettle.inventory) {
                System.err.println("Item at " + kettle.inventory.indexOf(temp) + " is " + temp.getItem());
            }

            ItemStack output = kettle.getOrCreateOutput(player);
            if (output.isEmpty())
                return InteractionResult.FAIL;

            ItemStack newStack = new ItemStack(output.getItem(), 1);
            kettle.shrinkOrRemoveOutput();

            if (stack.getCount() == 1) {
                player.setItemInHand(interactionHand, newStack);
            } else {
                if (player.getInventory().getFreeSlot() != -1 && player.getInventory().getSlotWithRemainingSpace(newStack) != -1)
                    player.getInventory().add(newStack);
                else {
                    ItemEntity drop = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), newStack);
                    drop.moveTo(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
                    level.addFreshEntity(drop);
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);

        level.setBlockAndUpdate(blockPos, getNewState(level, blockPos));
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
        super.animateTick(blockState, level, blockPos, random);

        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof KettleBlockEntity kettle) {
            if (KettleBlockEntity.hasRecipe(kettle))
                level.addParticle(
                        ParticleTypes.ELECTRIC_SPARK,
                        blockPos.getX() + 0.5,
                        blockPos.getY() + 6f/16f,
                        blockPos.getZ() + 0.5,
                        0.0,
                        1.0,
                        0.0
                );
            if (kettle.isHeated)
                level.addParticle(
                        ParticleTypes.BUBBLE_POP,
                        blockPos.getX() + random.nextDouble(0.25, 0.75),
                        blockPos.getY() + 0.7,
                        blockPos.getZ() + random.nextDouble(0.25, 0.75),
                        0.0,
                        0.1,
                        0.0
                );
        }
    }

    private boolean connectedTo(Level level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos.relative(direction));
        return (!isExceptionForConnection(state) &&
                state.isFaceSturdy(level, pos, direction.getOpposite())) ||
                isWall(state);
    }

    private boolean isWall(BlockState state) {
        return state.getBlock() instanceof WallBlock;
    }

    private BlockState getNewState(Level level, BlockPos pos) {
        BlockState state = this.defaultBlockState();

        boolean north = connectedTo(level, pos, Direction.NORTH);
        boolean south = connectedTo(level, pos, Direction.SOUTH);
        boolean east = connectedTo(level, pos, Direction.EAST);
        boolean west = connectedTo(level, pos, Direction.WEST);

        return state
                .setValue(NORTH, north)
                .setValue(SOUTH, south)
                .setValue(EAST, east)
                .setValue(WEST, west)
                .setValue(UP, connectedTo(level, pos, Direction.UP) ||
                                north || south || east || west)

                .setValue(NORTH_EXT, isWall(level.getBlockState(pos.relative(Direction.NORTH))))
                .setValue(SOUTH_EXT, isWall(level.getBlockState(pos.relative(Direction.SOUTH))))
                .setValue(EAST_EXT, isWall(level.getBlockState(pos.relative(Direction.EAST))))
                .setValue(WEST_EXT, isWall(level.getBlockState(pos.relative(Direction.WEST))));
    }
}
