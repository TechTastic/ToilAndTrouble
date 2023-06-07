package net.techtastic.tat.augments;

import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.api.altar.augment.IAltarAugment;

public class TorchAltarAugment implements IAltarAugment {
    private final BlockState state;

    public TorchAltarAugment(BlockState state) {
        this.state = state;
    }

    @Override
    public String getType() {
        return "light";
    }

    @Override
    public int getTypePriority() {
        return 10;
    }

    @Override
    public boolean matches(IAltarAugment augment) {
        return augment instanceof TorchAltarAugment;
    }

    @Override
    public double boostAltarRechargeRate(double initRate) {
        if (!(this.state.getBlock() instanceof RedstoneTorchBlock)) {
            initRate += 1;
        }
        return initRate;
    }
}
