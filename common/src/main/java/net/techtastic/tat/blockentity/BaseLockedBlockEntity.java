package net.techtastic.tat.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.TATBlockEntities;
import net.techtastic.tat.TATItems;
import net.techtastic.tat.api.KeyHelper;

import java.util.UUID;

public class BaseLockedBlockEntity extends BlockEntity {
    public BaseLockedBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(blockPos, blockState, UUID.randomUUID());
    }
    public BaseLockedBlockEntity(BlockPos blockPos, BlockState blockState, UUID keyId) {
        super(TATBlockEntities.BASE_LOCKED_BLOCK_ENTITY.get(), blockPos, blockState);
        this.keyId = keyId;
    }

    private UUID keyId;
    private boolean initKey = false;

    public UUID getKeyId() {
        return this.keyId;
    }

    public boolean hasMadeInitialKey() {
        return this.initKey;
    }

    public void initialKeyMade(boolean bool) {
        this.initKey = bool;
    }

    public boolean canUnlock(BaseLockedBlockEntity locked, ItemStack stack) {
        return KeyHelper.hasMatchInItem(locked.getKeyId(), stack);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putUUID("ToilAndTrouble$keyId", this.keyId);
        compoundTag.putBoolean("ToilAndTrouble$initKey", this.initKey);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        this.keyId = compoundTag.getUUID("ToilAndTrouble$keyId");
        this.initKey = compoundTag.getBoolean("ToilAndTrouble$initKey");
    }

    public void spawnKeyItem(BaseLockedBlockEntity locked, BlockPos blockPos) {
        BlockPos spawnPos = blockPos.above();
        Level level = locked.getLevel();
        ItemStack keyStack = new ItemStack(TATItems.KEY.get());

        KeyHelper.setKeyId(locked.getKeyId(), keyStack);

        CompoundTag tag = keyStack.getTag();
        tag.putLong("ToilAndTrouble$lockedBlockPos", blockPos.asLong());

        assert level != null;
        level.addFreshEntity(new ItemEntity(level, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), keyStack));
    }

    public void makeInitialKey(BaseLockedBlockEntity entity, BlockPos pos) {
        if (entity.hasMadeInitialKey()) return;

        entity.spawnKeyItem(entity, pos);
        entity.initialKeyMade(true);
        entity.setChanged();
    }
}
