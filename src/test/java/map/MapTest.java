package map;

import map.generation.GenerationStrategy;
import map.generation.SimpleFillStrategy;
import map.tiles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;
import testutils.MockFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests structural and content integrity of the DungeonMap itself.
 * Ensures that the map grid is fully populated, borders are correct,
 * and entrance/exit tiles are present and valid.
 */
public class MapTest {
    private DungeonMap map;

    @BeforeEach
    void setUp() {
        GenerationStrategy strategy = new SimpleFillStrategy();
        Player mockPlayer = MockFactory.createMockPlayer();
        DungeonMapManager dungeonMapManager = new DungeonMapManager(mockPlayer);
        dungeonMapManager.makeMap(10, 10, strategy);
        map = dungeonMapManager.getMap();
    }

    @Test
    void testMapHasCorrectDimensions() {
        int height = 10;
        int width = 10;
        int colLength = 0;
        int gridSize = 0;
        int rowLength;

        for (Tile[] row : map.getTileGrid()) {
            colLength++;
            for (Tile tile : row) {
                gridSize++;
            }
        }
        rowLength = gridSize / colLength;

        assertEquals(height, colLength);
        assertEquals(width, rowLength);
        assertEquals((height*width), gridSize);
    }

    @Test
    void testMapGridIsFullyInitialized() {
        for (Tile[] row : map.getTileGrid()) {
            for (Tile tile : row) {
                assertNotNull(tile);
            }
        }
    }

    @Test
    void testMapBordersAreWalls() {
        int height = map.getHeight();
        int width = map.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isBorder(y, x, height, width)) {
                    assertInstanceOf(Wall.class, map.getTileGrid()[y][x]);
                }
            }
        }
    }

    @Test
    void testMapContainsEntranceAndExit() {
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

    @Test
    public void testSetAndClearTileContainer(){

    }

    @Test
    public void testDrawMap(){

    }
    private boolean isBorder(int y, int x, int mapHeight, int mapWidth){
        boolean isTopWall = y == 0;
        boolean isBottomWall = y == mapHeight - 1;
        boolean isLeftWall = x == 0;
        boolean isRightWall = x == mapWidth - 1;

        return isTopWall || isBottomWall || isLeftWall || isRightWall;
    }
}
