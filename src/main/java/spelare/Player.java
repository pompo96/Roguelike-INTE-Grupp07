package spelare;

public class Player {
    int maxLife;
    int currentLife;
    int movementSpeed;
//    Race race;

    
    public Player() {
        int defaultCurrentLife = 100;
        int defaultMaxLife = 100;
        int defaultMovementSpeed = 100;
        this.maxLife = defaultMaxLife;
        this.currentLife = defaultCurrentLife;
//        this.race = defaultRace;
        this.movementSpeed = defaultMovementSpeed;
    }

//    public Player(Race race) {
//        this.race = race;
//
//    }

    public int getMaxLife(){
        return this.maxLife;
    }
    public int getCurrentLife(){
        return this.currentLife;
    }

    public void updateMaxLife(int maxLifeAdjustment){
        this.maxLife += maxLifeAdjustment;
    }

    public void updateCurrentLife(int currentLifeAdjustment){
        int finalCurrentLife = currentLife + currentLifeAdjustment;
        if(finalCurrentLife > this.maxLife){
            finalCurrentLife = this.maxLife;
        }
        this.currentLife = finalCurrentLife;
    }




}
