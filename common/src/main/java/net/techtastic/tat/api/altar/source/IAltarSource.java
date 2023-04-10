package net.techtastic.tat.api.altar.source;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IAltarSource {
    default double getRange() {
        return 0.0;
    }
    default void setRange(double newRange) {
    }

    default double getCurrentPower() {
        return 0.0;
    }
    default void setCurrentPower(double newPower) {
    }

    default double getMaxPower() {
        return 0.0;
    }
    default void setMaxPower(double newMaxPower) {
    }

    default double getRate() {
        return 0.0;
    }
    default void setRate(double newRate) {
    }

    boolean drawPowerFromAltar(Level level, BlockPos sink, BlockPos source, double amount);
}
