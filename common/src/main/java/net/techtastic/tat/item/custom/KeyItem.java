package net.techtastic.tat.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

public class KeyItem extends Item {
    public KeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.contains("ToilAndTrouble$lockedBlockPos")) {
            BlockPos pos = BlockPos.of(tag.getLong("ToilAndTrouble$lockedBlockPos"));

            list.add(new TranslatableComponent("item.tat.key.tooltip").append(" ").withStyle(Style.EMPTY.withColor(0xFF00FF)));
            list.add(new TextComponent("  X " + pos.getX() + ",").withStyle(Style.EMPTY.withColor(0xFF00FF)));
            list.add(new TextComponent("  Y " + pos.getY() + ",").withStyle(Style.EMPTY.withColor(0xFF00FF)));
            list.add(new TextComponent("  Z " + pos.getZ()).withStyle(Style.EMPTY.withColor(0xFF00FF)));
        }

        super.appendHoverText(itemStack, level, list, tooltipFlag);
    }
}
