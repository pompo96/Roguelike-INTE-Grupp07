package map;
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
    private final DefaultTileFactory tileFactory = new DefaultTileFactory();
    private int seedHeight;
    private int seedWidth;

    DungeonMapManager dungeonMapManager;

    @BeforeEach
    public void setUp() {
        this.seedHeight = 10;
        this.seedWidth = 10;
        dungeonMapManager = new DungeonMapManager(seedHeight, seedWidth);
    }

    @Test
    public void constructor_SetsCorrectDimensions(){
        seedHeight = 5;
        seedWidth = 10;
        DungeonMapManager dungeonMapManager = new DungeonMapManager(seedHeight, seedWidth);

        int mapHeight = dungeonMapManager.getHeight();
        int mapWidth = dungeonMapManager.getWidth();

        assertEquals(mapHeight, seedHeight);
        assertEquals(mapWidth, seedWidth);
    }

    @ParameterizedTest
    @CsvSource(value = { "-1,10", "10, -1"})
    public void negativeDimensions_ThrowsException(int width, int height){
        assertThrows(IllegalArgumentException.class, () -> {
            new DungeonMapManager(height, width);
        });
    }

    @Test
    public void renderMap_returnsCharGrid(){
        DungeonMapManager dungeonMapManager = new DungeonMapManager(5, 5);
        String render = dungeonMapManager.render();
    }

    @Test
    public void testGridContainsNoEmptyTiles(){
        DungeonMapManager dungeonMapManager = new DungeonMapManager(5, 5);
        Tile[][] map = dungeonMapManager.getMap();
        for(Tile[] row : map){
            for(Tile tile : row){
                assertNotNull(tile);
            }
        }
    }

    /**
        Invariant test scenarios
        1. entry exit always on border todo add not in corners too
        2. No Empty tiles
        3. corners are always walls or entry/exit
     ---- Tests ----
     Map is valid so, contains a walkable path between entrace and exit, no missing tiles,
     Test map entrance and exit dont spawn next to eachother, maybe minimum traversal distance?

     Test -> go from entrance to exit
     Test tile "sets" like bridge spawning a straight bridge x tiles long etc
     Test that floor spawns the proper map and uses proper structures and tiles
     */

    @Test
    public void testPathExistsEntranceToExit(){

    }

    @Test
    public void testMapBorderOnlyWalls(){
        Tile[][] generatedMap = dungeonMapManager.getMap();
        int mapHeight = dungeonMapManager.getHeight();
        int mapWidth = dungeonMapManager.getWidth();

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (isBorder(y, x, mapHeight, mapWidth)) {
                    assertInstanceOf(Wall.class, generatedMap[y][x]);
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
        Tile[][] map = dungeonMapManager.getMap();

        boolean hasEntrance = false;
        boolean hasExit = false;

        for (Tile[] row : map) {
            for (Tile tile : row) {
                if (tile instanceof Entrance) hasEntrance = true;
                if (tile instanceof Exit) hasExit = true;
            }
        }

        assertTrue(hasEntrance);
        assertTrue(hasExit);
    }
}
