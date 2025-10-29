package map;

import map.tileFactory.Tile;

public class DungeonMap {
    private Tile[][] map;
    private Tile entrance;
    private Tile exit;
    private final int height;
    private final int width;

    public DungeonMap(int Height, int Width) {
        this.map = new Tile[Height][Width];
        this.height = Height;
        this.width = Width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile[][] getTileGrid() {
        return map;
    }

    public Tile getEntrance() {
        return entrance;
    }

    public void setEntrance(Tile entrance) {
        this.entrance = entrance;
    }

    public Tile getExit() {
        return exit;
    }

    public void setExit(Tile exit) {
        this.exit = exit;
    }
}
