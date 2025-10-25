package race;

public class Human implements Race{
    public String getName(){
        return "Mr x";
    }
    @Override
    public int getLifeModifier() {
        return 0;
    }

    @Override
    public int getMovementModifier() {
        return 0;
    }
    @Override
    public int getAttackPowerModifier(){
        return 0;
    }
}
