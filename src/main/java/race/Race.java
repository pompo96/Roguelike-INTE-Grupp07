package race;

import magic.Magic;
//import utrustning.Item;
//import java.util.List;

public interface Race {
    int getLifeModifier();
    int getMovementModifier();
    int getAttackPowerModifier();
    String getName();

    int getSpellModifier(Magic spell);
    boolean canCastSpell(Magic spell);

    //metoder för hur equipments används av olika raser
    //List<Item> startingItems();
    //boolean canUseEquipment(Item item);

}
