package net.techtastic.tat.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IAltarSink {
    default boolean drawPowerFromAltar(Level level, BlockPos sink, BlockPos source, double amount) {
        if (level.getBlockState(source).getBlock() instanceof IAltarSource altar)
            return altar.drawPowerFromAltar(level, sink, source, amount);
        else if (level.getBlockEntity(source) instanceof IAltarSource altar)
            return altar.drawPowerFromAltar(level, sink, source, amount);
        return false;
    }
}
