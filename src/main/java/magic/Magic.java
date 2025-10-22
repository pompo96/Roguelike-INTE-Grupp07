package magic;

import player.Player;

public interface Magic {
    default int getBaseDamage(){return 10;}
    String getMagicType();
    int damageCalculation(Player caster, Player target);
    void castSpell(Player caster, Player target);
    boolean checkIfAbleToCast(MagicInformation info);
    int checkEnvironmentBoost(MagicInformation info);
}
