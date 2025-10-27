package map.pathfinding;

public enum Directions {
    EAST(0, 1),
    WEST(0, -1),
    NORTH(-1, 0),
    SOUTH(1, 0),
    SOUTHWEST(1, -1),
    SOUTHEAST(1,1),
    NORTHWEST(-1,-1),
    NORTHEAST(-1, 1);

    private final int column;
    private final int row;

    Directions(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
