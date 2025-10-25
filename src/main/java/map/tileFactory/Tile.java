package map.tileFactory;

import gameObject.GameObject;

public abstract class Tile extends GameObject {
    protected int x,y;
    private final boolean isWalkable;

    public Tile(int y, int x, boolean isWalkable, char tileSymbol) {
        super(y, x, tileSymbol);
        this.isWalkable = isWalkable;
    }
    public boolean isWalkable(){return isWalkable;}

}
