package magic;
import player.Player;

public class BuffedElectricalSpell extends SpellDecorator{
    private int numberOfUses = 5;
    public BuffedElectricalSpell(Magic decoratedSpell){
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

        int damageTaken = calculator.getCalculatedDamage();
        target.updateCurrentLife(-damageTaken);

        int decreaseSpeedAmount = 2;
        target.updateMovementSpeed(-decreaseSpeedAmount);
    }
}
