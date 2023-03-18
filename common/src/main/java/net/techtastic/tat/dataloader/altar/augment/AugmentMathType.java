package net.techtastic.tat.dataloader.altar.augment;

enum AugmentMathType {
    ADD,
    MULTIPLY,
    MULTIPLY_ADD,
    NONE;

    public static AugmentMathType fromString(String type) {
        return switch (type) {
            case "add" -> ADD;
            case "multiply" -> MULTIPLY;
            case "multiply-add" -> MULTIPLY_ADD;
            default -> NONE;
        };
    }

    public String toString() {
        return switch (this) {
            case ADD -> "add";
            case MULTIPLY -> "multiply";
            case MULTIPLY_ADD -> "multiply-add";
            default -> "none";
        };
    }

    public double performOperation(double initValue, double modifier) {
        return switch (this) {
            case ADD -> initValue + modifier;
            case MULTIPLY -> initValue * modifier;
            case MULTIPLY_ADD -> initValue + initValue * modifier;
            case NONE -> initValue;
        };
    }
}
