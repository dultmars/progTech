package hu.nye.wumpusgame.model;

public enum CellType {
    WALL("W"),
    WUMPUS("U"),
    PIT("P"),
    HERO("H"),
    GOLD("G"),
    EMPTY("_"),
    ;

    private final String sign;

    CellType(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public static CellType getByValue(String sign) {
        for (CellType value : values()) {
            if (value.sign.equals(sign)) {
                return value;
            }
        }
        return null;
    }
}
