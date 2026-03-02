package race;

import magic.*;

public class Elf extends AbstractRace{

    public Elf(){
        super(10, 2, 2, "Elf");
    }

    @Override
    public int getSpellModifier(Magic spell){
        if (spell instanceof FireSpell){return 10;}
        else if (spell instanceof IceSpell){return 0;}
        else if (spell instanceof ElectricSpell){return -5;}
        else if (spell instanceof HealingSpell){return 10;}
        else if (spell instanceof PowerBoostSpell){return 0;}
        return 0;
    }

    @Override
    public boolean canCastSpell(Magic spell){
        return !(spell instanceof IceSpell);
    }

    //metod för vilken equipment elf får/börjar med

    //metod för startområde?
    //ska olika races finnas inom olika områden i Map eller börja i ett visst område?
    //koordinater till område


}
