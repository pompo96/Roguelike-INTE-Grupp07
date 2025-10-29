package race;

import magic.*;

public abstract class AbstractRace implements Race{

    private final int lifeModifier;
    private final int movementModifier;
    private final int attackPowerModifier;
    private final String name;

    public AbstractRace(int lifeModifier, int movementModifier, int attackPowerModifier, String name){
        this.lifeModifier = lifeModifier;
        this.movementModifier = movementModifier;
        this.attackPowerModifier = attackPowerModifier;
        this.name = name;
    }

    @Override
    public int getLifeModifier(){
        return lifeModifier;
    }

    @Override
    public int getMovementModifier(){
        return movementModifier;
    }

    @Override
    public int getAttackPowerModifier(){
        return attackPowerModifier;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public abstract int getSpellModifier(Magic spell);

    @Override
    public abstract boolean canCastSpell(Magic spell);

}
