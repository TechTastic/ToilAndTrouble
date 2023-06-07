package net.techtastic.tat.block.custom;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.techtastic.tat.item.TATItems;
import org.jetbrains.annotations.NotNull;

public class WolfsbaneCropBlock extends CropBlock {
    private final IntegerProperty AGE = BlockStateProperties.AGE_7;

    public WolfsbaneCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAge() {
        return 7;
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return TATItems.WOLFSBANE_SEEDS.get();
    }
}
