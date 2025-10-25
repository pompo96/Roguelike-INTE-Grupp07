package map.tiles;

import map.tileFactory.Tile;

public class Exit extends Tile {
    private int movementModifier = 0;

    public Exit(int y, int x, boolean isWalkable) {
        super(y, x, isWalkable, 'E');
    }

    public int getMovementModifier() {
        return movementModifier;
    }

    public void setMovementModifier(int movementModifier) {
        this.movementModifier = movementModifier;
    }
}
