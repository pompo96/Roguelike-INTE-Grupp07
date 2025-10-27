package map.tiles;

import gameObject.GameObject;
import map.DungeonMapManager;
import player.Player;

public class Entrance extends Tile {
    private int movementModifier = 0;
    private final DungeonMapManager dungeonMapManager;
    public Entrance(int y, int x, DungeonMapManager dungeonMapManager) {
        super(y, x, true, '⨇');
        this.dungeonMapManager = dungeonMapManager;
    }


    public int getMovementModifier() {
        return movementModifier;
    }

    public void setMovementModifier(int movementModifier) {
        this.movementModifier = movementModifier;
    }

    @Override
    public void setTileContainer(GameObject object){
        if(object instanceof Player){
            dungeonMapManager.priorMap();
        }
    }

}
