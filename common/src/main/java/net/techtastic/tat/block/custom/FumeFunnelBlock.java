package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.techtastic.tat.api.IFumeFunnel;
import net.techtastic.tat.block.entity.CastIronOvenBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FumeFunnelBlock extends Block implements IFumeFunnel {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public FumeFunnelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(TOP)) {
            return switch (blockState.getValue(FACING)) {
                case SOUTH -> Block.box(6, 0, 0, 10, 14, 4);
                case EAST -> Block.box(0, 0, 6, 4, 14, 10);
                case WEST -> Block.box(12, 0, 6, 16, 14, 10);
                default -> Block.box(6, 0, 12, 10, 14, 16);
            };
        }
        return Block.box(2, 0, 2, 14, 16, 14);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction initFacing = blockPlaceContext.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, initFacing)
                .setValue(LIT, isOvenLit(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos(), initFacing))
                .setValue(TOP, isTop(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos()));
    }

    private boolean isTop(Level level, BlockPos clickedPos) {
        BlockState bottomState = level.getBlockState(clickedPos.relative(Direction.DOWN));
        return bottomState.getBlock() instanceof CastIronOvenBlock;
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
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
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);

        System.err.println("BlockState: " + blockState);
        System.err.println("Level: " + level);
        System.err.println("BlockPos: " + blockPos);
        System.err.println("Block: " + block);
        System.err.println("BlockPos2: " + blockPos2);
        System.err.println("Boolean: " + bl);

        BlockState ovenState = level.getBlockState(blockPos2);
        System.err.println("OvenState: " + ovenState);
        if (ovenState.getBlock() instanceof CastIronOvenBlock) {
            if (ovenState.getValue(FACING).equals(blockState.getValue(FACING))) {
                System.err.println("Both Face the same Direction");
                System.err.println("Funnel is Lit: " + blockState.getValue(LIT));
                System.err.println("Oven is Lit: " + ovenState.getValue(LIT));
                level.setBlockAndUpdate(blockPos, blockState.setValue(LIT, ovenState.getValue(LIT)));
            } else
                level.setBlockAndUpdate(blockPos, blockState.setValue(LIT, false));
        } else
            level.setBlockAndUpdate(blockPos, blockState.setValue(LIT, false));
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);

        if (isTop(level, blockPos)) {
            level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(TOP, true));
            level.setBlockAndUpdate(blockPos.below(), level.getBlockState(blockPos.below()).setValue(TOP, true));
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getValue(TOP) && isTop(level, blockPos)) {
            level.setBlockAndUpdate(blockPos.below(), level.getBlockState(blockPos.below()).setValue(TOP, false));
        }

        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        HashMap<BlockState, BlockPos> list = getAllApplicableOvens(level, blockPos, blockState.getValue(FACING));
        if (!list.isEmpty()) {
            BlockState ovenState = list.keySet().stream().findFirst().get();
            return ovenState.getBlock().use(ovenState, level, list.get(ovenState), player, interactionHand, blockHitResult);

        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public double getChance() {
        return 25.0;
    }

    @Override
    public boolean canUtilize(CastIronOvenBlockEntity oven, Level level, BlockPos funnelPos) {
        return oven.getBlockState().getValue(FACING).equals(level.getBlockState(funnelPos).getValue(FACING));
    }

    public boolean isOvenLit(Level level, BlockPos pos, Direction initFacing) {
        HashMap<BlockState, BlockPos> list = getAllApplicableOvens(level, pos, initFacing);

        if (!list.isEmpty()) {
            for (BlockState ovenState : list.keySet()) {
                if (ovenState.getValue(LIT)) return true;
            }
        }

        return false;
    }

    public HashMap<BlockState, BlockPos> getAllApplicableOvens(Level level, BlockPos pos, Direction initFacing) {
        HashMap<BlockState, BlockPos> list = new HashMap<>(3);

        BlockPos leftPos = pos.relative(initFacing.getCounterClockWise());
        BlockState leftState = getOvenAt(level, leftPos);
        if (leftState != null && leftState.getValue(FACING).equals(initFacing)) list.put(leftState, leftPos);

        BlockPos rightPos = pos.relative(initFacing.getClockWise());
        BlockState rightState = getOvenAt(level, rightPos);
        if (rightState != null && rightState.getValue(FACING).equals(initFacing)) list.put(rightState, rightPos);

        BlockPos downPos = pos.relative(Direction.DOWN);
        BlockState downState = getOvenAt(level, downPos);
        if (downState != null && downState.getValue(FACING).equals(initFacing)) list.put(downState, downPos);

        return list;
    }

    public BlockState getOvenAt(Level level, BlockPos pos) {
        BlockState oven = level.getBlockState(pos);
        if (oven.getBlock() instanceof CastIronOvenBlock) return oven;
        return null;
    }
}
