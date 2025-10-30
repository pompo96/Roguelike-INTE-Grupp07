package race;

import magic.*;

public class Human extends AbstractRace{

    public Human(){
        super(0,0,0,"Human");
    }

    @Override
    public int getSpellModifier(Magic spell){
        if (spell instanceof FireSpell){return 0;}
        if (spell instanceof IceSpell){return 0;}
        if (spell instanceof ElectricSpell){return 0;}
        if (spell instanceof HealingSpell){return 5;}
        if (spell instanceof PowerBoostSpell){return 0;}
        return 0;
    }

    @Override
    public boolean canCastSpell(Magic spell){
        return true;
    }

    //metod för vilken equipment human får/börjar med
}
