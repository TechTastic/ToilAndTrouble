package net.techtastic.tat.dataloader.altar.augment;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.Pair;
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

public class AltarAugmentDataResolver implements AltarAugmentBlocksInfoProvider {
    private static final HashMap<ResourceLocation, AltarAugmentBlocksInfo> map = new HashMap<>();
    public static AltarAugmentDataResolver.AltarAugmentBlocksDataLoader loader = new AltarAugmentDataResolver.AltarAugmentBlocksDataLoader();

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public AugmentType getAugmentType(BlockState state) {
        if (map.containsKey(Registry.BLOCK.getKey(state.getBlock()))) {
            return map.get(Registry.BLOCK.getKey(state.getBlock())).type();
        }
        return AugmentType.NONE;
    }

    @Override
    public int getTypePriority(BlockState state) {
        if (map.containsKey(Registry.BLOCK.getKey(state.getBlock()))) {
            return map.get(Registry.BLOCK.getKey(state.getBlock())).typePriority();
        }
        return 100;
    }

    public static class AltarAugmentBlocksDataLoader extends SimpleJsonResourceReloadListener {
        private final List<AltarAugmentBlocksInfo> tags = new ArrayList<>();

        public AltarAugmentBlocksDataLoader() {
            super(new Gson(), "tat_augment_blocks");

            RegistryEvents.onTagsLoaded(() -> tags.forEach((tagInfo) -> {
                Optional<HolderSet.Named<Block>> tag =
                        Registry.BLOCK.getTag(TagKey.create(Registry.BLOCK_REGISTRY, tagInfo.id()));
                if (tag.isPresent()) {
                    tag.get().forEach((it) -> {
                        add(new AltarAugmentBlocksInfo(Registry.BLOCK.getKey(it.value()), tagInfo.priority(), tagInfo.type(), tagInfo.typePriority()));
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
        private void addToBeAddedTags(AltarAugmentBlocksInfo tag) {
            tags.add(tag);
        }

        private void add(AltarAugmentBlocksInfo info) {
            if (map.containsKey(info.id())) {
                if (map.get(info.id()).priority() < info.priority()) {
                    map.put(info.id(), info);
                }
            } else {
                map.put(info.id(), info);
            }
        }

        private void parse(JsonElement element, ResourceLocation origin) {
            JsonObject object = element.getAsJsonObject();
            JsonElement tag = object.get("tag");

            int priority = object.has("priority") ? object.get("priority").getAsInt() : 100;

            String augmentType = "none";
            int typePriority = 100;
            Pair<>
            try {
                if (object.has("type")) {
                    augmentType = object.get("type").getAsString();
                    typePriority = object.get("type_priority").getAsInt();
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("No power or limit in file " + origin);
            }

            if (tag != null) {
                addToBeAddedTags(new AltarAugmentBlocksInfo(new ResourceLocation(tag.getAsString()), priority, AugmentType.fromString(augmentType), typePriority));
            } else {
                String block;
                try {
                    block = object.get("block").getAsString();
                } catch (Exception e) {
                    throw new IllegalArgumentException("No block or tag in file " + origin);
                }

                add(new AltarAugmentBlocksInfo(new ResourceLocation(block), priority, AugmentType.fromString(augmentType), typePriority));
            }
        }
    }
}
