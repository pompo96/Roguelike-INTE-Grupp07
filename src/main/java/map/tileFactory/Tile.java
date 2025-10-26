package map.tileFactory;

import gameObject.GameObject;

public abstract class Tile extends GameObject {
    private int x,y;
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


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
