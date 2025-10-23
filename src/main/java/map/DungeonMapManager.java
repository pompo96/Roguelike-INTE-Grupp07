package map;
import java.util.*;

import map.tileFactory.DefaultTileFactory;
import map.tileFactory.Tile;

public class DungeonMapManager {
    private final DefaultTileFactory factory = new DefaultTileFactory();
    private final Map<String, Tile[][]> maps = new HashMap<>();
    private Tile[][] currentMap;
    private String currentMapId;

    public DungeonMapManager(int height, int width) {
    }

    public Tile[][] getMap(){
    }

    public int getHeight(){
        return currentMap.length;
    }
    public int getWidth(){
        return currentMap[0].length;
    }
    public String render() {
    }
}
