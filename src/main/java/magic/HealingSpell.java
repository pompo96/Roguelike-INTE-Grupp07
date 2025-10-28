package magic;

import player.Player;

public class HealingSpell implements Magic{
    private int numberOfUses = 3;
    @Override
    public String getMagicType() {
        return "HealingSpell";
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

        int healthIncreased = calculator.getCalculatedDamage();
        target.updateCurrentLife(healthIncreased);
        return healthIncreased;
    }
    @Override
    public void accept(SpellVisitor visitor, Player caster, Player target){
        visitor.visit(this, caster, target);
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
