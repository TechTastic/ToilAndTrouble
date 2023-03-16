package net.techtastic.tat.item;

import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.techtastic.tat.TATItems;

public class TATItemModelPredicates {
    public static void registerModelPredicates() {
        registerKeyRing();
        registerTaglock();
    }

    private static void registerKeyRing() {
        ItemPropertiesRegistry.register(TATItems.KEY_RING.get(), new ResourceLocation("key_count"),
                (stack, clientLevel, livingEntity, i) -> {
                    CompoundTag ringTag = stack.getOrCreateTag();
                    if (!ringTag.contains("ToilAndTrouble$keyList")) return 0.0f;

                    ListTag listTag = (ListTag) ringTag.get("ToilAndTrouble$keyList");
                    int keyCount = listTag.size();
                    return switch (keyCount) {
                        case 0, 1 -> 0.0f;
                        case 2 -> 0.1f;
                        case 3 -> 0.2f;
                        case 4 -> 0.3f;
                        default -> 0.4f;
                    };
                });
    }

    private static void registerTaglock() {
        ItemPropertiesRegistry.register(TATItems.TAGLOCK.get(), new ResourceLocation("contents"),
                (stack, clientLevel, livingEntity, i) -> {
                    CompoundTag taglock = stack.getOrCreateTag();
                    if (taglock.contains("ToilAndTrouble$taglock")) {
                        CompoundTag tag = taglock.getCompound("ToilAndTrouble$taglock");
                        return tag.getBoolean("ToilAndTrouble$isDecayed") ? 0.5f : 1.0f;
                    } else {
                        return 0.0f;
                    }
                });
    }
}
