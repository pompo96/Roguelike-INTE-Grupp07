package magic;

import player.Player;
import race.Elf;
import race.Dwarf;

public class IceSpell implements Magic {
    private int numberOfUses = 2;

    @Override
    public String getMagicType() {
        return "IceSpell";
    }

    @Override
    public int getNumberOfUses() {
        return numberOfUses;
    }

    @Override
    public int castSpell(Player caster, Player target) {
        if (getNumberOfUses() == 0) {
            throw new IllegalStateException("Spell cannot be cast anymore!");
        }
        DamageCalculator calculator = new DamageCalculator();
        accept(calculator, caster, target);
        numberOfUses--;

        int damageTaken = calculator.getCalculatedDamage();
        target.updateCurrentLife(-damageTaken);
        return damageTaken;
    }

    @Override
    public boolean checkIfAbleToCast() {
        return true;
    }

    @Override
    public int checkEnvironmentBoost() {
        return 1;
    }
    @Override
    public void accept(SpellVisitor visitor, Player caster, Player target){
        visitor.visit(this, caster, target);
    }
    @Override
    public String toString() {
        return toStringDescription();
    }
}
