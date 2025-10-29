package magic;

import player.Player;

public class BuffedHealingSpell extends SpellDecorator{
    private int numberOfUses = 5;
    public BuffedHealingSpell(Magic decoratedSpell){
        super(decoratedSpell);
    }
    @Override
    public void castSpell(Player caster, Player target){
        if (getNumberOfUses() == 0) {
            throw new IllegalStateException("Spell cannot be cast anymore!");
        }
        DamageCalculator calculator = new DamageCalculator();
        accept(calculator, caster, target);
        numberOfUses--;

        int healingAmount = calculator.getCalculatedDamage();
        target.updateCurrentLife(healingAmount);
        target.updateMaxLife(healingAmount);
    }
}
