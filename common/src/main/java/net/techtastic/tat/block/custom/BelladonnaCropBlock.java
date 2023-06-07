package net.techtastic.tat.block.custom;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.techtastic.tat.item.TATItems;
import org.jetbrains.annotations.NotNull;

public class BelladonnaCropBlock extends CropBlock {
    private final IntegerProperty AGE = BlockStateProperties.AGE_5;

    public BelladonnaCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAge() {
        return 5;
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return TATItems.BELLADONNA_SEEDS.get();
    }
}
