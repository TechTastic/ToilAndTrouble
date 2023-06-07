package net.techtastic.tat.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.techtastic.tat.block.TATBlocks;

public class ChaliceBlockItem extends BlockItem {
    public ChaliceBlockItem(Properties properties) {
        super(TATBlocks.CHALICE.get(), properties);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        TranslatableComponent name = new TranslatableComponent("block.tat.chalice");
        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.contains("ToilAndTrouble$soup") && tag.getBoolean("ToilAndTrouble$soup"))
            name.append(" (")
                    .append(new TranslatableComponent("block.tat.chalice.filled"))
                    .append(")");
        return name;
    }
}
