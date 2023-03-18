package net.techtastic.tat.dataloader.altar.augment;

public enum AugmentTarget {
    POWER_MULTIPLIER,
    RATE_MULTIPLIER,
    RANGE_MULTIPLIER,
    POWER_BOOST;

    @Override
    public String toString() {
        return switch (this) {
            case POWER_MULTIPLIER -> "power";
            case RATE_MULTIPLIER -> "rate";
            case RANGE_MULTIPLIER -> "range";
            case POWER_BOOST -> "boost";
            default -> null;
        };
    }

    public static AugmentTarget fromString(String type) {
        return switch (type) {
            case "power" -> POWER_MULTIPLIER;
            case "rate" -> RATE_MULTIPLIER;
            case "range" -> RANGE_MULTIPLIER;
            case "boost" -> POWER_BOOST;
            default -> throw new RuntimeException("Invalid target type: " + type);
        };
    }
}
