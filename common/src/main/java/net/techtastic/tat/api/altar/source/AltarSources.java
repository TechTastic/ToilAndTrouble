package net.techtastic.tat.api.altar.source;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AltarSources {
    private static final List<IAltarSourceProvider> providers = new ArrayList<>();

    public static void registerAltarSourceProvider(IAltarSourceProvider provider) {
        providers.add(provider);
    }

    public static IAltarSource testForAltarSource(Level level, BlockPos pos) {
        if (pos == null) return null;

        List<IAltarSourceProvider> currentProviders = new ArrayList<>(providers);

        for (IAltarSourceProvider provider : currentProviders) {
            Optional<IAltarSource> source = provider.getAltarSource(level, pos.immutable());
            if (source.isPresent())
                return source.get();
        }

        return null;
    }
}
