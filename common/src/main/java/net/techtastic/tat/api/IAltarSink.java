package net.techtastic.tat.api;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface IAltarSink {
    default boolean drawPowerFromAltar(double amount, BlockEntity be) {
        if (be instanceof IAltarSource altar) {
            return altar.drawPowerFromAltar(amount);
        }
        return false;
    }
}
