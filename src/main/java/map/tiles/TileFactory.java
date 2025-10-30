package map.tiles;

public interface TileFactory {
        Tile createWall(int y, int x);
        Tile createFloor(int y, int x);
        Tile createEntrance(int y, int x);
        Tile createExit(int y, int x);
   }
