package race;

public class Elf implements Race{
    @Override
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
