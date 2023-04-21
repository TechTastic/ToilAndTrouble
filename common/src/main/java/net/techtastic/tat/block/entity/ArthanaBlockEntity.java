package net.techtastic.tat.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.item.TATItems;
import org.jetbrains.annotations.NotNull;

public class ArthanaBlockEntity extends BlockEntity {
    private CompoundTag arthanaTag = new CompoundTag();

    public ArthanaBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TATBlockEntities.ARTHANA_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        compoundTag.put("ToilAndTrouble&arthanaNBT", this.arthanaTag);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);

        this.arthanaTag = compoundTag.getCompound("ToilAndTrouble&arthanaNBT");
    }

    public ItemStack getArthana() {
        ItemStack arthana = new ItemStack(TATItems.ARTHANA.get(), 1);
        arthana.setTag(this.arthanaTag);
        return arthana;
    }

    public void setArthana(ItemStack arthana) {
        this.arthanaTag = arthana.getOrCreateTag();
        this.setChanged();
    }
}
