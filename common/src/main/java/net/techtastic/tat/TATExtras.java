package net.techtastic.tat;

import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;
import net.techtastic.tat.api.AltarAugments;
import net.techtastic.tat.augments.TATAugmentProvider;
import net.techtastic.tat.dataloader.altar.nature.NatureBlocksDataResolver;

public class TATExtras {
    public static void registerResourceListeners() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, NatureBlocksDataResolver.loader);
    }

    public static void registerAltarAugments() {
        AltarAugments.registerAltarAugmentProvider(new TATAugmentProvider());
    }

    public static void register() {
        registerResourceListeners();
        registerAltarAugments();
    }
}
