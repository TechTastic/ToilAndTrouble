package net.techtastic.tat;

import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;
import net.techtastic.tat.dataloader.NatureBlocksDataResolver;

public class TATResourceListeners {
    public static void register() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, NatureBlocksDataResolver.loader);
    }
}
