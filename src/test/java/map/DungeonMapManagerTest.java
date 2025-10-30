package map;
import equipment.Item;
import map.pathfinding.Directions;
import map.generation.GenerationStrategy;
import map.generation.SimpleFillStrategy;
import map.tiles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import player.Player;
import testutils.MockFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 ---- Tests ----
 Map is valid so, contains a walkable path between entrace and exit, no missing tiles,

 Todo below
 Test map entrance and exit dont spawn next to eachother, maybe minimum traversal distance?
 Test tile "sets" like bridge spawning a straight bridge x tiles long etc
 Test that floor spawns the proper map and uses proper structures and tiles

 //todo finish implementing this
 //    @Test
 //    public void drawMapMap_returnsCharGrid(){
 //        String render = dungeonMapManager.drawMap();
 //    }
 //Todo test for swapping currentMap if exit reached.
 //Todo test having multiple maps
 //Todo make exit and entrace be connected to a map somehow(index)

 */

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DungeonMapManagerTest {
    DungeonMapManager dungeonMapManager;
    GenerationStrategy strategy;
    Player mockPlayer;

    @BeforeEach
    public void setUp() {
        strategy = new SimpleFillStrategy();
        mockPlayer = MockFactory.createMockPlayer();
        dungeonMapManager = new DungeonMapManager(mockPlayer);
        dungeonMapManager.createMap(10, 10, strategy);

    }

    @ParameterizedTest
    @CsvSource(value = { "10,5", "5,10", "5,5", "50,73","100,100"})
    public void testValidDimensionsCreatesMap(int height, int width) {
        assertTrue(dungeonMapManager.createMap(height, width, strategy));
    }

    @ParameterizedTest
    @CsvSource(value = { "4,10", "10,4", "-1,10", "10,-1","101,10","10,101"})
    public void testInvalidDimensionsThrowsException(int height, int width) {
        assertThrows(IllegalArgumentException.class, () -> dungeonMapManager.createMap(height, width, strategy));
    }

    @Test
    public void testPlayerMovement_UpdatesPlayerCoordinates() {
        dungeonMapManager.spawnPlayerAt(5, 5);

        for (Directions dir : Directions.values()) {
            int oldY = mockPlayer.getY();
            int oldX = mockPlayer.getX();

            boolean moved = dungeonMapManager.movePlayer(dir);

            int expectedY = oldY + dir.getColumn();
            int expectedX = oldX + dir.getRow();

            assertTrue(moved);
            assertEquals(expectedY, mockPlayer.getY());
            assertEquals(expectedX, mockPlayer.getX());
        }
    }

    @Test
    public void testPlayerMovement_AllMoveableDirections() {
        dungeonMapManager.spawnPlayerAt(5, 5);

        for (Directions dir : Directions.values()) {
            DungeonMap map = dungeonMapManager.getMap();
            Tile[][] grid = map.getTileGrid();
            int oldY = mockPlayer.getY();
            int oldX = mockPlayer.getX();

            assertTrue(dungeonMapManager.movePlayer(dir));

            int newY = mockPlayer.getY();
            int newX = mockPlayer.getX();

            boolean newTileHasPlayer = grid[newY][newX].getTileContainer() instanceof Player;
            boolean oldTileHasPlayer = grid[oldY][oldX].getTileContainer() instanceof Player;

            assertTrue(newTileHasPlayer);
            assertFalse(oldTileHasPlayer);
        }
    }


    @Test
    void playerWalksOverItem_ItemIsLooted() {
        Item mockSword = MockFactory.createMockWeapon("newSword", 100);
        dungeonMapManager.spawnPlayerAt(3, 3);
        dungeonMapManager.placeObjectAt(4, 3, mockSword);
        Tile tile = dungeonMapManager.getMap().getTileGrid()[4][3];
        assertEquals(tile.getTileContainer(), mockSword);
        dungeonMapManager.movePlayer(Directions.SOUTH);
        assertEquals(tile.getTileContainer(), mockPlayer);
    }

    @Test
    public void playerWalksOntoEntrance_FloorZero_DoesNotChangeFloor() {
        dungeonMapManager.createMap(5, 5, strategy);
        int before = dungeonMapManager.getCurrentFloor();
        boolean moved = dungeonMapManager.priorMap();
        assertFalse(moved);
        assertEquals(before, dungeonMapManager.getCurrentFloor());
    }

    @Test
    public void playerWalksOntoEntrance_FloorGreaterThanZero_MovesUp() {
        dungeonMapManager.createMap(5, 5, strategy);
        dungeonMapManager.createMap(5, 5, strategy);

        assertEquals(1, dungeonMapManager.getCurrentFloor());
        assertTrue(dungeonMapManager.nextMap());
        assertEquals(2, dungeonMapManager.getCurrentFloor());
        assertTrue(dungeonMapManager.priorMap());
        assertEquals(1, dungeonMapManager.getCurrentFloor());
    }

    @Test
    public void playerWalksOntoExit_NotLastFloor_MovesDown() {
        dungeonMapManager.createMap(10, 15, strategy);

        assertEquals(1, dungeonMapManager.getCurrentFloor());
        assertTrue(dungeonMapManager.nextMap());
        assertEquals(2, dungeonMapManager.getCurrentFloor());
    }

    @ParameterizedTest
    @CsvSource(value = {"1,2,3"})
    public void playerWalksOntoExit_LastFloor_EndsDungeon(int floorOne, int floorTwo, int floorThree) {
        dungeonMapManager.createMap(5, 5, strategy);
        dungeonMapManager.createMap(5, 5, strategy);

        assertEquals(floorOne, dungeonMapManager.getCurrentFloor());
        assertTrue(dungeonMapManager.nextMap());
        assertEquals(floorTwo, dungeonMapManager.getCurrentFloor());
        assertTrue(dungeonMapManager.nextMap());
        assertEquals(floorThree, dungeonMapManager.getCurrentFloor());
        assertFalse(dungeonMapManager.nextMap());
        assertTrue(dungeonMapManager.isDungeonCompleted());
    }
}
