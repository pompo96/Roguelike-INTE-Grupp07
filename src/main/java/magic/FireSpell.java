package magic;
import player.Player;

public class FireSpell implements Magic {
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
    public void castSpell(Player caster, Player target) {
        if (getNumberOfUses() == 0) {
            throw new IllegalStateException("Spell cannot be cast anymore!");
        }
        DamageCalculator calculator = new DamageCalculator();
        accept(calculator, caster, target);
        numberOfUses--;

        int damageTaken = calculator.getCalculatedModifier();
        target.updateCurrentLife(-damageTaken);
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

