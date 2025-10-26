package map.mapGeneration.strategies;

import map.DungeonMap;
import map.WalkablePathFinder;
import map.mapGeneration.GenerationStrategy;
import map.tileFactory.Tile;
import map.tileFactory.TileFactory;

import java.util.Random;

public class CellularAutomata implements GenerationStrategy {
    //0-100 45-60
    private static final int NOISE_DENSITY = 50;
    private static final int SMOOTHING_ITERATIONS = 1;

    @Override
    public DungeonMap generate(TileFactory factory, int height, int width) {
        return guaranteeWalkableAutomata(factory, height, width);
    }

    private DungeonMap guaranteeWalkableAutomata(TileFactory factory, int height, int width) {
        DungeonMap walkableMap;
        WalkablePathFinder pathFinder;
//        int wastedMaps = 0;
        do {
            walkableMap = generateNoiseGrid(factory, height, width);
            applyCellularAutomaton(walkableMap, factory, height, width);
            pathFinder = new WalkablePathFinder(walkableMap, '⨇', 'E');
//            System.out.println("wasted maps: " + ++wastedMaps);
        } while(!pathFinder.pathExists());

        return walkableMap;
    }

    private DungeonMap generateNoiseGrid(TileFactory factory, int height, int width) {
        DungeonMap noiseGrid = new DungeonMap(height,width);
        Random rand = new Random();
        int randInt;

        for(int col = 0; col < height; col++) {
            for(int row = 0; row < width; row++) {
                randInt = rand.nextInt(1,100);
                if((randInt > NOISE_DENSITY) && !isBorder(col, row, height, width)){
                    noiseGrid.getTileGrid()[col][row] = factory.createFloor(col, row);
                }else{
                    noiseGrid.getTileGrid()[col][row] = factory.createWall(col, row);
                }
            }
        }
        return noiseGrid;
    }

    private void applyCellularAutomaton(DungeonMap map, TileFactory factory, int height, int width) {
        int entranceY = height - 2;
        int entranceX = width/2;
        int exitY = 1;
        int exitX = width/2;

        for (int i = 0; i < SMOOTHING_ITERATIONS; i++) {
            Tile[][] tempGrid = copyGrid(map.getTileGrid(), factory);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int neighborWallCount = countWallNeighbors(tempGrid, x, y);

                    if (neighborWallCount > 4) {
                        map.getTileGrid()[y][x] = factory.createWall(y, x);
                    } else {
                        map.getTileGrid()[y][x] = factory.createFloor(y, x);
                    }
                }
            }
        }

        map.getTileGrid()[entranceY][entranceX] = factory.createEntrance(entranceY, entranceX);
        map.getTileGrid()[exitY][exitX] = factory.createExit(exitY, exitX);
    }

    private int countWallNeighbors(Tile[][] grid, int x, int y) {
        int height = grid.length;
        int width = grid[0].length;
        int wallCount = 0;

        for (int ny = y - 1; ny <= y + 1; ny++) {
            for (int nx = x - 1; nx <= x + 1; nx++) {
                if (isWithinBounds(nx, ny, width, height)) {
                    if (nx != x || ny != y) {
                        if (!grid[ny][nx].isWalkable()) {
                            wallCount++;
                        }
                    }
                } else {
                    // Out of bounds counts as a wall
                    wallCount++;
                }
            }
        }
        return wallCount;
    }

    private boolean isWithinBounds(int x, int y, int width, int height) {
        return y >= 0 && y < height && x >= 0 && x < width;
    }

    private Tile[][] copyGrid(Tile[][] source, TileFactory factory) {
        int height = source.length;
        int width = source[0].length;
        Tile[][] copy = new Tile[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = source[y][x];
                if (!tile.isWalkable()) {
                    copy[y][x] = factory.createWall(y, x);
                } else {
                    copy[y][x] = factory.createFloor(y, x);
                }
            }
        }
        return copy;
    }

    private boolean isBorder(int y, int x, int mapHeight, int mapWidth){
        boolean isTopWall = y == 0;
        boolean isBottomWall = y == mapHeight - 1;
        boolean isLeftWall = x == 0;
        boolean isRightWall = x == mapWidth - 1;

        return isTopWall || isBottomWall || isLeftWall || isRightWall;
    }
}
