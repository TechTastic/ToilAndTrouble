package net.techtastic.tat.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.techtastic.tat.block.entity.CastIronOvenBlockEntity;

public interface IFumeFunnel {
    default double getChance() {
        return FunnelValues.chance;
    }
    default void setChance(double newChance) {
        FunnelValues.chance = newChance;
    }

    default boolean canUtilize(CastIronOvenBlockEntity oven, Level level, BlockPos funnelPos) {
        return false;
    }
}

class FunnelValues {
    public static double chance = 0.0;
}
