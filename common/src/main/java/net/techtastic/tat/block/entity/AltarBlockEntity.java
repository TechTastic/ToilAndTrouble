package net.techtastic.tat.block.entity;

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
import net.techtastic.tat.api.altar.augment.AltarAugments;
import net.techtastic.tat.api.altar.augment.IAltarAugment;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.block.TATBlocks;
import net.techtastic.tat.dataloader.altar.nature.NatureBlocksDataResolver;
import net.techtastic.tat.screen.AltarMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class AltarBlockEntity extends BlockEntity implements ExtendedMenuProvider {
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
    private final HashMap<Block, Pair<Integer, Integer>> natureCount = new HashMap<>();
    private final HashMap<String, IAltarAugment> augmentList = new HashMap<>();
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
        compoundTag.putLong("ToilAndTrouble$masterPos", this.masterPos.asLong());

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

    public double getRange() {
        return this.altarRange;
    }
    public void setRange(double newRange) {
        this.altarRange = newRange;
        this.setChanged();
    }

    public double getCurrentPower() {
        return this.altarPower;
    }
    public void setCurrentPower(double newPower) {
        this.altarPower = newPower;
    }

    public double getMaxPower() {
        return this.maxAltarPower;
    }
    public void setMaxPower(double newMaxPower) {
        this.maxAltarPower = newMaxPower;
    }

    public double getRate() {
        return this.altarRate;
    }
    public void setRate(double newRate) {
        this.altarRate = newRate;
    }

    public boolean drawPowerFromAltar(Level level, BlockPos sink, BlockPos source, double amount) {
        if (!this.isMaster()) source = this.masterPos;

        double curr = this.getCurrentPower();

        if (curr < amount || sink.distSqr(source) > this.getRange())
            return false;

        this.setCurrentPower(curr - amount);
        return true;
    }

    private void populateNatureCount() {
        this.natureCount.clear();

        double range = getRange();
        assert level != null;
        Stream<BlockState> stream = level.getBlockStates(AABB.of(
                BoundingBox.fromCorners(getMasterPos().offset(-range, -range, -range), getMasterPos().offset(range, range, range))));
        stream.forEach((testState) -> {
            int power = this.nature.getNaturalPower(testState);
            int limit = this.nature.getMaxNaturalLimit(testState);
            if (power != 0 || limit != 0) {
                if (!this.natureCount.containsKey(testState.getBlock())) {
                    this.natureCount.put(testState.getBlock(), new Pair<>(1, power));
                } else if (this.natureCount.get(testState.getBlock()).getA() < limit) {
                    this.natureCount.put(testState.getBlock(), new Pair<>(this.natureCount.get(testState.getBlock()).getA() + 1, power));
                }
            }
        });
    }

    private void updateMaxAltarPowerFromNature() {
        if (this.natureCount.isEmpty()) {
            populateNatureCount();
        }

        AtomicReference<Double> natureMaxAltarPower = new AtomicReference<>(0.0);

        this.natureCount.forEach((block, naturePair) ->
            natureMaxAltarPower.updateAndGet(v -> v + naturePair.getA() * naturePair.getB())
        );

        setMaxPower(natureMaxAltarPower.get());
    }

    private void populateAugmentsList() {
        this.augmentList.clear();

        assert level != null;
        BlockPattern.BlockPatternMatch match = getOrCreateAltarShape().find(level, worldPosition);
        if (match == null) return;

        int height = match.getHeight();
        int width = match.getWidth();

        for (int i = 0; i <= width - 1; i++) {
            for (int j = 0; j <= height - 1; j++) {
                BlockInWorld biw = match.getBlock(i, j, 1);

                IAltarAugment augment = AltarAugments.testForAltarAugment(this.level, biw.getPos());
                if (augment == null) continue;

                if (this.augmentList.isEmpty()) {
                    this.augmentList.put(augment.getType(), augment);
                    continue;
                }

                if (!this.augmentList.containsKey(augment.getType())) {
                    this.augmentList.put(augment.getType(), augment);
                    continue;
                }

                if (this.augmentList.get(augment.getType()).getTypePriority() > augment.getTypePriority())
                    this.augmentList.put(augment.getType(), augment);
            }
        }
    }

    private void updateRange() {
        double baseRange = 16;

        for (IAltarAugment augment : this.augmentList.values()) {
            baseRange = augment.boostAltarRange(baseRange);
        }
        for (IAltarAugment augment : this.augmentList.values()) {
            baseRange = augment.modifyAltarRange(baseRange);
        }

        setRange(baseRange);
    }

    private void updateRate() {
        double baseRate = 1;

        for (IAltarAugment augment : this.augmentList.values()) {
            baseRate = augment.boostAltarRechargeRate(baseRate);
        }
        for (IAltarAugment augment : this.augmentList.values()) {
            baseRate = augment.modifyAltarRechargeRate(baseRate);
        }

        setRate(baseRate);
    }

    private void updateMaxPower() {
        updateMaxAltarPowerFromNature();

        double basePower = getMaxPower();

        for (IAltarAugment augment : this.augmentList.values()) {
            basePower = augment.boostMaxAltarPower(basePower);
        }
        for (IAltarAugment augment : this.augmentList.values()) {
            basePower = augment.modifyMaxAltarPower(basePower);
        }

        setMaxPower(basePower);
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
        buf.writeBlockPos(this.worldPosition);
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
