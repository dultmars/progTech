package hu.nye.wumpusgame.model;

public enum Direction {
        NORTH("N"),
        EAST("E"),
        SOUTH("S"),
        WEST("W");
    private final String symbol;

    Direction(String symbol) {
        this.symbol = symbol;
    }

    public static Direction getByValue(String symbol) {
        for (Direction value : values()) {
            if (value.symbol.equals(symbol)) {
                return value;
            }
        }
        return null;
}

   public Direction left() {

        return  values()[(this.ordinal() + 1) % values().length];
   }
    public Direction right() {
        return values()[(this.ordinal() - 1 + values().length) % values().length];
    }
}
