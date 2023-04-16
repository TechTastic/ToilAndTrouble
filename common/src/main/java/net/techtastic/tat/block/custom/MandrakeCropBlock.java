package net.techtastic.tat.block.custom;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.techtastic.tat.item.TATItems;
import org.jetbrains.annotations.NotNull;

public class MandrakeCropBlock extends CustomCropBlock {
    public MandrakeCropBlock(Properties properties) {
        super(properties, BlockStateProperties.AGE_7, 7, TATItems.MANDRAKE_SEEDS.get());
    }
}
