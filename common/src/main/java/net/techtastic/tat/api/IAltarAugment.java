package net.techtastic.tat.api;

public interface IAltarAugment {
    default String getType() {return "none";}
    default boolean isOverridenByType() {
        return false;
    }
    default int getPriority() {return 100;}
    default double modifyPower(double power) {return power;}
    default double modifyRange(double range) {return range;}
    default double modifyRate(double rate) {return rate;}
}
