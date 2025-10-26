package map;
import java.util.*;

import map.mapGeneration.GenerationStrategy;
import map.tileFactory.DefaultTileFactory;
import player.Player;

public class DungeonMapManager {
    private final DefaultTileFactory factory;
    private final Map<Integer, DungeonMap> maps;
    private int currentFloor;

    public DungeonMapManager() {
        this.factory = new DefaultTileFactory(this);
        this.maps = new HashMap<>();
        this.currentFloor = 0;
    }

    public void makeMap(int height, int width, GenerationStrategy strategy) throws IllegalArgumentException {
        if(height < 1 || width < 1) {
            throw new IllegalArgumentException();
        }
        maps.put(currentFloor, strategy.generate(factory, height, width));
    }

    public DungeonMap getMap(){
        return maps.get(currentFloor);
    }

    public void nextMap(Player player) {
        if(currentFloor + 1 < maps.size()) {
            currentFloor++;
            maps.get(currentFloor).spawnAtEntrance(player);
            maps.get(currentFloor).drawMap();

        }
        //victory
    }

    public void priorMap(Player player) {
        if(currentFloor > 0) {
            currentFloor--;
            maps.get(currentFloor).spawnAtExit(player);
            maps.get(currentFloor).drawMap();
        }
        //player.updateCurrentLife(0);
    }
}
