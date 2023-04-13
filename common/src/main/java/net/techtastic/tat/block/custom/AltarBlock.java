package net.techtastic.tat.block.custom;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.block.entity.AltarBlockEntity;
import net.techtastic.tat.item.TATItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AltarBlock extends BaseEntityBlock {
    public static final BooleanProperty MULTIBLOCK = BooleanProperty.create("multiblock");

    public AltarBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(MULTIBLOCK, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MULTIBLOCK);
    }

    @Override
    public InteractionResult use(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (!level.isClientSide() && player.getItemInHand(interactionHand).isEmpty()) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof AltarBlockEntity altar) {
                if (altar.getMasterPos() == null)
                    setupAltarMultiblock(altar, level, blockPos);

                if (blockState.getValue(MULTIBLOCK)) {
                    MenuProvider menuProvider = this.getMenuProvider(blockState, level, altar.getMasterPos());
                    if (menuProvider != null)
                        MenuRegistry.openExtendedMenu((ServerPlayer) player, (ExtendedMenuProvider) menuProvider);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);

    }

    @Override
    public void onPlace(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);

        if (level.isClientSide) return;

        AltarBlockEntity altar = (AltarBlockEntity) level.getBlockEntity(blockPos);
        assert altar != null;
        BlockPattern.BlockPatternMatch match = altar.getOrCreateAltarShapeWithBoundaries().find(level, blockPos);
        if (match != null) {
            match = altar.getOrCreateAltarShape().find(level, blockPos);
            if (match != null) {
                int height = match.getHeight();
                int width = match.getWidth();

                BlockInWorld centerBlock = match.getBlock((width - 1) / 2, (height - 1) / 2, match.getDepth() - 1);
                ((AltarBlockEntity) Objects.requireNonNull(centerBlock.getEntity())).isMaster(true);

                for (int i = 0; i <= width - 1; i++) {
                    for (int j = 0; j <= height - 1; j++) {
                        for (int k = 0; k <= match.getDepth() - 1; k++) {
                            BlockInWorld biw = match.getBlock(i, j, k);

                            ((AltarBlockEntity) Objects.requireNonNull(biw.getEntity())).setMasterPos(centerBlock.getPos());

                            level.setBlockAndUpdate(biw.getPos(), biw.getState().setValue(MULTIBLOCK, true));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRemove(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState2, boolean bl) {
        if (blockState.getValue(MULTIBLOCK)) {
            AltarBlockEntity altar = (AltarBlockEntity) level.getBlockEntity(blockPos);
            assert altar != null;
            BlockPattern.BlockPatternMatch match = altar.isAltarBroken(level, blockPos);
            if (match != null) {
                int height = match.getHeight();
                int width = match.getWidth();

                for (int i = 0; i <= width - 1; i++) {
                    for (int j = 0; j <= height - 1; j++) {
                        for (int k = 0; k <= match.getDepth() - 1; k++) {
                            BlockInWorld biw = match.getBlock(i, j, k);
                            if (biw.getState().getBlock() instanceof AltarBlock) {
                                AltarBlockEntity curAltar = ((AltarBlockEntity) Objects.requireNonNull(biw.getEntity()));
                                curAltar.setMasterPos(null);
                                curAltar.isMaster(false);

                                level.setBlockAndUpdate(biw.getPos(), biw.getState().setValue(MULTIBLOCK, false));
                            }
                        }
                    }
                }
            }
        } else {
            AltarBlockEntity altar = (AltarBlockEntity) level.getBlockEntity(blockPos);
            assert altar != null;
            BlockPattern.BlockPatternMatch match = altar.getOrCreateAltarShapeWithBoundaries().find(level, blockPos);
            if (match != null) {
                BlockPos pos = match.getBlock(2, 2, match.getDepth() - 1).getPos();
                match = altar.getOrCreateAltarShape().find(level, pos);
                if (match != null) {
                    int height = match.getHeight();
                    int width = match.getWidth();

                    BlockInWorld centerBlock = match.getBlock((width - 1) / 2, (height - 1) / 2, match.getDepth() - 1);
                    ((AltarBlockEntity) Objects.requireNonNull(centerBlock.getEntity())).isMaster(true);

                    for (int i = 0; i <= width - 1; i++) {
                        for (int j = 0; j <= height - 1; j++) {
                            for (int k = 0; k <= match.getDepth() - 1; k++) {
                                BlockInWorld biw = match.getBlock(i, j, k);
                                if (biw.getState().getBlock() instanceof AltarBlock) {
                                    if (!biw.getState().getValue(MULTIBLOCK)) {
                                        level.setBlockAndUpdate(biw.getPos(), biw.getState().setValue(MULTIBLOCK, true));
                                    }

                                    AltarBlockEntity curAltar = (AltarBlockEntity) biw.getEntity();
                                    assert curAltar != null;
                                    if (curAltar.getMasterPos() == null) {
                                        curAltar.setMasterPos(centerBlock.getPos());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public void neighborChanged(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Block block, @NotNull BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);

        BlockState altarState = level.getBlockState(blockPos);
        if (!(altarState.getBlock() instanceof AltarBlock)) return;
        if (!altarState.getValue(MULTIBLOCK)) return;

        AltarBlockEntity altar = (AltarBlockEntity) level.getBlockEntity(blockPos);

        assert altar != null;
        BlockPattern.BlockPatternMatch matchWithBoundaries = altar.getOrCreateAltarShapeWithBoundaries().find(level, blockPos);
        BlockPattern.BlockPatternMatch matchWithoutBoundaries = altar.getOrCreateAltarShape().find(level, blockPos);
        if (matchWithBoundaries != null) return;
        if (matchWithoutBoundaries == null) return;

        int height = matchWithoutBoundaries.getHeight();
        int width = matchWithoutBoundaries.getWidth();

        for (int i = 0; i <= width - 1; i++) {
            for (int j = 0; j <= height - 1; j++) {
                for (int k = 0; k <= matchWithoutBoundaries.getDepth() - 1; k++) {
                    BlockInWorld biw = matchWithoutBoundaries.getBlock(i, j, k);

                    AltarBlockEntity curAltar = ((AltarBlockEntity) Objects.requireNonNull(biw.getEntity()));
                    curAltar.setMasterPos(null);
                    curAltar.isMaster(false);

                    level.setBlockAndUpdate(biw.getPos(), biw.getState().setValue(MULTIBLOCK, false));
                }
            }
        }
    }

    // BLOCK ENTITY

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new AltarBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, TATBlockEntities.ALTAR_BLOCK_ENTITY.get(), AltarBlockEntity::tick);
    }

    public void setupAltarMultiblock(AltarBlockEntity altar, Level level, BlockPos blockPos) {
        BlockPattern.BlockPatternMatch match = altar.getOrCreateAltarShapeWithBoundaries().find(level, blockPos);
        if (match != null) {
            match = altar.getOrCreateAltarShape().find(level, blockPos);
            if (match != null) {
                int height = match.getHeight();
                int width = match.getWidth();

                BlockInWorld centerBlock = match.getBlock((width - 1) / 2, (height - 1) / 2, match.getDepth() - 1);
                ((AltarBlockEntity) Objects.requireNonNull(centerBlock.getEntity())).isMaster(true);

                for (int i = 0; i <= width - 1; i++) {
                    for (int j = 0; j <= height - 1; j++) {
                        for (int k = 0; k <= match.getDepth() - 1; k++) {
                            BlockInWorld biw = match.getBlock(i, j, k);

                            ((AltarBlockEntity) Objects.requireNonNull(biw.getEntity())).setMasterPos(centerBlock.getPos());

                            level.setBlockAndUpdate(biw.getPos(), biw.getState().setValue(MULTIBLOCK, true));
                        }
                    }
                }
            }
        }
    }
}
