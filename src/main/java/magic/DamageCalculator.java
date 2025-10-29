package magic;

import player.Player;
import race.Dwarf;
import race.Elf;
import race.Human;

public class DamageCalculator implements SpellVisitor {
    int calculatedModifier;

    public void visit(IceSpell iceSpell, Player caster, Player target) {
        int damage = iceSpell.getBaseModifier();
        if (target.getRace() instanceof Dwarf) {
            calculatedModifier = damage + 10;
        }
        if (target.getRace() instanceof Elf) {
            calculatedModifier = damage - 5;
        }
        if(target.getRace() instanceof Human){
            calculatedModifier = damage;
        }
    }

    public void visit(FireSpell fireSpell, Player caster, Player target) {
        int damage = fireSpell.getBaseModifier();
        if (target.getRace() instanceof Dwarf) {
            calculatedModifier = damage - 5;
        }
        if (target.getRace() instanceof Elf) {
            calculatedModifier = damage + 10;
        }
        if(target.getRace() instanceof Human){
            calculatedModifier = damage;
        }
    }
    public void visit(ElectricSpell electricSpell, Player caster, Player target) {
        int damage = electricSpell.getBaseModifier();
        if (target.getRace() instanceof Dwarf) {
            calculatedModifier = damage - 5;
        }
        if (target.getRace() instanceof Elf) {
            calculatedModifier = damage - 5;
        }
        if(target.getRace() instanceof Human){
            calculatedModifier = damage;
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
        if(target.getRace() instanceof Human){
            calculatedModifier = healingPower;
        }
        calculatedModifier = healingPower;
    }
    public void visit(PowerBoostSpell powerBoostSpell, Player caster, Player target){
        int attackPower = powerBoostSpell.getBaseModifier();
        if(target.getRace() instanceof Dwarf){
            calculatedModifier = attackPower + 10;
        }
        if(target.getRace() instanceof Elf){
            calculatedModifier = attackPower - 5;
        }
        if(target.getRace() instanceof Human){
            calculatedModifier = attackPower;
        }
    }

    public int getCalculatedModifier() {
        return calculatedModifier;
    }
}
