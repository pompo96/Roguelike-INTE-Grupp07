package magic;

import player.Player;
import race.Dwarf;
import race.Elf;

public class DamageCalculator implements SpellVisitor {
    int calculatedDamage;

    public void visit(IceSpell iceSpell, Player caster, Player target) {
        int damage = iceSpell.getBaseModifier();
        if (target.getRace() instanceof Dwarf) {
            calculatedDamage = damage + 10;

        }
        if (target.getRace() instanceof Elf) {
            calculatedDamage = damage - 5;
        }
    }

    public void visit(FireSpell fireSpell, Player caster, Player target) {
        int damage = fireSpell.getBaseModifier();
        if (target.getRace() instanceof Dwarf) {
            calculatedDamage = damage - 5;
        }
        if (target.getRace() instanceof Elf) {
            calculatedDamage = damage + 10;
        }
    }
    public void visit(ElectricSpell electricSpell, Player caster, Player target) {
        int damage = electricSpell.getBaseModifier();
        if (target.getRace() instanceof Dwarf) {
            calculatedDamage = damage - 5;
        }
        if (target.getRace() instanceof Elf) {
            calculatedDamage = damage - 5;
        }
    }
    public void visit(HealingSpell healingSpell, Player caster, Player target){
        int healingPower = healingSpell.getBaseModifier();
        if(target.getRace() instanceof Dwarf){
            healingPower -= 5;
        }
        if(target.getRace() instanceof Elf){
            healingPower += 10;
        }
        calculatedDamage = healingPower;
    }
    public void visit(PowerBoostSpell powerBoostSpell, Player caster, Player target){
        int attackPower = powerBoostSpell.getBaseModifier();
        calculatedDamage = target.getBaseAttackPower() + attackPower;
    }

    public int getCalculatedDamage() {
        return calculatedDamage;
    }
}
