package map.tiles;

import gameObject.GameObject;
import map.DungeonMapManager;
import player.Player;

public class Exit extends Tile {
    private int movementModifier = 0;
    private final DungeonMapManager dungeonMapManager;
    public Exit(int y, int x, DungeonMapManager dungeonMapManager) {
        super(y, x, true, 'E');
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
            dungeonMapManager.nextMap();
        }
    }
}
