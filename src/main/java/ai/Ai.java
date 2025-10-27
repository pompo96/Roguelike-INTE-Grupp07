package ai;

import player.Player;

import java.util.Random;

public class Ai {
    private final PlaceholderMob mob;
    private final Player player;
    private MobState state;
    private int idleFrames;
    private Position destination;
    private static final int SPAWN_RADIUS = 20;

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
        switch (state) {
            case IDLE:
                if(idleFrames>1){
                    beginPatrolling();
                } else {
                    idleFrames++;
                } break;

            case PATROLLING:
                patroll();


        }
    }

    private void beginPatrolling(){
        state = MobState.PATROLLING;
        setDestination(generatePatrollDestination());
    }

    public void setDestination(Position destination) {
        this.destination = destination;
    }

    private Position generatePatrollDestination(){
        Position candidate;
        do {
            Random rand = new Random();
            double angle = rand.nextDouble() * 2 * Math.PI;
            double distance = rand.nextDouble() * SPAWN_RADIUS;
            int dx = (int) Math.round(Math.cos(angle) * distance);
            int dy = (int) Math.round(Math.sin(angle) * distance);
            candidate = new Position(mob.getX() + dx, mob.getY() + dy);
        }
        while(candidate.equals(mob.getCurrentPosition()) || candidate.x < 2 || candidate.y < 2);
            return candidate;
    }

    private void patroll(){
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

    private void beginIdle(){
        state = MobState.IDLE;
        idleFrames = 0;
    }
}
