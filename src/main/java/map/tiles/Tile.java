package map.tiles;

import gameObject.GameObject;

public abstract class Tile extends GameObject {

    private final boolean isWalkable;
    private GameObject tileContainer;

    public Tile(int y, int x, boolean isWalkable, char tileSymbol) {
        super(y, x, tileSymbol);
        this.isWalkable = isWalkable;
    }
    public boolean isWalkable(){return isWalkable;}

    public void setTileContainer(GameObject tileContainer) {
        this.tileContainer = tileContainer;
    }
    public GameObject getTileContainer() {
        return tileContainer;
    }

    public void clearTileContainer() {
        tileContainer = null;
    }

}
