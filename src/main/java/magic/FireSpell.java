package magic;

import player.Player;
import race.Dwarf;
import race.Elf;

public class FireSpell implements Magic {
    private int damage = getBaseDamage();

    public String getMagicType() {
        return "fire";
    }

    public int castSpell(Player caster, Player target) {
        if (checkIfAbleToCast()) {
            //cast spell
        }
        if (target.getRace() instanceof Dwarf) {
            damage -= 5;
        }
        if (target.getRace() instanceof Elf) {
            damage += 10;
        }
        return damage;
    }
    public boolean checkIfAbleToCast() {
        // if(Om race tillåter det, mana cost, environment etc)
        return true;
    }

    public int checkEnvironmentBoost() {
        //Om du är i gräs område -> damage boost
        //Om du är i vatten område -> damage decrease
        return 1;
    }
}

