package map;
import java.util.*;

import gameObject.GameObject;
import map.generation.GenerationStrategy;
import map.pathfinding.Directions;
import map.tiles.DefaultTileFactory;
import player.Player;

public class DungeonMapManager {
    private final DefaultTileFactory factory;
    private final Map<Integer, DungeonMap> maps;
    private int currentFloor;
    private Player player;
    private boolean dungeonCompleted = false;

    public DungeonMapManager(Player player) {
        this.factory = new DefaultTileFactory(this);
        this.maps = new HashMap<>();
        this.currentFloor = 0;
        this.player = player;
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

    public void spawnPlayerAt(int y, int x) {
        DungeonMap map = getMap();
        map.spawnAtLocation(y, x, player);
    }

    public boolean movePlayer(Directions dir) {
        if (player == null) throw new IllegalStateException("Player not set");
        DungeonMap map = getMap();
        return map.movePlayer(dir, player);
    }

    public boolean nextMap() {
        if(currentFloor + 1 < maps.size()) {
            currentFloor++;
            maps.get(currentFloor).spawnPlayerAtEntrance(player);
            maps.get(currentFloor).drawMap();
            return true;
        }else{
            dungeonCompleted = true;
            return false;
        }
    }

    public boolean priorMap() {
        if(currentFloor > 0) {
            currentFloor--;
            maps.get(currentFloor).spawnPlayerAtExit(player);
            maps.get(currentFloor).drawMap();
            return true;
        }else{
            return false;
        }
    }


    public boolean isDungeonCompleted() {
        return dungeonCompleted;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void placeObjectAt(int y, int x, GameObject object) {
        getMap().spawnAtLocation(y, x, object);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }
}
