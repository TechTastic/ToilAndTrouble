package net.techtastic.tat.augments;

import net.techtastic.tat.api.altar.augment.IAltarAugment;

public class PentacleAltarAugment implements IAltarAugment {
    @Override
    public String getType() {
        return "pentacle";
    }

    @Override
    public int getTypePriority() {
        return 5;
    }

    @Override
    public boolean matches(IAltarAugment augment) {
        return augment instanceof PentacleAltarAugment;
    }

    @Override
    public double modifyAltarRechargeRate(double initRate) {
        return initRate * 2;
    }
}
