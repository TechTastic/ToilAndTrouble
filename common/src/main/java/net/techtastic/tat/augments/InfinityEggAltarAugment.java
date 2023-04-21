package net.techtastic.tat.augments;

import net.techtastic.tat.api.altar.augment.IAltarAugment;

public class InfinityEggAltarAugment implements IAltarAugment {
    @Override
    public String getType() {
        return "infinity_egg";
    }

    @Override
    public int getTypePriority() {
        return 5;
    }

    @Override
    public boolean matches(IAltarAugment augment) {
        return augment instanceof InfinityEggAltarAugment;
    }

    @Override
    public double modifyAltarRechargeRate(double initRate) {
        return initRate * 10;
    }

    @Override
    public double modifyMaxAltarPower(double initPower) {
        return initPower * 10;
    }

    @Override
    public double boostMaxAltarPower(double initPower) {
        return initPower + 1000;
    }
}
