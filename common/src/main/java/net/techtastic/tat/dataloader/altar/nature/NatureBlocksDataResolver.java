package net.techtastic.tat.dataloader.altar.nature;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.event.RegistryEvents;
import org.jetbrains.annotations.NotNull;

import java.util.*;

record NatureBlocksInfo (
        ResourceLocation id, int priority, int naturalPower, int limit
) {}

public class NatureBlocksDataResolver implements NaturalBlocksInfoProvider {
    private static final HashMap<ResourceLocation, NatureBlocksInfo> map = new HashMap<>();
    public static NatureBlocksDataLoader loader = new NatureBlocksDataLoader();

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public int getNaturalPower(BlockState state) {
        if (map.containsKey(Registry.BLOCK.getKey(state.getBlock()))) {
            return map.get(Registry.BLOCK.getKey(state.getBlock())).naturalPower();
        }
        return 0;
    }

    @Override
    public int getMaxNaturalLimit(BlockState state) {
        if (map.containsKey(Registry.BLOCK.getKey(state.getBlock()))) {
            return map.get(Registry.BLOCK.getKey(state.getBlock())).limit();
        }
        return 0;
    }

    public static class NatureBlocksDataLoader extends SimpleJsonResourceReloadListener {
        private final List<NatureBlocksInfo> tags = new ArrayList<>();

        public NatureBlocksDataLoader() {
            super(new Gson(), "tat_nature_blocks");

            RegistryEvents.onTagsLoaded(() -> tags.forEach((tagInfo) -> {
                Optional<HolderSet.Named<Block>> tag =
                        Registry.BLOCK.getTag(TagKey.create(Registry.BLOCK_REGISTRY, tagInfo.id()));
                if (tag.isPresent()) {
                    tag.get().forEach((it) -> {
                        add(new NatureBlocksInfo(Registry.BLOCK.getKey(it.value()), tagInfo.priority(), tagInfo.naturalPower(), tagInfo.limit()));
                    });
                } else {
                    System.err.println("No specified tag " + tagInfo.id() + " doesn't exist!");
                    return;
                }
            }));
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
            map.clear();
            tags.clear();
            object.forEach((location, element) -> {
            try {
                if (element.isJsonArray()) {
                    element.getAsJsonArray().forEach((element1) -> {
                            parse(element1, location);
                    });
                } else if (element.isJsonObject()) {
                    parse(element, location);
                } else throw new IllegalArgumentException();
            } catch (Exception e) {
                System.err.println("Error while loading datapack for tat_nature_blocks " + e);
            }});
        }

        // so why does this exist? cus for some reason initializes their tags after all the other things
        // idk why, so we note them down and use them later
        private void addToBeAddedTags(NatureBlocksInfo tag) {
            tags.add(tag);
        }

        private void add(NatureBlocksInfo info) {
            if (map.containsKey(info.id())) {
                if (map.get(info.id()).priority() < info.priority()) {
                    map.put(info.id(), info);
                }
            } else {
                map.put(info.id(), info);
            }
        }

        private void parse(JsonElement element, ResourceLocation origin) {

            JsonElement tag = element.getAsJsonObject().get("tag");

            int priority = 100;
            if (element.getAsJsonObject().has("priority")) {
                priority = element.getAsJsonObject().get("priority").getAsInt();
            }

            int power;
            int limit;
            try {
                power = element.getAsJsonObject().get("power").getAsInt();
                limit = element.getAsJsonObject().get("limit").getAsInt();
            } catch (Exception e) {
                throw new IllegalArgumentException("No power or limit in file " + origin);
            }

            if (tag != null) {
                addToBeAddedTags(new NatureBlocksInfo(new ResourceLocation(tag.getAsString()), priority, power, limit));
            } else {
                String block;
                try {
                    block = element.getAsJsonObject().get("block").getAsString();
                } catch (Exception e) {
                    throw new IllegalArgumentException("No block or tag in file " + origin);
                }

                add(new NatureBlocksInfo(new ResourceLocation(block), priority, power, limit));
            }
        }
    }
}
