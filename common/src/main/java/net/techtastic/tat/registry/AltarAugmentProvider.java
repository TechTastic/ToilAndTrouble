package net.techtastic.tat.registry;

import dev.architectury.core.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.techtastic.tat.util.IAltarAugment;

import java.util.Optional;

public class AltarAugmentProvider extends RegistryEntry<AltarAugmentProvider> {
    public static Optional<IAltarAugment> getAugment(Level level, BlockPos pos) {
        return Optional.empty();
    }
}
