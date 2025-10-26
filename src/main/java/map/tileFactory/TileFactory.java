package map.tileFactory;

import map.DungeonMapManager;

public interface TileFactory {
        Tile createWall(int y, int x);
        Tile createFloor(int y, int x);
        Tile createEntrance(int y, int x);
        Tile createExit(int y, int x);
        Tile createTestTile(int y, int x);
   }
