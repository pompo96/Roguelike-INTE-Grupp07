package map;

import map.generation.GenerationStrategy;
import map.generation.SimpleFillStrategy;
import map.pathfinding.Directions;
import map.pathfinding.WalkablePathFinder;
import map.tiles.DefaultTileFactory;
import map.tiles.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;
import testutils.MockFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenerationStrategyTest {
    private static final char START_SYMBOL = '⨇';
    private static final char END_SYMBOL = 'E';
    DungeonMapManager dungeonMapManager;
    GenerationStrategy strategy;

    @BeforeEach
    public void setUp() {
        strategy = new SimpleFillStrategy();
        Player mockPlayer = MockFactory.createMockPlayer();
        dungeonMapManager = new DungeonMapManager(mockPlayer);
        dungeonMapManager.createMap(10, 10, strategy);
    }

    @Test
    public void testPathExistsEntranceToExit() {
        DungeonMap map = dungeonMapManager.getMap();
        WalkablePathFinder pathFinder = new WalkablePathFinder(map, START_SYMBOL, END_SYMBOL);

        boolean exists = pathFinder.pathExists();

        assertTrue(exists);
    }

    @Test
    public void testPathDoesntExistEntranceToExit(){
        DungeonMap map = dungeonMapManager.getMap();
        blockEntrance(map);
        WalkablePathFinder pathFinder = new WalkablePathFinder(map, START_SYMBOL, END_SYMBOL);
        boolean exists = pathFinder.pathExists();

        assertFalse(exists);
    }

    private void blockEntrance(DungeonMap map){
        DefaultTileFactory factory = new DefaultTileFactory(dungeonMapManager);
        int column = map.getEntrance().getY();
        int row = map.getEntrance().getX();

        for (Directions dir : Directions.values()) {
            int newColumn = column + dir.getColumn();
            int newRow = row + dir.getRow();

            Tile wall = factory.createWall(newRow, newColumn);
            map.getTileGrid()[newColumn][newRow] = wall;
        }
    }

}
