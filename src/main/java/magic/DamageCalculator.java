package magic;

import player.Player;
import race.Dwarf;
import race.Elf;

public class DamageCalculator implements SpellVisitor {
    int calculatedDamage;

    public void visit(IceSpell iceSpell, Player caster, Player target) {
        int damage = iceSpell.getBaseDamage();
        if (target.getRace() instanceof Dwarf) {
            calculatedDamage = damage + 10;

        }
        if (target.getRace() instanceof Elf) {
            calculatedDamage = damage - 5;
        }
    }

    public void visit(FireSpell fireSpell, Player caster, Player target) {
        int damage = fireSpell.getBaseDamage();
        if (target.getRace() instanceof Dwarf) {
            calculatedDamage = damage - 5;
        }
        if (target.getRace() instanceof Elf) {
            calculatedDamage = damage + 10;
        }
    }

    public int getCalculatedDamage() {
        return calculatedDamage;
    }
}
