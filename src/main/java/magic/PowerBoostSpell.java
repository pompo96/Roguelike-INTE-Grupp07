package magic;

import player.Player;

public class PowerBoostSpell implements Magic {
    private int numberOfUses = 1;
    public String getMagicType(){
        return "PowerBoostSpell";
    }
    public int getNumberOfUses(){
        return numberOfUses;
    }
    public void castSpell(Player caster, Player target){
        if (getNumberOfUses() == 0) {
            throw new IllegalStateException("Spell cannot be cast anymore!");
        }
        DamageCalculator calculator = new DamageCalculator();
        accept(calculator, caster, target);
        numberOfUses--;

        int attackPowerBoost = calculator.getCalculatedDamage();
        target.updateAttackPowerEffectModifier(attackPowerBoost);
    }
    public void accept(SpellVisitor visitor, Player caster, Player target){
        visitor.visit(this, caster, target);
    }
    public boolean checkIfAbleToCast(){
        return true;
    }
    public int checkEnvironmentBoost(){
        return 1;
    }
}
