package map;
import equipment.Item;
import gameObject.GameObject;
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
        dungeonMapManager.makeMap(10, 10, strategy);

    }

    @Test
    public void testPlayerMovement_UpdatesTilesCorrectly() {
        dungeonMapManager.setPlayer(mockPlayer);
        dungeonMapManager.spawnPlayerAt(5, 5);

        for (Directions dir : Directions.values()) {
            DungeonMap map = dungeonMapManager.getMap();
            Tile[][] grid = map.getTileGrid();

            int oldY = mockPlayer.getY();
            int oldX = mockPlayer.getX();

            boolean moved = dungeonMapManager.movePlayer(dir);
            assertTrue(moved);

            int newY = mockPlayer.getY();
            int newX = mockPlayer.getX();

            boolean newTileHasPlayer = grid[newY][newX].getTileContainer() instanceof Player;
            boolean oldTileHasPlayer = grid[oldY][oldX].getTileContainer() instanceof Player;

            assertTrue(newTileHasPlayer);
            assertFalse(oldTileHasPlayer);
        }
    }

    @Test
    public void testPlayerMovement_UpdatesPlayerCoordinates() {
        dungeonMapManager.setPlayer(mockPlayer);
        dungeonMapManager.spawnPlayerAt(5, 5);

        for (Directions dir : Directions.values()) {
            int oldY = mockPlayer.getY();
            int oldX = mockPlayer.getX();

            boolean moved = dungeonMapManager.movePlayer(dir);

            int expectedY = oldY + dir.getRow();
            int expectedX = oldX + dir.getColumn();

            assertTrue(moved);
            assertEquals(expectedY, mockPlayer.getY());
            assertEquals(expectedX, mockPlayer.getX());
        }
    }


    @ParameterizedTest
    @CsvSource(value = { "0,10", "10, 0"})
    public void nonPositiveDimensions_ThrowsException(int height, int width) {
        assertThrows(IllegalArgumentException.class, () -> dungeonMapManager.makeMap(height, width, strategy));
    }

//    //broken due to shitty class hierarchy
//    @Test
//    void playerWalksOverItem_GetsNewItem() {
//        dungeonMapManager.setPlayer(mockPlayer);
//
//        dungeonMapManager.spawnPlayerAt(3, 3);
//        dungeonMapManager.placeObjectAt(4, 3, MockFactory.createMockWeapon("newSword", 100));
//        dungeonMapManager.movePlayer(Directions.SOUTH);
//
//        assertTrue(mockPlayer.hasItem("newSword"));
//    }

    @Test
    public void playerWalksOntoEntrance_FloorZero_DoesNotChangeFloor() {
        DungeonMapManager manager = new DungeonMapManager(mockPlayer);
        GenerationStrategy strategy = new SimpleFillStrategy();
        manager.makeMap(5, 5, strategy);
        Player mockPlayer = MockFactory.createMockPlayer();
        manager.setPlayer(mockPlayer);

        int before = manager.getCurrentFloor();

        boolean moved = manager.priorMap();

        assertFalse(moved);
        assertEquals(before, manager.getCurrentFloor());
    }

    @Test
    public void playerWalksOntoEntrance_FloorGreaterThanZero_MovesUp() {
        DungeonMapManager manager = new DungeonMapManager(mockPlayer);
        GenerationStrategy strategy = new SimpleFillStrategy();
        manager.makeMap(5, 5, strategy);
        manager.makeMap(5, 5, strategy);
        manager.setPlayer(MockFactory.createMockPlayer());


        manager.nextMap();
        assertEquals(1, manager.getCurrentFloor());

        boolean moved = manager.priorMap();

        assertTrue(moved);
        assertEquals(0, manager.getCurrentFloor());
    }

    @Test
    public void playerWalksOntoExit_NotLastFloor_MovesDown() {
        DungeonMapManager manager = new DungeonMapManager(mockPlayer);
        GenerationStrategy strategy = new SimpleFillStrategy();
        manager.makeMap(5, 5, strategy);
        manager.makeMap(5, 5, strategy);
        manager.makeMap(5, 5, strategy);
        manager.setPlayer(MockFactory.createMockPlayer());

        assertEquals(0, manager.getCurrentFloor());
        boolean moved = manager.nextMap();

        assertTrue(moved);
        assertEquals(1, manager.getCurrentFloor());
    }

    @Test
    public void playerWalksOntoExit_LastFloor_EndsDungeon() {
        DungeonMapManager manager = new DungeonMapManager(mockPlayer);
        GenerationStrategy strategy = new SimpleFillStrategy();
        manager.makeMap(5, 5, strategy);
        manager.makeMap(5, 5, strategy);
        manager.setPlayer(MockFactory.createMockPlayer());

        manager.nextMap();
        assertEquals(1, manager.getCurrentFloor());

        boolean moved = manager.nextMap();

        assertFalse(moved);
        assertTrue(manager.isDungeonCompleted());
    }


}
