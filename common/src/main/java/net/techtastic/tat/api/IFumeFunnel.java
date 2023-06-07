package net.techtastic.tat.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.techtastic.tat.block.entity.CastIronOvenBlockEntity;

public interface IFumeFunnel {
    double getChance();
    default void setChance(double newChance) {}

    boolean canUtilize(CastIronOvenBlockEntity oven, Level level, BlockPos funnelPos);

}
