package map.tiles;

import gameObject.GameObject;
import map.tileFactory.Tile;

public class Floor extends Tile {
    private int movementModifier = 0;

    public Floor(int y, int x) {
        super(y, x, true, '▧');
    }

    public int getMovementModifier() {
        return movementModifier;
    }

    public void setMovementModifier(int movementModifier) {
        this.movementModifier = movementModifier;
    }
}
