package net.techtastic.tat.api.altar.augment;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Optional;

public interface IAltarAugmentProvider {
    default Optional<IAltarAugment> getAugment(Level level, BlockPos pos) {
        return Optional.empty();
    }
}
