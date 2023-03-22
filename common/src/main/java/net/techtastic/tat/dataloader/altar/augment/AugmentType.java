package net.techtastic.tat.dataloader.altar.augment;

public enum AugmentType {
    LIGHT,
    CHALICE,
    HEAD,
    ARTHANA,
    NONE;

    static AugmentType fromString(String type) {
        return switch (type) {
            case "light" -> LIGHT;
            case "chalice" -> CHALICE;
            case "head" -> HEAD;
            case "arthana" -> ARTHANA;
            default -> NONE;
        };
    }

    String toString(AugmentType type) {
        return switch (type) {
            case LIGHT -> "light";
            case CHALICE -> "chalice";
            case HEAD -> "head";
            case ARTHANA -> "arthana";
            default -> "none";
        };
    }

    boolean isBlocking(AugmentType type) {
        return !type.equals(NONE);
    }
}
