package race;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RaceTests {

    @Test
    public void elfHasCorrectModifiers(){
        Race elf = new Elf();
        assertEquals(10, elf.getLifeModifier());
        assertEquals(2, elf.getMovementModifier());
        assertEquals(2, elf.getAttackPowerModifier());
        assertEquals("Elf", elf.getName());
    }

    @Test
    public void humanHasCorrectModifiers(){
        Race human = new Human();
        assertEquals(0, human.getLifeModifier());
        assertEquals(0, human.getMovementModifier());
        assertEquals(0, human.getAttackPowerModifier());
        assertEquals("Human", human.getName());
    }
}
