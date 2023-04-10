package net.techtastic.tat.api.altar.augment;

public interface IAltarAugment {
    String getType();

    int getTypePriority();

    boolean matches(IAltarAugment augment);

    default double modifyMaxAltarPower(double initPower) {
        return initPower;
    }

    default double modifyAltarRange(double initRange) {
        return initRange;
    }

    default double modifyAltarRechargeRate(double initRate) {
        return initRate;
    }

    default double boostMaxAltarPower(double initPower) {
        return initPower;
    }

    default double boostAltarRange(double initRange) {
        return initRange;
    }

    default double boostAltarRechargeRate(double initRate) {
        return initRate;
    }
}
