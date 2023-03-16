package net.techtastic.tat.world.feature.tree;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.techtastic.tat.world.feature.TATConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class HawthornTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean bl) {
        return TATConfiguredFeatures.HAWTHORN_TREE;
    }
}
