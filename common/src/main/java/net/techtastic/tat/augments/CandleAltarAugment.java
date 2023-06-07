package net.techtastic.tat.augments;

import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.techtastic.tat.api.altar.augment.IAltarAugment;

public class CandleAltarAugment implements IAltarAugment {
    private final BlockState state;

    public CandleAltarAugment(BlockState state) {
        this.state = state;
    }

    @Override
    public String getType() {
        return "light";
    }

    @Override
    public int getTypePriority() {
        return 15;
    }

    @Override
    public boolean matches(IAltarAugment augment) {
        return augment instanceof CandleAltarAugment;
    }

    @Override
    public double boostAltarRechargeRate(double initRate) {
        if (this.state.getBlock() instanceof CandleCakeBlock)
            if (this.state.getValue(BlockStateProperties.LIT))
                initRate += (0.25 * this.state.getValue(CandleBlock.CANDLES));
        return initRate;
    }
}
