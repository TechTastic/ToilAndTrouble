package net.techtastic.tat.block.custom;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.techtastic.tat.block.entity.ArthanaBlockEntity;
import net.techtastic.tat.block.entity.CastIronOvenBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ArthanaBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ArthanaBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(3, 0, 3, 13, 1, 13);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState()
                .setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
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
        builder.add(FACING);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        BlockEntity be = blockGetter.getBlockEntity(blockPos);
        System.err.println("BlockEntity: " + be);
        if (be instanceof ArthanaBlockEntity arthana) {
            ItemStack dagger = arthana.getArthana();
            System.err.println("ItemStack: " + dagger);
            return dagger;
        }

        return super.getCloneItemStack(blockGetter, blockPos, blockState);
    }

    public static void placeArthana(Level level, BlockPos pos, BlockState state, ItemStack stack) {
        level.setBlockAndUpdate(pos, state);

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ArthanaBlockEntity arthana) {
            arthana.setArthana(stack);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if ((be instanceof ArthanaBlockEntity arthana) && player.isCrouching() && player.getItemInHand(interactionHand).isEmpty()) {
                ItemStack stack = arthana.getArthana().copy();
                level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                player.setItemInHand(interactionHand, stack);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        if (level.isClientSide || !player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty())
            return;

        ItemStack stack = ItemStack.EMPTY;
        if (blockEntity instanceof ArthanaBlockEntity arthana)
            stack = arthana.getArthana().copy();

        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);

        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ArthanaBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
