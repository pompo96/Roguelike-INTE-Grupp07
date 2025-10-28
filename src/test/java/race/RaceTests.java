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
    }

    @Test
    public void elfHasCorrectName(){
        Race elf = new Elf();
        assertEquals("Elf", elf.getName());
    }

    @Test
    public void humanHasCorrectModifiers(){
        Race human = new Human();
        assertEquals(0, human.getLifeModifier());
        assertEquals(0, human.getMovementModifier());
        assertEquals(0, human.getAttackPowerModifier());
    }

    @Test
    public void humanHasCorrectName() {
        Race human = new Human();
        assertEquals("Human", human.getName());
    }

    //test som kollar att rätt ras har/startar med rätt koordinater ex "elfStartsIn..."

    //test som kollar att rätt ras har/startar med rätt equipment

    //test som kollar att rätt ras har rätt krafter

    //test som kollar att rätt ras har rätt quests

}
