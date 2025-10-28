package ai;

import gameObject.GameObject;
import player.Player;

public class PlaceholderMob extends GameObject {
    private final int maximumHealth;
    private final int movementSpeed;
    private int currentHealth;
    private boolean hostile;
    private boolean combatAlert;
    private boolean combat;
    private boolean vulnerable;
    private Position currentPosition;
    private final Position spawnPoint;
    private static final int SPAWN_RADIUS = 20;
    private static final int AGGRO_RADIUS = 10;
    private static final int COMBAT_RADIUS = 40;


    public PlaceholderMob(Position position, char objectSymbol, int maximumHealth, int movementSpeed, boolean hostile){
        super(position.y, position.x, objectSymbol);
        this.maximumHealth = maximumHealth;
        this.currentHealth = maximumHealth;
        this.movementSpeed = movementSpeed;
        this.hostile = hostile;
        currentPosition = new Position(position.x, position.y);
        spawnPoint = new Position(position.x, position.y);
        combatAlert = false;
        combat = false;
        vulnerable = true;
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

    public boolean isVulnerable(){
        return vulnerable;
    }

    public boolean getCombatAlert(){
        return combatAlert;
    }

    public boolean inCombat(){
        return combat;
    }

    public int getMovementSpeed(){
        return movementSpeed;
    }

    public int getMaximumHealth() {
        return maximumHealth;
    }

    public int getCurrentHealth(){
        return currentHealth;
    }

    public int getSpawnRadius(){
        return SPAWN_RADIUS;
    }

    public int getAggroRadius(){
        return AGGRO_RADIUS;
    }

    public int getCombatRadius(){
        return COMBAT_RADIUS;
    }

    public void setHostile(boolean hostile){
        this.hostile = hostile;
    }

    public void setVulnerable(boolean vulnerable){
        this.vulnerable = vulnerable;
    }

    public void setCombatAlert(boolean combatAlert){
        this.combatAlert = combatAlert;
    }

    public void setCombat(boolean combat){
        this.combat = combat;
    }

    public void receiveAttack(int damage){
        if (vulnerable) {
            if(!combat) {
                setCombatAlert(true);
                setCombat(true);
            }
            updateCurrentHealth(-damage);
        }
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
