package map;
import java.util.*;

import gameObject.GameObject;
import map.generation.GenerationStrategy;
import map.pathfinding.Directions;
import map.tiles.DefaultTileFactory;
import player.Player;

public class DungeonMapManager {
    private static final int MIN_MAP_HEIGHT = 5;
    private static final int MIN_MAP_WIDTH = 5;
    private static final int MAX_MAP_HEIGHT = 100;
    private static final int MAX_MAP_WIDTH = 100;

    private final DefaultTileFactory factory;
    private final Map<Integer, DungeonMap> maps;
    private int currentFloor;
    private final Player player;
    private boolean dungeonCompleted = false;

    public DungeonMapManager(Player player) {
        this.factory = new DefaultTileFactory(this);
        this.maps = new HashMap<>();
        this.currentFloor = 1;
        this.player = player;
    }

    public boolean createMap(int height, int width, GenerationStrategy strategy) throws IllegalArgumentException {
        if(height < MIN_MAP_HEIGHT || width < MIN_MAP_WIDTH || height > MAX_MAP_HEIGHT || width > MAX_MAP_WIDTH) {
            throw new IllegalArgumentException();
        }
        int mapKeyIndex = maps.size()+1;

        maps.put(mapKeyIndex, strategy.generate(factory, height, width));
        return maps.containsKey(mapKeyIndex);
    }

    public DungeonMap getMap(){
        return maps.get(currentFloor);
    }

    public void spawnPlayerAt(int y, int x) {
        DungeonMap map = getMap();
        map.spawnAtLocation(y, x, player);
    }

    public boolean movePlayer(Directions dir) {
        DungeonMap map = getMap();
        return map.movePlayer(dir, player);
    }

    public boolean nextMap() {
        if(currentFloor == maps.size()) {
            dungeonCompleted = true;
            return false;
        }
            currentFloor++;
            maps.get(currentFloor).spawnPlayerAtEntrance(player);
            maps.get(currentFloor).drawMap();
            return true;

    }

    public boolean priorMap() {
        if(currentFloor == 1) {
            return false;
        }
            currentFloor--;
            maps.get(currentFloor).spawnPlayerAtExit(player);
            maps.get(currentFloor).drawMap();
            return true;

    }

    public boolean isDungeonCompleted() {
        return dungeonCompleted;
    }

    public Player getPlayer() {
        return player;
    }

    public void placeObjectAt(int y, int x, GameObject object) {
        getMap().spawnAtLocation(y, x, object);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }
}
