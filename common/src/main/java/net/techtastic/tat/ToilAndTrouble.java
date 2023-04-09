package net.techtastic.tat;

import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ItemLike;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.block.TATBlocks;
import net.techtastic.tat.dataloader.altar.nature.NatureBlockStateInfo;
import net.techtastic.tat.dataloader.altar.nature.NatureBlocksDataResolver;
import net.techtastic.tat.integration.TATFuels;
import net.techtastic.tat.item.TATItemModelPredicates;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.screen.*;
import net.techtastic.tat.world.feature.TATConfiguredFeatures;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ToilAndTrouble {
    public static final String MOD_ID = "tat";
    public static List<ItemLike> FUELS;
    
    public static void init() {
        TATItems.register();
        TATBlocks.register();

        TATBlockEntities.register();
        TATMenuTypes.register();

        TATConfiguredFeatures.register();

        TATRecipes.register();

        FUELS = TATFuels.getAllFuels();
        NatureBlockStateInfo.init();
        TATExtras.register();

        ReloadListenerRegistry.register(PackType.SERVER_DATA, new PreparableReloadListener() {
            @Override
            public String getName() {
                return "tat_nature_blocks";
            }

            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
                return NatureBlocksDataResolver.loader.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
            }
        });
    }

    public static void initClient() {
        RenderTypeRegistry.register(RenderType.translucent(), TATBlocks.CAST_IRON_OVEN.get());
        RenderTypeRegistry.register(RenderType.translucent(), TATBlocks.DISTILLERY.get());

        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.ROWAN_LEAVES.get());
        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.ROWAN_SAPLING.get());
        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.HAWTHORN_LEAVES.get());
        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.HAWTHORN_SAPLING.get());
        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.ALDER_LEAVES.get());
        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.ALDER_SAPLING.get());

        MenuRegistry.registerScreenFactory(TATMenuTypes.CAST_IRON_OVEN_MENU.get(), CastIronOvenScreen::new);
        MenuRegistry.registerScreenFactory(TATMenuTypes.DISTILLERY_MENU.get(), DistilleryScreen::new);
        MenuRegistry.registerScreenFactory(TATMenuTypes.ALTAR_MENU.get(), AltarScreen::new);

        TATItemModelPredicates.registerModelPredicates();
    }
}
