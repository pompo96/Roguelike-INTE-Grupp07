package race;

import magic.Magic;

public interface Race {
    int getLifeModifier();
    int getMovementModifier();
    int getAttackPowerModifier();
    String getName();

    int getSpellModifier(Magic spell);
    boolean canCastSpell(Magic spell);

}
