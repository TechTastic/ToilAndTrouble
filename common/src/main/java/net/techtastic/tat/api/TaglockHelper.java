package net.techtastic.tat.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.techtastic.tat.util.ITaglockedBlock;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public abstract class TaglockHelper {
    public static boolean getTaglockFromBlock(ItemStack taglock, Block block) {
        if (block instanceof ITaglockedBlock tagged) {
            Entity tag = tagged.getTaggedEntity();
            if (tag instanceof Player) {
                taglockPlayer(taglock, (Player) tag);
            } else {
                taglockEntity(taglock, tag);
            }
            tagged.resetTaggedEntity();
            return true;
        }
        return false;
    }

    public static boolean getTaglockFromBlockEntity(ItemStack taglock, BlockEntity block) {
        if (block instanceof ITaglockedBlock tagged) {
            Entity tag = tagged.getTaggedEntity();
            if (tag instanceof Player) {
                taglockPlayer(taglock, (Player) tag);
            } else {
                taglockEntity(taglock, tag);
            }
            tagged.resetTaggedEntity();
            return true;
        }
        return false;
    }

    public static boolean isTaglockEmpty(ItemStack taglock) {
        CompoundTag mainTag = taglock.getOrCreateTag();
        return mainTag.contains("ToilAndTrouble$taglock");
    }

    public static boolean isTaglockEmptyOrDecayed(ItemStack taglock) {
        CompoundTag mainTag = taglock.getOrCreateTag();
        return !mainTag.contains("ToilAndTrouble$taglock") ||
                mainTag.getCompound("ToilAndTrouble$taglock").getBoolean("ToilAndTrouble$isDecayed");
    }

    public static void setDecayed(ItemStack taglock, boolean bool) {
        CompoundTag lock = taglock.getOrCreateTag();
        if (TaglockHelper.isTaglockEmpty(taglock)) {
            CompoundTag tag = lock.getCompound("ToilAndTrouble$taglock");
            int decayTimer = getDecayTicks(taglock);
            tag.putBoolean("ToilAndTrouble$isDecayed", bool);
        }
    }

    public static int getDecayTicks(ItemStack taglock) {
        CompoundTag mainTag = taglock.getOrCreateTag();
        return mainTag.contains("ToilAndTrouble$taglock") ? mainTag.getCompound("ToilAndTrouble$taglock").getInt("ToilAndTrouble$decayTimer") : 0;
    }

    public static void setDecayTicks(ItemStack taglock, int ticks) {
        CompoundTag mainTag = taglock.getOrCreateTag();
        if (mainTag.contains("ToilAndTrouble$taglock")) mainTag.getCompound("ToilAndTrouble$taglock").putInt("ToilAndTrouble$decayTimer", ticks);
    }

    public static void removeTaglockedEntityData(ItemStack taglock) {
        if (isTaglockEmpty(taglock)) {
            CompoundTag tag = taglock.getOrCreateTag().getCompound("ToilAndTrouble$taglock");
            if (!tag.contains("ToilAndTrouble$playerProfile")) {
                tag.remove("ToilAndTrouble$entityName");
                tag.remove("ToilAndTrouble$entityUuid");
            } else {
                tag.remove("ToilAndTrouble$playerProfile");
            }
        }
    }

    public static Entity getTaglockedEntityOrNull(ItemStack taglock, ServerLevel level) {
        CompoundTag mainTag = taglock.getOrCreateTag();
        if (!mainTag.contains("ToilAndTrouble$taglock")) return null;
        CompoundTag tag = mainTag.getCompound("ToilAndTrouble$taglock");
        if (tag.contains("ToilAndTrouble$playerProfile")) return level.getPlayerByUUID(
                NbtUtils.readGameProfile(tag.getCompound("ToilAndTrouble$playerProfile")).getId());
        return level.getEntity(tag.getUUID("ToilAndTrouble$entityUuid"));
    }

    public static UUID getTaglockedUuidOrNull(ItemStack taglock) {
        CompoundTag mainTag = taglock.getOrCreateTag();
        if (!mainTag.contains("ToilAndTrouble$taglock")) return null;
        CompoundTag tag = mainTag.getCompound("ToilAndTrouble$taglock");
        if (tag.contains("ToilAndTrouble$playerProfile")) return
                NbtUtils.readGameProfile(tag.getCompound("ToilAndTrouble$playerProfile")).getId();
        return tag.getUUID("ToilAndTrouble$entityUuid");
    }

    @Nonnull
    public static String getTaglockedNameOrEmpty(ItemStack taglock) {
        CompoundTag mainTag = taglock.getOrCreateTag();
        if (!mainTag.contains("ToilAndTrouble$taglock")) return "";
        CompoundTag tag = mainTag.getCompound("ToilAndTrouble$taglock");
        if (tag.contains("ToilAndTrouble$playerProfile")) return
                Objects.requireNonNull(NbtUtils.readGameProfile(tag.getCompound("ToilAndTrouble$playerProfile"))).getName();
        return tag.getString("ToilAndTrouble$entityName");
    }

    public static void taglockPlayer(ItemStack taglock, Player player) {
        CompoundTag mainTag = taglock.getOrCreateTag();

        if (mainTag.contains("ToilAndTrouble$taglock")) {
            mainTag.remove("ToilAndTrouble$taglock");
        }

        CompoundTag profile = new CompoundTag();
        NbtUtils.writeGameProfile(profile, player.getGameProfile());

        CompoundTag tag = new CompoundTag();
        tag.put("ToilAndTrouble$playerProfile", profile);
        tag.putBoolean("ToilAndTrouble$isDecayed", false);
        tag.putInt("ToilAndTrouble$decayTimer", 100000);

        mainTag.put("ToilAndTrouble$taglock", tag);
    }

    public static void taglockPlayer(ItemStack taglock, GameProfile player) {
        CompoundTag mainTag = taglock.getOrCreateTag();

        if (mainTag.contains("ToilAndTrouble$taglock")) {
            mainTag.remove("ToilAndTrouble$taglock");
        }

        CompoundTag profile = new CompoundTag();
        NbtUtils.writeGameProfile(profile, player);

        CompoundTag tag = new CompoundTag();
        tag.put("ToilAndTrouble$playerProfile", profile);
        tag.putBoolean("ToilAndTrouble$isDecayed", false);
        tag.putInt("ToilAndTrouble$decayTimer", 100000);

        mainTag.put("ToilAndTrouble$taglock", tag);
    }

    public static void taglockEntity(ItemStack taglock, Entity entity) {
        CompoundTag mainTag = taglock.getOrCreateTag();

        if (mainTag.contains("ToilAndTrouble$taglock")) {
            mainTag.remove("ToilAndTrouble$taglock");
        }

        CompoundTag tag = new CompoundTag();
        tag.putString("ToilAndTrouble$entityName", entity.getDisplayName().getString());
        tag.putUUID("ToilAndTrouble$entityUuid", entity.getUUID());
        tag.putBoolean("ToilAndTrouble$isDecayed", false);
        tag.putInt("ToilAndTrouble$decayTimer", 100000);

        mainTag.put("ToilAndTrouble$taglock", tag);
    }
}
