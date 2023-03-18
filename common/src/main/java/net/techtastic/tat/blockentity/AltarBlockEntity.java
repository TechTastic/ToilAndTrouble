package net.techtastic.tat.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.techtastic.tat.api.IAltarAugment;
import net.techtastic.tat.api.IAltarSource;
import net.techtastic.tat.dataloader.altar.nature.NatureBlocksDataResolver;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class AltarBlockEntity extends BlockEntity implements IAltarSource {
    private double altarPower = 0;
    private double maxAltarPower = 0;
    private double altarRange = 16;
    private double altarRate = 0;
    private boolean isMaster = false;
    private BlockPos masterPos;
    private BlockPattern altarShape;
    private BlockPattern innerAltarShape;
    private int ticks = 0;
    private NatureBlocksDataResolver nature = new NatureBlocksDataResolver();
    private HashMap<Block, Pair<Integer, Integer>> natureCount = new HashMap<>();
    private List<IAltarAugment> altarAugments = new ArrayList<>();

    public AltarBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TATBlockEntities.ALTAR_BLOCK_ENTITY.get(), blockPos, blockState);
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
            entity.populateNatureCount(level, blockPos, entity);
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

    public void incrementTicks() {
        ticks++;
    }

    public void resetTicks() {
        ticks = 0;
    }

    public int getTicks() {
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

    public HashMap<Block, Pair<Integer, Integer>> getNatureCount() {
        return this.natureCount;
    }

    public void setNatureCount(HashMap<Block, Pair<Integer, Integer>> newNatureCount) {
        this.natureCount = newNatureCount;
    }

    public void populateNatureCount(Level level, BlockPos altarPos, AltarBlockEntity altar) {
        natureCount.clear();

        double range = altar.getRange();
        Stream<BlockState> stream = level.getBlockStates(AABB.of(
                BoundingBox.fromCorners(altarPos.offset(-range, -range, -range), altarPos.offset(range, range, range))));
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

    public double getMaxAltarPowerFromNature(Level level, BlockPos altarPos, AltarBlockEntity altar) {
        HashMap<Block, Pair<Integer, Integer>> natureCount = altar.getNatureCount();

        if (natureCount.isEmpty()) {
            altar.populateNatureCount(level, altarPos, altar);
            natureCount = altar.getNatureCount();
        }

        AtomicReference<Double> natureMaxAltarPower = new AtomicReference<>(0.0);

        natureCount.forEach((block, naturePair) ->
            natureMaxAltarPower.updateAndGet(v -> v + naturePair.getA() * naturePair.getB())
        );

        return natureMaxAltarPower.get();
    }

    /*public List<IAltarAugment> getAltarAugments() {
        return this.altarAugments;
    }

    public void setAltarAugments(List<IAltarAugment> newAltarAugments) {
        this.altarAugments = newAltarAugments;
    }

    public static void populateAltarAugments(Level level, BlockPos altarPos, AltarBlockEntity altar) {
        List<IAltarAugment> list = new ArrayList<>();

        BlockPattern.BlockPatternMatch match = altar.getOrCreateAltarShape().find(level, altarPos);
        if (match == null) return;

        int height = match.getHeight();
        int width = match.getWidth();

        for (int i = 0; i <= width - 1; i++) {
            for (int j = 0; j <= height - 1; j++) {
                BlockInWorld biw = match.getBlock(i, j, 1);

                IAltarAugment augment = null;
                if (biw.getState().getBlock() instanceof IAltarAugment newAugment) {
                    augment = newAugment;
                } else if (biw.getEntity() instanceof  IAltarAugment newAugment) {
                    augment = newAugment;
                }

                if (augment == null) continue;

                if (!list.isEmpty() && augment.isOverridenByType() && !augment.getType().equals("none")) {
                    for (IAltarAugment aug : list) {
                        if (aug.getType().equals(augment.getType()) && augment.getPriority() < aug.getPriority()) {
                            list.set(list.indexOf(aug), augment);
                        }
                    }
                } else {
                    list.add(augment);
                }
            }
        }

        altar.setAltarAugments(list);
    }*/
}
