package net.techtastic.tat.block.custom;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class CustomCropBlock extends CropBlock {
    private final IntegerProperty AGE;
    private final int MAX_AGE;
    private final ItemLike SEED;
    public CustomCropBlock(Properties properties, IntegerProperty age, int maxAge, ItemLike seed) {
        super(properties);
        this.AGE = age;
        this.MAX_AGE = maxAge;
        this.SEED = seed;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return SEED;
    }
}
