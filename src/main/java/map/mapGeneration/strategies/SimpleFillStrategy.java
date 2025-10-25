package map.mapGeneration.strategies;


import map.DungeonMap;
import map.mapGeneration.GenerationStrategy;
import map.tileFactory.Tile;
import map.tileFactory.TileFactory;

public class SimpleFillStrategy implements GenerationStrategy {

    @Override
    public DungeonMap generate(TileFactory factory, int height, int width) {
        DungeonMap map = new DungeonMap(height, width);

        int entranceY = height - 2;
        int entranceX = width/2;
        int exitY = 1;
        int exitX = width/2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(isBorder(y,x,height, width)){
                    map.getTileGrid()[y][x] = factory.createWall(y,x);
                }else{
                    map.getTileGrid()[y][x] = factory.createFloor(y,x);
                }
            }
        }

        Tile entrance = factory.createEntrance(entranceY, entranceX);
        Tile exit = factory.createExit(exitY, exitX, true);

        map.getTileGrid()[entranceY][entranceX] = entrance;
        map.getTileGrid()[exitY][exitX] = exit;
        map.setEntrance(entrance);
        map.setExit(exit);

        return map;
    }

    private boolean isBorder(int y, int x, int mapHeight, int mapWidth){
        boolean isTopWall = y == 0;
        boolean isBottomWall = y == mapHeight - 1;
        boolean isLeftWall = x == 0;
        boolean isRightWall = x == mapWidth - 1;

        return isTopWall || isBottomWall || isLeftWall || isRightWall;
    }

}
