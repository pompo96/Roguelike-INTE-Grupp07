package magic;

import player.Player;

public interface SpellVisitor {
    void visit(IceSpell iceSpell, Player caster, Player target);
    void visit(FireSpell fireSpell, Player caster, Player target);
    int getCalculatedDamage();
}
