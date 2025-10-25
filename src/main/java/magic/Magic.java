package magic;

import player.Player;

public interface Magic {
    default int getBaseDamage(){return 10;}
    String getMagicType();
    int getNumberOfUses();
    int castSpell(Player caster, Player target);
    boolean checkIfAbleToCast();
    int checkEnvironmentBoost();
    default String toStringDescription() {
        return getMagicType() + "Spell [damage=" + getBaseDamage() + ", usesLeft=" + getNumberOfUses() + "]";
    }
}
