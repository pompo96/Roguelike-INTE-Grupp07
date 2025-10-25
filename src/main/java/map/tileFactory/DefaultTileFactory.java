package map.tileFactory;

import map.tiles.*;

public class DefaultTileFactory implements TileFactory {
        public Tile createWall(int y, int x) { return new Wall(y, x); }
        public Tile createFloor(int y, int x) { return new Floor(y, x); }
        public Tile createEntrance(int y, int x) { return new Entrance(y, x); }
        //maybe quest object as exit condition
        public Tile createExit(int y, int x, boolean exitCondition) { return new Exit(y, x, exitCondition); }
        public Tile createTestTile(int y, int x) { return new TestTile(y, x); }
}
