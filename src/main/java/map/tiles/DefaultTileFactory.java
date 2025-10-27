package map.tiles;

import map.DungeonMapManager;

public class DefaultTileFactory implements TileFactory {
        DungeonMapManager mapManager;

        public DefaultTileFactory(DungeonMapManager dungeonMapManager) {
                this.mapManager = dungeonMapManager;
        }

        public Tile createWall(int y, int x) { return new Wall(y, x); }
        public Tile createFloor(int y, int x) { return new Floor(y, x); }
        public Tile createEntrance(int y, int x) {
                return new Entrance(y, x, this.mapManager);
        }
        //maybe quest object as exit condition
        public Tile createExit(int y, int x) {
                return new Exit(y, x, this.mapManager);
        }
}
