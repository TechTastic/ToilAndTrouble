package net.techtastic.tat.block.custom;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.techtastic.tat.TATBlockEntities;
import net.techtastic.tat.blockentity.CastIronOvenBlockEntity;
import org.jetbrains.annotations.Nullable;

public class CastIronOvenBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public CastIronOvenBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(TOP)) {
            return switch (blockState.getValue(FACING)) {
                case SOUTH -> Shapes.join(Block.box(1, 0, 1, 15, 11, 16), Block.box(6, 10, 0, 10, 16, 4), BooleanOp.OR);
                case WEST -> Shapes.join(Block.box(0, 0, 1, 15, 11, 15), Block.box(12, 10, 6, 16, 16, 10), BooleanOp.OR);
                case EAST -> Shapes.join(Block.box(1, 0, 1, 16, 11, 15), Block.box(0, 10, 6, 4, 16, 10), BooleanOp.OR);
                default -> Shapes.join(Block.box(1, 0, 0, 15, 11, 15), Block.box(6, 10, 12, 10, 16, 16), BooleanOp.OR);
            };
        }

        return switch (blockState.getValue(FACING)) {
            case SOUTH -> Shapes.join(Block.box(1, 0, 1, 15, 11, 16), Block.box(7, 11, 0, 9, 14, 3), BooleanOp.OR);
            case WEST -> Shapes.join(Block.box(0, 0, 1, 15, 11, 15), Block.box(13, 11, 7, 16, 14, 9), BooleanOp.OR);
            case EAST -> Shapes.join(Block.box(1, 0, 1, 16, 11, 15), Block.box(0, 11, 7, 3, 14, 9), BooleanOp.OR);
            default -> Shapes.join(Block.box(1, 0, 0, 15, 11, 15), Block.box(7, 11, 13, 9, 14, 16), BooleanOp.OR);
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState()
                .setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
                .setValue(LIT, false)
                .setValue(TOP, hasTop(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos()));
    }

    private boolean hasTop(Level level, BlockPos clickedPos) {
        BlockState upState = level.getBlockState(clickedPos.relative(Direction.UP));
        return upState.getBlock() instanceof FumeFunnelBlock;
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
        builder.add(FACING).add(LIT).add(TOP);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);

        if (hasTop(level, blockPos)) {
            BlockState funnel = level.getBlockState(blockPos.above());
            BlockState oven = level.getBlockState(blockPos);
            if (funnel.getValue(FACING).equals(oven.getValue(FACING))) {
                level.setBlockAndUpdate(blockPos, oven.setValue(TOP, true));
                level.setBlockAndUpdate(blockPos.above(), funnel.setValue(TOP, true));
            }
        }
    }

    // BLOCK ENTITY

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CastIronOvenBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, TATBlockEntities.CAST_IRON_OVEN_BLOCK_ENTITY.get(), CastIronOvenBlockEntity::tick);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getBlock() != blockState2.getBlock()) {
            if (level.getBlockEntity(blockPos) instanceof CastIronOvenBlockEntity cio) {
                cio.drops();
            }
        }

        if (blockState.getValue(TOP) && hasTop(level, blockPos)) {
            level.setBlockAndUpdate(blockPos.above(), level.getBlockState(blockPos.above()).setValue(TOP, false));
        }

        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof CastIronOvenBlockEntity) {
                MenuProvider menuProvider = this.getMenuProvider(blockState, level, blockPos);
                if (menuProvider != null) {
                    MenuRegistry.openExtendedMenu((ServerPlayer) player, (ExtendedMenuProvider) menuProvider);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
