package map.tileFactory;

public interface TileFactory {
        Tile createWall(int y, int x);
        Tile createFloor(int y, int x);
        Tile createEntrance(int y, int x);
        Tile createExit(int y, int x, boolean exitCondition);
        Tile createTestTile(int y, int x);
   }
