package net.techtastic.tat.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.util.ITaglockedBlock;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;

@Mixin(BedBlockEntity.class)
public class MixinBedBlockEntity extends BlockEntity implements ITaglockedBlock {
    public MixinBedBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    private GameProfile lastSleptIn;

    @Override
    public void setTaggedEntity(Entity entity) {
        if (entity instanceof Player player) {
            this.lastSleptIn = player.getGameProfile();
            this.setChanged();
        }
    }

    @Override
    public Entity getTaggedEntity() {
        if (this.lastSleptIn == null) return null;
        return Objects.requireNonNull(this.getLevel()).getPlayerByUUID(this.lastSleptIn.getId());
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        if (this.lastSleptIn != null) {
            CompoundTag tag = new CompoundTag();
            NbtUtils.writeGameProfile(tag, this.lastSleptIn);
            compoundTag.put("ToilAndTrouble$lastSleptIn", tag);
        }

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        if (compoundTag.contains("ToilAndTrouble$lastSleptIn")) {
            CompoundTag tag = compoundTag.getCompound("ToilAndTrouble$lastSleptIn");
            this.lastSleptIn = NbtUtils.readGameProfile(tag);
        }
    }
}
