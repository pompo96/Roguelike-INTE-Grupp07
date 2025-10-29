package magic;

import player.Player;

public interface Magic {
    default int getBaseModifier(){return 10;}
    String getMagicType();
    int getNumberOfUses();
    void castSpell(Player caster, Player target);
    void accept(SpellVisitor visitor, Player caster, Player target);
    boolean checkIfAbleToCast();
    int checkEnvironmentBoost();
    default String toStringDescription() {
        return getMagicType() + "Spell [damage=" + getBaseModifier() + ", usesLeft=" + getNumberOfUses() + "]";
    }
}
