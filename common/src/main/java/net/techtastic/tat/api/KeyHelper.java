package net.techtastic.tat.api;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class KeyHelper {
    public static boolean hasMultipleKeys(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains("ToilAndTrouble$keyList");
    }

    public static boolean hasMatchInItem(UUID id, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        if (tag.contains("ToilAndTrouble$keyId")) return id.equals(tag.getUUID("ToilAndTrouble$keyId"));

        if (tag.contains("ToilAndTrouble$keyList")) {
            ListTag listTag = (ListTag) tag.get("ToilAndTrouble$keyList");
            for (Tag key : listTag) {
                boolean matches = id.equals(((CompoundTag) key).getUUID("ToilAndTrouble$keyId"));
                if (matches) return true;
            }
        }

        return false;
    }

    public static UUID getKeyIdOrNull(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("ToilAndTrouble$keyId")) return tag.getUUID("ToilAndTrouble$keyId");

        return null;
    }

    public static List<UUID> getKeyListOrNull(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("ToilAndTrouble$keyList")) {
            ListTag listTag = (ListTag) tag.get("ToilAndTrouble$keyList");

            List<UUID> list = new ArrayList<>(listTag.size());
            for (Tag key : listTag) {
                list.add(((CompoundTag) key).getUUID("ToilAndTrouble$keyId"));
            }
            return list;
        }

        return null;
    }

    public static void setKeyId(UUID id, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putUUID("ToilAndTrouble$keyId", id);
    }

    public static void setKeyPos(BlockPos pos, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putLong("ToilAndTrouble$lockedBlockPos", pos.asLong());
    }

    public static void addToKeyList(UUID id, ItemStack stack, BlockPos pos) {
        CompoundTag keyTag = new CompoundTag();
        keyTag.putUUID("ToilAndTrouble$keyId", id);
        keyTag.putLong("ToilAndTrouble$lockedBlockPos", pos.asLong());

        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("ToilAndTrouble$keyList")) {
            ListTag listTag = (ListTag) tag.get("ToilAndTrouble$keyList");
            listTag.add(keyTag);
            tag.put("ToilAndTrouble$keyList", listTag);
        } else {
            ListTag listTag = new ListTag();
            listTag.add(keyTag);
            tag.put("ToilAndTrouble$keyList", listTag);
        }
    }
}
