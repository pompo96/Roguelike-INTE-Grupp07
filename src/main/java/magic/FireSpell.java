package magic;
import player.Player;
import race.Dwarf;
import race.Elf;

public class FireSpell implements Magic {
    private int damage = getBaseDamage();
    private int numberOfUses = 3;

    @Override
    public String getMagicType() {
        return "FireSpell";
    }

    @Override
    public int getNumberOfUses() {
        return numberOfUses;
    }

    @Override
    public int castSpell(Player caster, Player target) {
        int modifiedDamage = damage;

        if (getNumberOfUses() == 0) {
            return 0;
        }
        if (checkIfAbleToCast()) {
            //cast spell
        }
        if (target.getRace() instanceof Dwarf) {
            modifiedDamage = damage - 5;

        }
        if (target.getRace() instanceof Elf) {
            modifiedDamage = damage + 10;
        }
        numberOfUses--;
        return modifiedDamage;
    }

    @Override
    public boolean checkIfAbleToCast() {
        // if(Om race tillåter det, mana cost, environment etc)
        return true;
    }

    @Override
    public int checkEnvironmentBoost() {
        //Om du är i gräs område -> damage boost
        //Om du är i vatten område -> damage decrease
        return 1;
    }
    @Override
    public String toString() {
        return toStringDescription();
    }
}

