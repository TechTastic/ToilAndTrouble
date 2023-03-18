package net.techtastic.tat.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.techtastic.tat.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.techtastic.tat.dataloader.altar.augment.AltarAugmentDataResolver;
import net.techtastic.tat.dataloader.altar.nature.NatureBlocksDataResolver;
import net.techtastic.tat.event.RegistryEvents;

@Mod(ToilAndTrouble.MOD_ID)
public class ToilAndTroubleForge {
    public ToilAndTroubleForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ToilAndTrouble.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientSetup);
        eventBus.addListener((e) -> {
            if (e instanceof AddReloadListenerEvent reload)
                registerResourceManagers(reload);
        });
        eventBus.addListener((e) -> {
            if (e instanceof TagsUpdatedEvent tags)
                RegistryEvents.tagsAreLoaded(tags.getTagManager(), false);
        });

        ToilAndTrouble.init();
    }

    void clientSetup(final FMLClientSetupEvent event) {
        ToilAndTrouble.initClient();
    }

    private void registerResourceManagers(AddReloadListenerEvent event) {
        event.addListener(NatureBlocksDataResolver.loader);
        event.addListener(AltarAugmentDataResolver.loader);
    }
}
