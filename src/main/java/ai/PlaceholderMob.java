package ai;

import gameObject.GameObject;
import player.Player;

public class PlaceholderMob extends GameObject {
    private final int maximumHealth;
    private final int movementSpeed;
    private int currentHealth;
    private boolean hostile;
    private Position currentPosition;
    private final Position spawnPoint;


    public PlaceholderMob(Position position, char objectSymbol, int maximumHealth, int movementSpeed, boolean hostile){
        super(position.y, position.x, objectSymbol);
        this.maximumHealth = maximumHealth;
        this.currentHealth = maximumHealth;
        this.movementSpeed = movementSpeed;
        this.hostile = hostile;
        currentPosition = new Position(position.x, position.y);
        spawnPoint = new Position(position.x, position.y);
    }

    public int getX(){
        return currentPosition.x;
    }

    public int getY(){
        return currentPosition.y;
    }

    public Position getCurrentPosition(){
        return new Position(currentPosition.x, currentPosition.y);
    }

    public Position getSpawnPoint(){
        return new Position(spawnPoint.x, spawnPoint.y);
    }

    public boolean isHostile(){
        return hostile;
    }

    public int getMovementSpeed(){
        return movementSpeed;
    }

    public int getMaximumHealth() {
        return maximumHealth;
    }

    public void setHostile(boolean hostility){
        hostile = hostility;
    }

    public void updateCurrentHealth(int adjustment){
        int finalCurrentHealth = currentHealth + adjustment;
        if (finalCurrentHealth > maximumHealth) {
            finalCurrentHealth = maximumHealth;
        }
        this.currentHealth = finalCurrentHealth;
    }

    public void move(Position destination){
        currentPosition = destination;
    }

    public void attack(Player player){
        player.updateCurrentLife(-1);
    }


}
