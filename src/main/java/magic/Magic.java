package magic;

import player.Player;

public interface Magic {
    default int getBaseDamage(){return 10;}
    String getMagicType();
    int castSpell(Player caster, Player target);
    boolean checkIfAbleToCast();
    int checkEnvironmentBoost();
}
