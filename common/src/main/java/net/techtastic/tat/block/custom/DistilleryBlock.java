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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.block.entity.CastIronOvenBlockEntity;
import net.techtastic.tat.block.entity.DistilleryBlockEntity;
import org.jetbrains.annotations.Nullable;

public class DistilleryBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final IntegerProperty JARS = IntegerProperty.create("jars", 0,4);

    public DistilleryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(FACING)) {
            case SOUTH -> Block.box(2, 0, 2, 14, 11, 15);
            case WEST -> Block.box(1, 0, 2, 14, 11, 14);
            case EAST -> Block.box(2, 0, 2, 15, 11, 14);
            default -> Block.box(2, 0, 1, 14, 11, 14);
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState()
                .setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
                .setValue(POWERED, false)
                .setValue(JARS, 0);
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
        builder.add(FACING).add(POWERED).add(JARS);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getBlock() != blockState2.getBlock()) {
            if (level.getBlockEntity(blockPos) instanceof DistilleryBlockEntity cio) {
                cio.drops();
            }
        }

        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof DistilleryBlockEntity) {
                MenuProvider menuProvider = this.getMenuProvider(blockState, level, blockPos);
                if (menuProvider != null) {
                    MenuRegistry.openExtendedMenu((ServerPlayer) player, (ExtendedMenuProvider) menuProvider);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DistilleryBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, TATBlockEntities.DISTILLERY_BLOCK_ENTITY.get(), DistilleryBlockEntity::tick);
    }
}
