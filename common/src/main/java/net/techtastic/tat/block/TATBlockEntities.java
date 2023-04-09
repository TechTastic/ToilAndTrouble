package net.techtastic.tat.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.techtastic.tat.ToilAndTrouble;
import net.techtastic.tat.block.TATBlocks;
import net.techtastic.tat.block.entity.*;

public class TATBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ToilAndTrouble.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);

    public static final RegistrySupplier<BlockEntityType<CastIronOvenBlockEntity>> CAST_IRON_OVEN_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("cast_iron_oven", () ->
                    BlockEntityType.Builder.of(CastIronOvenBlockEntity::new, TATBlocks.CAST_IRON_OVEN.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<DistilleryBlockEntity>> DISTILLERY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("distillery", () ->
                    BlockEntityType.Builder.of(DistilleryBlockEntity::new, TATBlocks.DISTILLERY.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<BaseLockedBlockEntity>> BASE_LOCKED_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("base_locked_block_entity", () ->
                    BlockEntityType.Builder.of(
                            BaseLockedBlockEntity::new,
                            TATBlocks.ROWAN_PRESSURE_PLATE.get(),
                            TATBlocks.ROWAN_BUTTON.get(),
                            TATBlocks.ROWAN_FENCE_GATE.get(),
                            TATBlocks.HAWTHORN_PRESSURE_PLATE.get(),
                            TATBlocks.HAWTHORN_BUTTON.get(),
                            TATBlocks.HAWTHORN_FENCE_GATE.get(),
                            TATBlocks.ALDER_PRESSURE_PLATE.get(),
                            TATBlocks.ALDER_BUTTON.get(),
                            TATBlocks.ALDER_FENCE_GATE.get()
                    ).build(null));

    public static final RegistrySupplier<BlockEntityType<AltarBlockEntity>> ALTAR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("altar", () ->
                    BlockEntityType.Builder.of(AltarBlockEntity::new, TATBlocks.ALTAR.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<BaseTaglockedBlockEntity>> BASE_TAGLOCKED_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("taglocked", () ->
                    BlockEntityType.Builder.of(BaseTaglockedBlockEntity::new, TATBlocks.BLODDY_ROSE.get()).build(null));

    public static void register() {
        BLOCK_ENTITIES.register();
    }
}
