package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.api.RitualType;
import org.jetbrains.annotations.Nullable;

public class ChalkBlock extends Block {
    private final RitualType type;

    public ChalkBlock(RitualType type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public MutableComponent getName() {
        return new TranslatableComponent(switch (type) {
            case NORMAL -> "item.tat.chalk.normal";
            case GOLDEN -> "item.tat.chalk.golden";
            case OTHERWHERE -> "item.tat.chalk.otherwhere";
            case INFERNAL -> "item.tat.chalk.infernal";
        }).append(" ").append(new TranslatableComponent("item.tat.chalk"));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
