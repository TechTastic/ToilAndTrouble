package net.techtastic.tat.api.altar.source;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Optional;

public interface IAltarSourceProvider {
    default Optional<IAltarSource> getAltarSource(Level level, BlockPos pos) {
        return Optional.empty();
    }
}
