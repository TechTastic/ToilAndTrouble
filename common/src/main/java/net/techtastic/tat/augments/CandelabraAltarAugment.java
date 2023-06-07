package net.techtastic.tat.augments;

import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.techtastic.tat.api.altar.augment.IAltarAugment;

public class CandelabraAltarAugment implements IAltarAugment {
    private final BlockState state;

    public CandelabraAltarAugment(BlockState state) {
        this.state = state;
    }

    @Override
    public String getType() {
        return "light";
    }

    @Override
    public int getTypePriority() {
        return 5;
    }

    @Override
    public boolean matches(IAltarAugment augment) {
        return augment instanceof CandelabraAltarAugment;
    }

    @Override
    public double boostAltarRechargeRate(double initRate) {
        if (this.state.getValue(BlockStateProperties.LIT)) {
            initRate += 2;
        }
        return initRate;
    }
}
