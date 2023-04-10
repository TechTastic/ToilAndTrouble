package net.techtastic.tat.api.altar.augment;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AltarAugments {
    private static final List<IAltarAugmentProvider> providers = new ArrayList<>();

    public static void registerAltarAugmentProvider(IAltarAugmentProvider provider) {
        providers.add(provider);
    }

    public static IAltarAugment testForAltarAugment(Level level, BlockPos pos) {
        for (IAltarAugmentProvider provider : providers) {
            Optional<IAltarAugment> optAugment = provider.getAugment(level, pos);
            if (optAugment.isPresent()) return optAugment.get();
        }

        return null;
    }
}
