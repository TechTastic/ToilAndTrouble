package net.techtastic.tat.block.custom;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.techtastic.tat.item.TATItems;
import org.jetbrains.annotations.NotNull;

public class WormwoodCropBlock extends CropBlock {
    private final IntegerProperty AGE = BlockStateProperties.AGE_5;

    public WormwoodCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAge() {
        return 5;
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return TATItems.WORMWOOD_SEEDS.get();
    }
}
