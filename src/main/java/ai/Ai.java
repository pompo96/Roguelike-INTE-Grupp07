package ai;

import player.Player;

import java.util.Random;
import java.util.Set;

public class Ai {
    private final PlaceholderMob mob;
    private final Player player;
    private MobState state;
    private int idleFrames;
    private Position destination;


    public Ai(PlaceholderMob mob, Player player){
        this.mob = mob;
        this.player = player;
        state = MobState.IDLE;
        idleFrames = 0;
        destination = mob.getSpawnPoint();
    }

    public PlaceholderMob getMob() {
        return mob;
    }

    public Player getPlayer(){
        return player;
    }

    public MobState getState() {
        return state;
    }

    public Position getDestination(){
        return new Position(destination.x, destination.y);
    }

    public void update(){
        if(mob.getCurrentHealth() < 1)
            state = MobState.DEAD;
        else if(mob.getCombatAlert()){
            mob.setCombatAlert(false);
            beginCombat();
        }
        else {
            switch (state) {
                case IDLE:
                    if (areaIsClear()) {
                        if (idleFrames > 1)
                            beginPatrolling();
                        else
                            idleFrames++;

                    }
                    break;

                case PATROLLING:
                    if (areaIsClear())
                        moveToDestination();
                    break;

                case COMBAT:
                    if (playerOutOfCombatRadius())
                        beginReset();
                    else
                        engagePlayer();
                    break;
                case RESET:
                    moveToDestination();
                    break;
            }
        }
    }

    void beginIdle(){
        state = MobState.IDLE;
        idleFrames = 0;
        mob.setVulnerable(true);
    }

    void beginCombat(){
        state = MobState.COMBAT;
        player.engageMob(mob);
    }
    private void beginReset(){
        state = MobState.RESET;
        mob.setCombat(false);
        mob.setVulnerable(false);
        mob.updateCurrentHealth(mob.getMaximumHealth() - mob.getCurrentHealth());
        player.disengageMob(mob);
        destination = getMob().getSpawnPoint();
    }

    void beginPatrolling(){
        state = MobState.PATROLLING;
        setDestination(generatePatrollingDestination());
    }

    public void setDestination(Position destination) {
        this.destination = destination;
    }

    Position generatePatrollingDestination(){
        Position candidate;
        do {
            Random rand = new Random();
            double angle = rand.nextDouble() * 2 * Math.PI;
            double distance = rand.nextDouble() * mob.getSpawnRadius();
            int dx = (int) Math.round(Math.cos(angle) * distance);
            int dy = (int) Math.round(Math.sin(angle) * distance);
            candidate = new Position(mob.getX() + dx, mob.getY() + dy);
        }
        while(candidate.equals(mob.getCurrentPosition()) || candidate.x < 2 || candidate.y < 2);
        return candidate;
    }

    void moveToDestination(){
        int xStep = mob.getX();
        int yStep = mob.getY();
        boolean destinationReached = false;
        for(int i = 0; i < mob.getMovementSpeed(); i++){
            if(destination.x == xStep && destination.y == yStep){
                destinationReached = true;
                break;
            }
            else{
                xStep+= Integer.compare(destination.x, xStep);
                yStep+= Integer.compare(destination.y, yStep);
            }
        }
        mob.move(new Position(xStep, yStep));
        if (destinationReached)
            beginIdle();
    }

    boolean areaIsClear(){
        if(!mob.isHostile())
            return true;
        else {
            Position playerPos = new Position(player.getX(), player.getY());
            if(isInAggroZone(playerPos)) {
                beginCombat();
                return false;
            }
            else {
                Set<PlaceholderMob> allMobsInCombat = player.getEngagedMobs();
                for(PlaceholderMob currentMob : allMobsInCombat){
                    if(currentMob.isHostile() && isInAggroZone(currentMob.getCurrentPosition())){
                        beginCombat();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    boolean isInAggroZone(Position pos){
        double distanceToMob = calculateDistance(pos, mob.getCurrentPosition());
        return distanceToMob <= mob.getAggroRadius();
    }

    /*boolean playerOutOfCombatRadius(){
        double distanceToPlayer = Math.sqrt( Math.pow(player.getX() - mob.getSpawnPoint().x, 2) +
                Math.pow(player.getY() - mob.getSpawnPoint().y, 2) );
        return distanceToPlayer > mob.getCombatRadius();}*/

    boolean playerOutOfCombatRadius(){
        double distanceToPlayer = calculateDistance(mob.getSpawnPoint(), new Position(player.getX(), player.getY()));
        return distanceToPlayer > mob.getCombatRadius();
    }
    double calculateDistance(Position p1, Position p2){
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)) ;
    }

    void engagePlayer(){
        if (nextToPlayer())
            attack();
        else
            chasePlayer();
    }

    boolean nextToPlayer(){
        return Math.abs(player.getX() - mob.getX()) < 2 && Math.abs(player.getY() - mob.getY()) < 2;
    }

    void attack(){
        mob.attack(player);
    }

    void chasePlayer(){
        int xStep = mob.getX();
        int yStep = mob.getY();
        for(int i = 0; i < mob.getMovementSpeed(); i++){
            if(Math.abs(xStep - player.getX()) > 1)
                xStep+= Integer.compare(player.getX(), xStep);
            if(Math.abs(yStep - player.getY()) > 1)
                yStep+= Integer.compare(player.getY(), yStep);
        }
        mob.move(new Position(xStep, yStep));
    }

}
