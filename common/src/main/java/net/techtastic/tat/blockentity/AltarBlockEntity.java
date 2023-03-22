package net.techtastic.tat.blockentity;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.techtastic.tat.TATBlockEntities;
import net.techtastic.tat.TATBlocks;
import net.techtastic.tat.api.IAltarSource;
import net.techtastic.tat.dataloader.altar.augment.AltarAugmentBlocksInfo;
import net.techtastic.tat.dataloader.altar.augment.AltarAugmentDataResolver;
import net.techtastic.tat.dataloader.altar.augment.AugmentType;
import net.techtastic.tat.dataloader.altar.nature.NatureBlocksDataResolver;
import net.techtastic.tat.screen.AltarMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class AltarBlockEntity extends BlockEntity implements IAltarSource, ExtendedMenuProvider {
    private double altarPower = 0;
    private double maxAltarPower = 0;
    private double altarRange = 16;
    private double altarRate = 1;
    private boolean isMaster = false;
    private BlockPos masterPos;
    private BlockPattern altarShape;
    private BlockPattern innerAltarShape;
    private int ticks = 0;
    private final NatureBlocksDataResolver nature = new NatureBlocksDataResolver();
    private final AltarAugmentDataResolver augment = new AltarAugmentDataResolver();
    private final HashMap<Block, Pair<Integer, Integer>> natureCount = new HashMap<>();
    private final List<BlockState> augmentList = new ArrayList<>();
    private final ContainerData data;

    public AltarBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TATBlockEntities.ALTAR_BLOCK_ENTITY.get(), blockPos, blockState);

        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> (int) AltarBlockEntity.this.getCurrentPower();
                    case 1 -> (int) AltarBlockEntity.this.getMaxPower();
                    case 2 -> (int) AltarBlockEntity.this.getRate();
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int j) {
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putDouble("ToilAndTrouble$altarPower", this.altarPower);
        compoundTag.putDouble("ToilAndTrouble$maxAltarPower", this.maxAltarPower);
        compoundTag.putDouble("ToilAndTrouble$altarRange", this.altarRange);
        compoundTag.putDouble("ToilAndTrouble$altarRate", this.altarRate);
        compoundTag.putBoolean("ToilAndTrouble$isMaster", this.isMaster);

        if (this.masterPos != null) {
            compoundTag.putLong("ToilAndTrouble$masterPos", this.masterPos.asLong());
        }

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);

        this.altarPower = compoundTag.getDouble("ToilAndTrouble$altarPower");
        this.maxAltarPower = compoundTag.getDouble("ToilAndTrouble$maxAltarPower");
        this.altarRange = compoundTag.getDouble("ToilAndTrouble$altarRange");
        this.altarRate = compoundTag.getDouble("ToilAndTrouble$altarRate");
        this.isMaster = compoundTag.getBoolean("ToilAndTrouble$isMaster");
        if (compoundTag.contains("ToilAndTrouble$masterPos"))
            this.masterPos = BlockPos.of(compoundTag.getLong("ToilAndTrouble$masterPos"));
    }

    public static <E extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, AltarBlockEntity entity) {
        if (!entity.isMaster) return;

        if (entity.getTicks() % 5 == 0) {
            entity.populateAugmentsList();
            entity.updateRange();

            entity.populateNatureCount();
            entity.updateMaxAltarPowerFromNature();
            entity.updateMaxPower();
            entity.updateCurrentPower();

            entity.updateRate();

            entity.resetTicks();
        }

        entity.incrementTicks();
    }

    public BlockPattern getOrCreateAltarShapeWithBoundaries() {
        if (altarShape == null) {
            altarShape = BlockPatternBuilder.start().aisle("X???X", "?###?", "?###?", "X???X")
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(TATBlocks.ALTAR.get())))
                    .where('?', BlockInWorld.hasState(BlockStatePredicate.ANY.and(BlockStatePredicate.forBlock(TATBlocks.ALTAR.get()).negate())))
                    .where('X', BlockInWorld.hasState(BlockStatePredicate.ANY))
                    .build();
        }

        return altarShape;
    }

    public BlockPattern getOrCreateAltarShape() {
        if (innerAltarShape == null) {
            innerAltarShape = BlockPatternBuilder.start().aisle("###", "###")
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(TATBlocks.ALTAR.get())))
                    .build();
        }

        return innerAltarShape;
    }

    public BlockPattern.BlockPatternMatch isAltarBroken(Level level, BlockPos pos) {
        BlockPattern.BlockPatternMatch match = BlockPatternBuilder.start().aisle("?##", "###")
                .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(TATBlocks.ALTAR.get())))
                .where('?', BlockInWorld.hasState(BlockStatePredicate.forBlock(TATBlocks.ALTAR.get()).negate()))
                .build().find(level, pos);

        if (match == null) {
            match = BlockPatternBuilder.start().aisle("#?#", "###")
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(TATBlocks.ALTAR.get())))
                    .where('?', BlockInWorld.hasState(BlockStatePredicate.forBlock(TATBlocks.ALTAR.get()).negate()))
                    .build().find(level, pos);
        }

        return match;
    }

    private void incrementTicks() {
        ticks++;
    }

    private void resetTicks() {
        ticks = 0;
    }

    private int getTicks() {
        return ticks;
    }

    public void isMaster(boolean bool) {
        this.isMaster = bool;
        this.setChanged();
    }
    public boolean isMaster() {
        return this.isMaster;
    }

    public void setMasterPos(BlockPos pos) {
        this.masterPos = pos;
        this.setChanged();
    }
    public BlockPos getMasterPos() {
        return this.masterPos;
    }

    @Override
    public double getRange() {
        return this.altarRange;
    }
    @Override
    public void setRange(double newRange) {
        this.altarRange = newRange;
        this.setChanged();
    }

    @Override
    public double getCurrentPower() {
        return this.altarPower;
    }
    @Override
    public void setCurrentPower(double newPower) {
        this.altarPower = newPower;
    }

    @Override
    public double getMaxPower() {
        return this.maxAltarPower;
    }

    @Override
    public void setMaxPower(double newMaxPower) {
        this.maxAltarPower = newMaxPower;
    }

    @Override
    public double getRate() {
        return this.altarRate;
    }
    @Override
    public void setRate(double newRate) {
        this.altarRate = newRate;
    }

    @Override
    public boolean drawPowerFromAltar(double amount) {
        return IAltarSource.super.drawPowerFromAltar(amount);
    }

    private void populateNatureCount() {
        natureCount.clear();

        double range = getRange();
        assert level != null;
        Stream<BlockState> stream = level.getBlockStates(AABB.of(
                BoundingBox.fromCorners(getMasterPos().offset(-range, -range, -range), getMasterPos().offset(range, range, range))));
        stream.forEach((testState) -> {
            int power = nature.getNaturalPower(testState);
            int limit = nature.getMaxNaturalLimit(testState);
            if (power != 0 || limit != 0) {
                if (!natureCount.containsKey(testState.getBlock())) {
                    natureCount.put(testState.getBlock(), new Pair<>(1, power));
                } else if (natureCount.get(testState.getBlock()).getA() < limit) {
                    natureCount.put(testState.getBlock(), new Pair<>(natureCount.get(testState.getBlock()).getA() + 1, power));
                }
            }
        });
    }

    private void updateMaxAltarPowerFromNature() {
        if (natureCount.isEmpty()) {
            populateNatureCount();
        }

        AtomicReference<Double> natureMaxAltarPower = new AtomicReference<>(0.0);

        natureCount.forEach((block, naturePair) ->
            natureMaxAltarPower.updateAndGet(v -> v + naturePair.getA() * naturePair.getB())
        );

        setMaxPower(natureMaxAltarPower.get());
    }

    private void populateAugmentsList() {
        augmentList.clear();

        assert level != null;
        BlockPattern.BlockPatternMatch match = getOrCreateAltarShape().find(level, worldPosition);
        if (match == null) return;

        int height = match.getHeight();
        int width = match.getWidth();

        for (int i = 0; i <= width - 1; i++) {
            for (int j = 0; j <= height - 1; j++) {
                BlockInWorld biw = match.getBlock(i, j, 1);

                System.err.println(biw.getState());
                System.err.println(augment.hasInfo(biw.getState()));
                System.err.println(augment.getInfo(biw.getState()));
                System.err.println(augment.getAugmentType(biw.getState()));

                if (!augment.hasInfo(biw.getState())) continue;

                System.err.println("This block has info!");

                if (augmentList.isEmpty()) {
                    System.err.println("List is Empty!");

                    augmentList.add(biw.getState());
                    continue;
                }

                AltarAugmentBlocksInfo newInfo = augment.getInfo(biw.getState());
                for (BlockState state : augmentList) {
                    System.err.println("Testing Info Types...");

                    AltarAugmentBlocksInfo info = augment.getInfo(state);
                    if (info.type() == newInfo.type()) {
                        System.err.println("Rivaling Types!");

                        if (info.type() == AugmentType.NONE) {
                            System.err.println("JK, it was a NONE");

                            augmentList.add(biw.getState());
                            continue;
                        }

                        if (newInfo.typePriority() < info.typePriority()) {
                            System.err.println("New Info has higher priority!");

                            augmentList.remove(state);
                            augmentList.add(biw.getState());
                        }
                    }
                }
            }
        }
    }

    private void updateRange() {
        if (augmentList.isEmpty()) {
            populateAugmentsList();
        }

        for (BlockState state : augmentList) {
            setRange(augment.modifyRange(state, getRange()));
        }
    }

    private void updateRate() {
        if (augmentList.isEmpty()) {
            populateAugmentsList();
        }

        double baseRate = 1;

        for (BlockState state : augmentList) {
            baseRate = augment.modifyRate(state, baseRate);

            System.err.println("New Rate: " + baseRate);
        }

        setRate(baseRate);

        System.err.println("Total Rate: " + getRate());
    }

    private void updateMaxPower() {
        if (augmentList.isEmpty()) {
            populateAugmentsList();
        }

        for (BlockState state : augmentList) {
            setMaxPower(augment.modifyPower(state, getMaxPower()));
        }
    }

    private void updateCurrentPower() {
        double curr = getCurrentPower();
        double max = getMaxPower();

        if (curr >= max) {
            setCurrentPower(max);
            return;
        }
        double newCurr = curr + (10 * getRate());
        if (newCurr >= max) {
            setCurrentPower(max);
            return;
        }

        setCurrentPower(newCurr);
    }

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(worldPosition);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.tat.altar");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new AltarMenu(i, inventory, this, this.data);
    }
}
