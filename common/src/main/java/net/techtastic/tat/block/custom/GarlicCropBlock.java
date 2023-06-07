package net.techtastic.tat.block.custom;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.techtastic.tat.item.TATItems;
import org.jetbrains.annotations.NotNull;

public class GarlicCropBlock extends CropBlock {
    private final IntegerProperty AGE = IntegerProperty.create("age", 0, 6);

    public GarlicCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAge() {
        return 6;
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return TATItems.GARLIC.get();
    }
}
