package net.techtastic.tat.dataloader;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
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
import java.util.function.ToIntFunction;

interface NaturalBlocksInfoProvider {
    default int getPriority() {return 0;}
    default int getNaturalPower(BlockState state) {return 0;}
    default int getMaxNaturalLimit(BlockState state) {return 0;}
}

public class NatureBlockStateInfo {

    // registry for mods to add their weights
    static MappedRegistry<NaturalBlocksInfoProvider> REGISTRY = new MappedRegistry<>(
            ResourceKey.createRegistryKey(new ResourceLocation(ToilAndTrouble.MOD_ID, "natural_block_providers")),
            Lifecycle.experimental(),
            null
    );

    private static List<NaturalBlocksInfoProvider> SORTED_REGISTRY;

    public static void init() {
        Registry.register(REGISTRY, new ResourceLocation(ToilAndTrouble.MOD_ID, "data"), new NatureBlocksDataResolver());

        RegistryEvents.onRegistriesComplete(() -> SORTED_REGISTRY = REGISTRY.stream().sorted((first, second) -> {

        }).toList());
    }

    // init { doesn't work since the class gets loaded too late
    // This is [ThreadLocal] because in single-player games the Client thread and Server thread will read/write to
    // [CACHE] simultaneously. This creates a data race that can crash the game (See https://github.com/ValkyrienSkies/Valkyrien-Skies-2/issues/126).
    public ThreadLocal<Int2ObjectOpenHashMap<Pair<Integer, Integer>>> CACHE = ThreadLocal.withInitial(Int2ObjectOpenHashMap::new);
    // NOTE: this caching can get allot better, ex. default just returns constants so it might be more faster
    //  if we store that these values do not need to be cached by double and blocktype but just that they use default impl

    // this gets the weight and type provided by providers; or it gets it out of the cache

    public Pair<Integer, Integer> get(BlockState state) {
        int r = Block.getId(state);
        if (r != -1) {
            return CACHE.get().getOrDefault(r, iterateNatureRegistry(state));
        }
        return null;
    }

    private Pair<Integer, Integer> iterateNatureRegistry (BlockState state) {
        return new Pair<>(
                SORTED_REGISTRY.stream().findFirst().orElseGet(() -> new NaturalBlocksInfoProvider() {
                }).getNaturalPower(state),
                SORTED_REGISTRY.stream().findFirst().orElseGet(() -> new NaturalBlocksInfoProvider() {
                }).getMaxNaturalLimit(state)
        );
    }
}
