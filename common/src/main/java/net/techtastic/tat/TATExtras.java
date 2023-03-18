package net.techtastic.tat;

import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;
import net.techtastic.tat.dataloader.altar.augment.AltarAugmentDataResolver;
import net.techtastic.tat.dataloader.altar.nature.NatureBlocksDataResolver;

public class TATExtras {
    public static void registerResourceListeners() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, NatureBlocksDataResolver.loader);
        ReloadListenerRegistry.register(PackType.SERVER_DATA, AltarAugmentDataResolver.loader);
    }

    public static void register() {
        registerResourceListeners();
    }
}
