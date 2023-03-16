package net.techtastic.tat.blockentity;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.TATBlockEntities;
import net.techtastic.tat.TATItems;
import net.techtastic.tat.api.KeyHelper;
import net.techtastic.tat.util.ITaglockedBlock;

import java.util.UUID;

public class BaseTaglockedBlockEntity extends BlockEntity implements ITaglockedBlock {
    public BaseTaglockedBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TATBlockEntities.BASE_TAGLOCKED_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    private boolean isPlayer = false;
    private GameProfile profile = null;
    private UUID uuid = null;

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putBoolean("ToilAndTrouble$isPlayer", this.isPlayer);
        if (this.profile != null) {
            CompoundTag playerTag = new CompoundTag();
            NbtUtils.writeGameProfile(playerTag, this.profile);
            compoundTag.put("ToilAndTrouble$profile", playerTag);
        }
        if (this.uuid != null)
            compoundTag.putUUID("ToilAndTrouble$uuid", this.uuid);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        this.isPlayer = compoundTag.getBoolean("ToilAndTrouble$isPlayer");
        if (compoundTag.contains("ToilAndTrouble$profile"))
            this.profile = NbtUtils.readGameProfile(compoundTag.getCompound("ToilAndTrouble$profile"));
        if (compoundTag.contains("ToilAndTrouble$uuid"))
            this.uuid = compoundTag.getUUID("ToilAndTrouble$uuid");
    }

    @Override
    public Entity getTaggedEntity() {
        if (isPlayer) {
            return getLevel().getPlayerByUUID(profile.getId());
        }
        return ((ServerLevel) getLevel()).getEntity(uuid);
    }

    @Override
    public void setTaggedEntity(Entity entity) {
        if (!(entity instanceof LivingEntity)) return;

        if (entity instanceof Player player) {
            isPlayer = true;
            profile = player.getGameProfile();
        } else {
            isPlayer = false;
            uuid = entity.getUUID();
        }

        setChanged();
    }

    @Override
    public void resetTaggedEntity() {
        isPlayer = false;
        profile = null;
        uuid = null;

        setChanged();
    }
}
