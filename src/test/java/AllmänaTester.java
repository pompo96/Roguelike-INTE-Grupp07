import org.junit.jupiter.api.Test;
import utrustning.Utrustning;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllmänaTester {

    @Test
    public void testUtrustning() {
        Utrustning ut = new Utrustning();
        assertEquals(true, ut.getValue());
    }

    @Test
    public void weaponHadIDNameAndStats(){
        Weapon sword = new Weapon(1, "Flamesword", 14);
        assertEquals(1, sword.getID());
        assertEquals("Flamesword", sword.getName());
        assertEquals(14, sword.getDamage());
    }

}
