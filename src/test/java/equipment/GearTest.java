package equipment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GearTest {

    @Test
    public void testUtrustning() {
        Gear ut = new Gear();
        assertEquals(true, ut.getValue());
    }

    @Test
    public void weaponHadIDNameStatsAndType(){
        Gear sword = new Gear(1, "Flamesword", 14);
        assertEquals(true, sword.getValue());
        assertEquals(1, sword.getID());
        assertEquals("Flamesword", sword.getName());
        assertEquals(14, sword.getDamage());
        assertEquals("weapon", sword.getType());
    }

}
