package net.techtastic.tat.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KeyRingItem extends Item {
    public KeyRingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.contains("ToilAndTrouble$keyList")) {
            ListTag listTag = (ListTag) tag.get("ToilAndTrouble$keyList");
            list.add(new TranslatableComponent("item.tat.key_ring.tooltip.contains")
                    .append(" " + listTag.size() + " ")
                    .append(new TranslatableComponent("item.tat.key_ring.tooltip.keys"))
                    .withStyle(Style.EMPTY.withColor(0xFF00FF)));

            list.add(new TranslatableComponent("item.tat.key.tooltip").withStyle(Style.EMPTY.withColor(0xFF00FF)));

            for (Tag tagFromList : listTag) {
                CompoundTag keyTag = (CompoundTag) tagFromList;
                BlockPos pos = BlockPos.of(keyTag.getLong("ToilAndTrouble$lockedBlockPos"));
                list.add(new TextComponent("  ")
                                .append("  X " + pos.getX() + ", Y " + pos.getY() + ", Z " + pos.getZ()));
            }
        }

        super.appendHoverText(itemStack, level, list, tooltipFlag);
    }
}
