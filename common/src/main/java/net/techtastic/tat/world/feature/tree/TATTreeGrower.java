package net.techtastic.tat.world.feature.tree;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.techtastic.tat.world.feature.TATConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TATTreeGrower extends AbstractTreeGrower {
    private final TreeType type;

    public TATTreeGrower(TreeType type) {
        this.type = type;
    }

    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean bl) {
        return switch (this.type) {
            case ROWAN -> TATConfiguredFeatures.ROWAN_TREE;
            case HAWTHORN -> TATConfiguredFeatures.HAWTHORN_TREE;
            case ALDER -> TATConfiguredFeatures.ALDER_TREE;
        };
    }

    public enum TreeType {
        ROWAN,
        HAWTHORN,
        ALDER;
    }
}
