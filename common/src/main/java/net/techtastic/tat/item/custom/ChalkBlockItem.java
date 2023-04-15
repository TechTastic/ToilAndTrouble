package net.techtastic.tat.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.techtastic.tat.api.RitualType;
import org.jetbrains.annotations.NotNull;

public class ChalkBlockItem extends BlockItem {
    private final RitualType type;

    public ChalkBlockItem(RitualType type, Block block, Properties properties) {
        super(block, properties);
        this.type = type;
    }

    @Override
    public Component getName(@NotNull ItemStack itemStack) {
        return new TranslatableComponent(switch (type) {
            case NORMAL -> "item.tat.chalk.normal";
            case GOLDEN -> "item.tat.chalk.golden";
            case OTHERWHERE -> "item.tat.chalk.otherwhere";
            case INFERNAL -> "item.tat.chalk.infernal";
        }).append(" ").append(new TranslatableComponent("item.tat.chalk"));
    }
}
