package net.techtastic.tat.fabric;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.techtastic.tat.block.TATBlocks;

public class TATExtraStuffs {
    public static void register() {
        registerFlammables();
        registerStrippables();
    }

    private static void registerFlammables() {
        FlammableBlockRegistry flame = FlammableBlockRegistry.getDefaultInstance();

        flame.add(TATBlocks.ROWAN_STAIRS.get(), 5, 20);
        flame.add(TATBlocks.ROWAN_SLAB.get(), 5, 20);
        flame.add(TATBlocks.ROWAN_PLANKS.get(), 5, 20);
        flame.add(TATBlocks.ROWAN_FENCE.get(), 5, 20);
        flame.add(TATBlocks.ROWAN_FENCE_GATE.get(), 5, 20);
        flame.add(TATBlocks.ROWAN_LOG.get(), 5, 5);
        flame.add(TATBlocks.ROWAN_WOOD.get(), 5, 5);
        flame.add(TATBlocks.STRIPPED_ROWAN_LOG.get(), 5, 5);
        flame.add(TATBlocks.STRIPPED_ROWAN_WOOD.get(), 5, 5);
        flame.add(TATBlocks.ROWAN_LEAVES.get(), 30, 60);

        flame.add(TATBlocks.HAWTHORN_STAIRS.get(), 5, 20);
        flame.add(TATBlocks.HAWTHORN_SLAB.get(), 5, 20);
        flame.add(TATBlocks.HAWTHORN_PLANKS.get(), 5, 20);
        flame.add(TATBlocks.HAWTHORN_FENCE.get(), 5, 20);
        flame.add(TATBlocks.HAWTHORN_FENCE_GATE.get(), 5, 20);
        flame.add(TATBlocks.HAWTHORN_LOG.get(), 5, 5);
        flame.add(TATBlocks.HAWTHORN_WOOD.get(), 5, 5);
        flame.add(TATBlocks.STRIPPED_HAWTHORN_LOG.get(), 5, 5);
        flame.add(TATBlocks.STRIPPED_HAWTHORN_WOOD.get(), 5, 5);
        flame.add(TATBlocks.HAWTHORN_LEAVES.get(), 30, 60);

        flame.add(TATBlocks.ALDER_STAIRS.get(), 5, 20);
        flame.add(TATBlocks.ALDER_SLAB.get(), 5, 20);
        flame.add(TATBlocks.ALDER_PLANKS.get(), 5, 20);
        flame.add(TATBlocks.ALDER_FENCE.get(), 5, 20);
        flame.add(TATBlocks.ALDER_FENCE_GATE.get(), 5, 20);
        flame.add(TATBlocks.ALDER_LOG.get(), 5, 5);
        flame.add(TATBlocks.ALDER_WOOD.get(), 5, 5);
        flame.add(TATBlocks.STRIPPED_ALDER_LOG.get(), 5, 5);
        flame.add(TATBlocks.STRIPPED_ALDER_WOOD.get(), 5, 5);
        flame.add(TATBlocks.ALDER_LEAVES.get(), 30, 60);
    }

    private static void registerStrippables() {
        StrippableBlockRegistry.register(TATBlocks.ROWAN_LOG.get(), TATBlocks.STRIPPED_ROWAN_LOG.get());
        StrippableBlockRegistry.register(TATBlocks.ROWAN_WOOD.get(), TATBlocks.STRIPPED_ROWAN_WOOD.get());

        StrippableBlockRegistry.register(TATBlocks.HAWTHORN_LOG.get(), TATBlocks.STRIPPED_HAWTHORN_LOG.get());
        StrippableBlockRegistry.register(TATBlocks.HAWTHORN_WOOD.get(), TATBlocks.STRIPPED_HAWTHORN_WOOD.get());

        StrippableBlockRegistry.register(TATBlocks.ALDER_LOG.get(), TATBlocks.STRIPPED_ALDER_LOG.get());
        StrippableBlockRegistry.register(TATBlocks.ALDER_WOOD.get(), TATBlocks.STRIPPED_ALDER_WOOD.get());
    }
}
