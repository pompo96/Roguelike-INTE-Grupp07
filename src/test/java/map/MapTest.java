package map;

import equipment.Item;
import map.generation.GenerationStrategy;
import map.generation.SimpleFillStrategy;
import map.pathfinding.WalkablePathFinder;
import map.tiles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;
import testutils.MockFactory;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
    private DungeonMap map;
    private final int DEFAULT_HEIGHT = 10;
    private final int DEFAULT_WIDTH = 12;
    private DungeonMapManager mapManager;

    @BeforeEach
    void setUp() {
        GenerationStrategy strategy = new SimpleFillStrategy();
        Player mockPlayer = MockFactory.createMockPlayer();
        mapManager =  new DungeonMapManager(mockPlayer);
        mapManager.createMap(DEFAULT_HEIGHT, DEFAULT_WIDTH, strategy);
        map = mapManager.getMap();
    }

    @Test
    void testMapHasCorrectDimensions() {
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

        assertEquals(DEFAULT_HEIGHT, colLength);
        assertEquals(DEFAULT_WIDTH, rowLength);
        assertEquals((DEFAULT_HEIGHT*DEFAULT_WIDTH), gridSize);
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
    public void testSetAndClearTileContainer() {
        Item mockObject = MockFactory.createMockWeapon("mockSword", 50);
        int y = 2;
        int x = 3;
        map.spawnAtLocation(y, x, mockObject);
        Tile targetTile = map.getTileGrid()[y][x];
        assertNotNull(targetTile.getTileContainer());
        assertEquals(mockObject, targetTile.getTileContainer());
        targetTile.clearTileContainer();
        assertNull(targetTile.getTileContainer());
    }

    @Test
    void testDrawMap() {
        var outContent = new java.io.ByteArrayOutputStream();
        var originalOut = System.out;
        System.setOut(new java.io.PrintStream(outContent));
        map.drawMap();
        System.setOut(originalOut);
        String actual = outContent.toString().trim();

        var oracleOut = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(oracleOut));
        drawMap(map);
        System.setOut(originalOut);
        String expected = oracleOut.toString().trim();
        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }


    @Test
    public void testSpawnPlayerAtEntrance() {
        Player player = mapManager.getPlayer();
        map.spawnPlayerAtEntrance(player);


        WalkablePathFinder pathFinder = new WalkablePathFinder(map, '⨇', 'E');
        int[] coords = pathFinder.getWalkableTile(map.getEntrance());

        assertEquals(coords[0], player.getY());
        assertEquals(coords[1], player.getX());
    }

    @Test
    public void testSpawnPlayerAtExit() {
        Player player = mapManager.getPlayer();
        map.spawnPlayerAtExit(player);
        WalkablePathFinder pathFinder = new WalkablePathFinder(map, '⨇', 'E');
        int[] coords = pathFinder.getWalkableTile(map.getExit());
        assertEquals(coords[0], player.getY());
        assertEquals(coords[1], player.getX());
    }

    private boolean isBorder(int y, int x, int mapHeight, int mapWidth){
        boolean isTopWall = y == 0;
        boolean isBottomWall = y == mapHeight - 1;
        boolean isLeftWall = x == 0;
        boolean isRightWall = x == mapWidth - 1;

        return isTopWall || isBottomWall || isLeftWall || isRightWall;
    }


    private void drawMap(DungeonMap map) {
        StringBuilder sb = new StringBuilder();
        for(Tile[] row : map.getTileGrid()) {
            for(Tile tile : row){
                sb.append(tile.toString());
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }
}
