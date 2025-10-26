package map;
import map.mapGeneration.GenerationStrategy;
import map.mapGeneration.strategies.SimpleFillStrategy;
import map.tileFactory.*;
import map.tiles.Entrance;
import map.tiles.Exit;
import map.tiles.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class DungeonMapManagerTest {
    private int seedHeight;
    private int seedWidth;
    private static final char START_SYMBOL = '⨇';
    private static final char END_SYMBOL = 'E';
    DungeonMapManager dungeonMapManager;
    GenerationStrategy strategy;

    @BeforeEach
    public void setUp() {
        this.seedHeight = 10;
        this.seedWidth = 10;
        strategy = new SimpleFillStrategy();
        dungeonMapManager = new DungeonMapManager();
        dungeonMapManager.makeMap(seedHeight, seedWidth, strategy);
    }
    //Todo test for swapping currentMap if exit reached.
    //Todo test having multiple maps
    //Todo make exit and entrace be connected to a map somehow(index)
    @Test
    public void constructor_SetsCorrectDimensions(){
        seedHeight = 5;
        seedWidth = 10;
        DungeonMapManager dungeonMapManager = new DungeonMapManager();
        dungeonMapManager.makeMap(seedHeight, seedWidth, strategy);

        int mapHeight = dungeonMapManager.getMap().getHeight();
        int mapWidth = dungeonMapManager.getMap().getWidth();

        assertEquals(mapHeight, seedHeight);
        assertEquals(mapWidth, seedWidth);
    }

    @ParameterizedTest
    @CsvSource(value = { "0,10", "10, 0"})
    public void nonPositiveDimensions_ThrowsException(int seedHeight, int seedWidth) {
        dungeonMapManager = new DungeonMapManager();
        assertThrows(IllegalArgumentException.class, () -> dungeonMapManager.makeMap(seedHeight, seedWidth, strategy));
    }

    //todo finish implementing this
//    @Test
//    public void drawMapMap_returnsCharGrid(){
//        String render = dungeonMapManager.drawMap();
//    }

    @Test
    public void testGridContainsNoEmptyTiles(){
        DungeonMap map = dungeonMapManager.getMap();
        for(Tile[] row : map.getTileGrid()){
            for(Tile tile : row){
                assertNotNull(tile);
            }
        }
    }

    /**
          ---- Tests ----
     Map is valid so, contains a walkable path between entrace and exit, no missing tiles,

     Todo below
     Test map entrance and exit dont spawn next to eachother, maybe minimum traversal distance?
     Test tile "sets" like bridge spawning a straight bridge x tiles long etc
     Test that floor spawns the proper map and uses proper structures and tiles
     */

    @Test
    public void playerWalk_UpdatesTiles(){

    }

    @Test
    public void playerWalkOverItem_PicksItemFromTile(){

    }

    @Test
    public void testPathExistsEntranceToExit() {
        DungeonMap map = dungeonMapManager.getMap();
        WalkablePathFinder pathFinder = new WalkablePathFinder(map, START_SYMBOL, END_SYMBOL);

        boolean exists = pathFinder.pathExists();
        dungeonMapManager.getMap().drawMap(); //todo remove

        assertTrue(exists);
    }

    @Test
    public void testPathDoesntExistEntranceToExit(){

    }

    @Test
    public void testMapBorderOnlyWalls(){
        DungeonMap generatedMap = dungeonMapManager.getMap();
        int mapHeight = generatedMap.getHeight();
        int mapWidth = generatedMap.getWidth();

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (isBorder(y, x, mapHeight, mapWidth)) {
                    assertInstanceOf(Wall.class, generatedMap.getTileGrid()[y][x]);
                }
            }

        }
    }

    private boolean isBorder(int y, int x, int mapHeight, int mapWidth){
        boolean isTopWall = y == 0;
        boolean isBottomWall = y == mapHeight - 1;
        boolean isLeftWall = x == 0;
        boolean isRightWall = x == mapWidth - 1;

        return isTopWall || isBottomWall || isLeftWall || isRightWall;
    }

    @Test
    void testHasEntranceAndExit() {
        DungeonMap map = dungeonMapManager.getMap();

        boolean hasEntrance = false;
        boolean hasExit = false;

        for (Tile[] row : map.getTileGrid()) {
            for (Tile tile : row) {
                if (tile instanceof Entrance) hasEntrance = true;
                if (tile instanceof Exit) hasExit = true;
            }
        }

        assertTrue(hasEntrance);
        assertTrue(hasExit);
    }
}
