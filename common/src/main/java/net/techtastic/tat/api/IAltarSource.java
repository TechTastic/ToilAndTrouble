package net.techtastic.tat.api;

public interface IAltarSource {
    default double getRange() {
        return 0.0;
    }
    default void setRange(double newRange) {
    }

    default double getCurrentPower() {
        return 0.0;
    }
    default void setCurrentPower(double newPower) {
    }

    default double getMaxPower() {
        return 0.0;
    }
    default void setMaxPower(double newMaxPower) {
    }

    default double getRate() {
        return 0.0;
    }
    default void setRate(double newRate) {
    }

    default boolean drawPowerFromAltar(double amount) {
        if (amount < getCurrentPower()) {
            setCurrentPower(getCurrentPower() - amount);
            return true;
        }
        return false;
    }
}
