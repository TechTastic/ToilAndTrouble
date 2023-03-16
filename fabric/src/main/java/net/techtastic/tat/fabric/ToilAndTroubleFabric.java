package net.techtastic.tat.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.techtastic.tat.ToilAndTrouble;
import net.fabricmc.api.ModInitializer;
import net.techtastic.tat.dataloader.NatureBlocksDataResolver;
import net.techtastic.tat.event.RegistryEvents;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static net.minecraft.server.packs.PackType.SERVER_DATA;

public class ToilAndTroubleFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ToilAndTrouble.init();

        TATExtraStuffs.register();

        // registering data loaders
        NatureBlocksDataResolver.NatureBlocksDataLoader loader = NatureBlocksDataResolver.loader; // the get makes a new instance so get it only once
        ResourceManagerHelper.get(SERVER_DATA)
                .registerReloadListener(new IdentifiableResourceReloadListener() {
                    @Override
                    public ResourceLocation getFabricId() {
                        return new ResourceLocation(ToilAndTrouble.MOD_ID, "altar_nature_blocks");
                    }


                    @Override
                    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
                        return loader.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
                    }
                });
        CommonLifecycleEvents.TAGS_LOADED.register(RegistryEvents::tagsAreLoaded);
    }
}
