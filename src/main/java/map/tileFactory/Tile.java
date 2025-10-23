package map.tileFactory;

public abstract class Tile {
    protected int x,y;
    private final boolean isWalkable;
    private final char tileSymbol;

    public Tile(int y, int x, boolean isWalkable, char tileSymbol) {
        this.x = x;
        this.y = y;
        this.tileSymbol = tileSymbol;
        this.isWalkable = isWalkable;
    }

    public boolean isWalkable(){return isWalkable;}
    public char getTileSymbol(){return tileSymbol;}

    @Override
    public String toString() {
        return String.valueOf(tileSymbol);
    }
}
