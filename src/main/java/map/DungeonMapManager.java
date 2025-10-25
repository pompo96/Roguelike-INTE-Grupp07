package map;
import java.util.*;

import map.mapGeneration.GenerationStrategy;
import map.tileFactory.DefaultTileFactory;
import map.tileFactory.Tile;

public class DungeonMapManager {
    private final DefaultTileFactory factory;
    private final Map<Integer, DungeonMap> maps;
    private DungeonMap currentMap;
    private int floor;

    public DungeonMapManager() {
        this.factory = new DefaultTileFactory();
        this.maps = new HashMap<>();
        this.floor = 0;
    }

    public void makeMap(int height, int width, GenerationStrategy strategy) throws IllegalArgumentException {
        if(height < 1 || width < 1) {
            throw new IllegalArgumentException();
        }
        currentMap = strategy.generate(factory, height, width);
        floor++;
        maps.put(floor, currentMap);
    }

    public DungeonMap getMap(){
        return currentMap;
    }


    public String drawMap() {
        StringBuilder sb = new StringBuilder();
        for(Tile[] row : currentMap.getTileGrid()){
            for(Tile tile : row){
                sb.append(tile.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
