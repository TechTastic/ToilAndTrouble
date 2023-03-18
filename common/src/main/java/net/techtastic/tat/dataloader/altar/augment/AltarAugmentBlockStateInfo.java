package net.techtastic.tat.dataloader.altar.augment;

import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.ToilAndTrouble;
import net.techtastic.tat.event.RegistryEvents;

import java.util.Comparator;
import java.util.List;

public class AltarAugmentBlockStateInfo {
    static MappedRegistry<AltarAugmentBlocksInfoProvider> REGISTRY = new MappedRegistry<>(
            ResourceKey.createRegistryKey(new ResourceLocation(ToilAndTrouble.MOD_ID, "augment_block_providers")),
            Lifecycle.experimental(),
            null
    );

    private static List<AltarAugmentBlocksInfoProvider> SORTED_REGISTRY;

    public static void init() {
        Registry.register(REGISTRY, new ResourceLocation(ToilAndTrouble.MOD_ID, "data"), new AltarAugmentDataResolver());

        RegistryEvents.onRegistriesComplete(() -> SORTED_REGISTRY = REGISTRY.stream()
                .sorted(Comparator.comparingInt(AltarAugmentBlocksInfoProvider::getPriority).reversed()).toList());
    }

    // init { doesn't work since the class gets loaded too late
    // This is [ThreadLocal] because in single-player games the Client thread and Server thread will read/write to
    // [CACHE] simultaneously. This creates a data race that can crash the game (See https://github.com/ValkyrienSkies/Valkyrien-Skies-2/issues/126).
    public ThreadLocal<Int2ObjectOpenHashMap<Pair<AugmentType, Integer>>> CACHE = ThreadLocal.withInitial(Int2ObjectOpenHashMap::new);
    // NOTE: this caching can get allot better, ex. default just returns constants so it might be more faster
    //  if we store that these values do not need to be cached by double and blocktype but just that they use default impl

    // this gets the weight and type provided by providers; or it gets it out of the cache

    public Pair<AugmentType, Integer> get(BlockState state) {
        int r = Block.getId(state);
        if (r != -1) {
            return CACHE.get().getOrDefault(r, iterateRegistry(state));
        }
        return null;
    }

    private Pair<AugmentType, Integer> iterateRegistry (BlockState state) {
        return Pair.of(
                SORTED_REGISTRY.stream().findFirst().orElseGet(() -> new AltarAugmentBlocksInfoProvider() {}).getAugmentType(state),
                SORTED_REGISTRY.stream().findFirst().orElseGet(() -> new AltarAugmentBlocksInfoProvider() {}).getTypePriority(state)
        );
    }
}
