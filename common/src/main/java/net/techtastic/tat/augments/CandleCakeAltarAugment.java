package net.techtastic.tat.augments;

import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.techtastic.tat.api.IAltarAugment;

public class CandleCakeAltarAugment implements IAltarAugment {
    private final BlockState state;

    public CandleCakeAltarAugment(BlockState state) {
        this.state = state;
    }

    @Override
    public String getType() {
        return "light";
    }

    @Override
    public int getTypePriority() {
        return 20;
    }

    @Override
    public boolean matches(IAltarAugment augment) {
        return augment instanceof CandleCakeAltarAugment;
    }

    @Override
    public double boostAltarRechargeRate(double initRate) {
        if (this.state.getBlock() instanceof CandleCakeBlock)
            if (this.state.getValue(BlockStateProperties.LIT))
                initRate += 0.25;
        return initRate;
    }
}
