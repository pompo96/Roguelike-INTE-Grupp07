package map.tiles;

import map.tileFactory.Tile;

public class Wall extends Tile {
    public Wall(int y, int x) {
        super(y, x, false, '#');
    }
}
