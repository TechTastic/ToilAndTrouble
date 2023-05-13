package net.techtastic.tat.fabric;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.block.TATBlocks;
import net.techtastic.tat.block.entity.KettleBlockEntity;
import net.techtastic.tat.fabric.util.FabricFluidTank;

public class TATExtraStuffs {
    public static void register() {
        registerFlammables();
        registerStrippables();
        registerFluidStorage();
    }

    public static void registerFluidStorage() {
        FluidStorage.SIDED.registerForBlockEntity(
                (be, direction) -> ((FabricFluidTank) be.tank).fluidStorage,
                TATBlockEntities.KETTLE_BLOCK_ENTITY.get()
        );
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
