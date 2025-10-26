package ai;

import gameObject.GameObject;
import player.Player;

public class PlaceholderMob extends GameObject {
    private final int maximumHealth;
    private int currentHealth;
    private final boolean hostile;
    private Position currentPosition;
    private final Position spawnPoint;
    private final Player player;

    public PlaceholderMob(Position position, char objectSymbol, int maximumHealth, boolean hostile, Player player){
        super(position.y, position.x, objectSymbol);
        this.maximumHealth = maximumHealth;
        this.currentHealth = maximumHealth;
        this.hostile = hostile;
        this.player = player;
        currentPosition = new Position(position.x, position.y);
        spawnPoint = new Position(position.x, position.y);
    }

    public int getX(){
        return currentPosition.x;
    }

    public int getY(){
        return currentPosition.y;
    }

    public Position getSpawnPoint(){
        return new Position(spawnPoint.x, spawnPoint.y);
    }

    public boolean isHostile(){
        return hostile;
    }

    public void move(Position destination){
        currentPosition = destination;
    }

    public void attack(){
        player.updateCurrentLife(-1);
    }

    public void updateCurrentHealth(int adjustment){
        int finalCurrentHealth = currentHealth + adjustment;
        if (finalCurrentHealth > this.maximumHealth) {
            finalCurrentHealth = this.maximumHealth;
        }
        this.currentHealth = finalCurrentHealth;
    }
}
