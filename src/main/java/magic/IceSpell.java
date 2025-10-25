package magic;

import player.Player;
import race.Elf;
import race.Dwarf;

public class IceSpell implements Magic {
    int damage = getBaseDamage();
    private int numberOfUses = 2;
    @Override
    public String getMagicType() {
        return "ice";
    }
    @Override
    public int getNumberOfUses(){
        return numberOfUses;
    }
    @Override
    public int castSpell(Player caster, Player target) {
        int modifiedDamage = damage;

        if(getNumberOfUses() == 0){
            return 0;
        }
        if (checkIfAbleToCast()) {
            //cast spell
        }
        if (target.getRace() instanceof Dwarf) {
            modifiedDamage = damage + 10;
            numberOfUses--;

        }
        if (target.getRace() instanceof Elf) {
            modifiedDamage = damage - 5;
            numberOfUses--;
        }
        return modifiedDamage;
    }

    @Override
    public boolean checkIfAbleToCast() {
        return true;
    }

    @Override
    public int checkEnvironmentBoost() {
        return 1;
    }
}
