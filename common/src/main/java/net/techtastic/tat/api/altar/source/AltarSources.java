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
        AtomicReference<IAltarSource> altar = new AtomicReference<>();

        if (pos == null) return altar.get();

        providers.forEach(provider ->
            provider.getAltarSource(level, pos).ifPresent(altar::getAndSet)
        );

        return altar.get();
    }
}
