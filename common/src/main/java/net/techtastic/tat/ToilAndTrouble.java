package net.techtastic.tat;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.ItemLike;
import net.techtastic.tat.dataloader.altar.augment.AltarAugmentBlockStateInfo;
import net.techtastic.tat.dataloader.altar.nature.NatureBlockStateInfo;
import net.techtastic.tat.integration.TATFuels;
import net.techtastic.tat.item.TATItemModelPredicates;
import net.techtastic.tat.screen.CastIronOvenScreen;
import net.techtastic.tat.screen.TATMenuTypes;
import net.techtastic.tat.world.feature.TATConfiguredFeatures;

import java.util.List;

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
        AltarAugmentBlockStateInfo.init();
        TATExtras.register();
    }

    public static void initClient() {
        RenderTypeRegistry.register(RenderType.translucent(), TATBlocks.CAST_IRON_OVEN.get());

        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.ROWAN_LEAVES.get());
        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.ROWAN_SAPLING.get());
        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.HAWTHORN_LEAVES.get());
        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.HAWTHORN_SAPLING.get());
        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.ALDER_LEAVES.get());
        RenderTypeRegistry.register(RenderType.cutout(), TATBlocks.ALDER_SAPLING.get());

        MenuRegistry.registerScreenFactory(TATMenuTypes.CAST_IRON_OVEN_MENU.get(), CastIronOvenScreen::new);

        TATItemModelPredicates.registerModelPredicates();
    }
}
