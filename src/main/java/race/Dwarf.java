package race;

import magic.*;

public class Dwarf extends AbstractRace{

    public Dwarf(){
        super(20, -1, 3, "Dwarf");
    }

    @Override
    public int getSpellModifier(Magic spell){
        if (spell instanceof FireSpell){return 0;}
        else  if (spell instanceof IceSpell){return 10;}
        else if (spell instanceof ElectricSpell){return -5;}
        else if (spell instanceof HealingSpell){return 5;}
        else if (spell instanceof PowerBoostSpell){return 0;}
        return 0;
    }

    @Override
    public boolean canCastSpell(Magic spell){
        return !(spell instanceof FireSpell);
    }
}
