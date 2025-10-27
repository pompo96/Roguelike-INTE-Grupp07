package magic;

import race.Dwarf;
import race.Elf;
import player.Player;

public class BuffedHealingSpell extends SpellDecorator{
    public BuffedHealingSpell(Magic decoratedSpell){
        super(decoratedSpell);
    }
    @Override
    public int castSpell(Player caster, Player target){
        int healingPower = decoratedSpell.castSpell(caster, target);
        target.updateMaxLife(healingPower);
        return healingPower;
    }
}
