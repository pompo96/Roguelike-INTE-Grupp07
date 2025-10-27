package ai;

import player.Player;

import java.util.Random;

public class Ai {
    private PlaceholderMob mob;
    private Player player;
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
                if(idleFrames>2){
                    beginPatrolling();
                } else {
                    idleFrames++;
                } break;

            case PATROLLING:
                if(mob.getX() == destination.x && mob.getY() == destination.y){
                    state = MobState.IDLE;
                } else {
                    patroll();
                }


        }
    }

    private void beginPatrolling(){
        state = MobState.PATROLLING;
        destination = generatePatrollDestination();
        idleFrames = 0;
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

    }

}
