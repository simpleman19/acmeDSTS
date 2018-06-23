package acme.pd;

public enum Direction {
    WEST("West"),
    NORTH("North"),
    EAST("East"),
    SOUTH("South"),
    NONE("None");

    Direction(String name) {
        this.displayValue = name;
    }

    private String displayValue;

    public String getDisplayValue() {
        return displayValue;
    }

    @Override
    public String toString() {
        return displayValue;
    }
}
