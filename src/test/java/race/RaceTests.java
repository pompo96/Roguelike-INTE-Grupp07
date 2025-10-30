package race;

import magic.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RaceTests {

    @Test
    public void dwarfHasCorrectModifiers(){
        Race dwarf = new Dwarf();
        assertEquals(20, dwarf.getLifeModifier());
        assertEquals(-1, dwarf.getMovementModifier());
        assertEquals(3, dwarf.getAttackPowerModifier());
    }

    @Test
    public void dwarfHasCorrectName(){
        Race dwarf = new Dwarf();
        assertEquals("Dwarf", dwarf.getName());
    }

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

    @Test
    public void dwarfHasCorrectSpellModifiers(){
        Race dwarf = new Dwarf();
        assertEquals(0, dwarf.getSpellModifier(new FireSpell()));
        assertEquals(10, dwarf.getSpellModifier(new IceSpell()));
        assertEquals(-5, dwarf.getSpellModifier(new ElectricSpell()));
        assertEquals(5, dwarf.getSpellModifier(new HealingSpell()));
        assertEquals(0, dwarf.getSpellModifier(new PowerBoostSpell()));
    }

    @Test
    public void elfHasCorrectSpellModifiers(){
        Race elf = new Elf();
        assertEquals(10, elf.getSpellModifier(new FireSpell()));
        assertEquals(0, elf.getSpellModifier(new IceSpell()));
        assertEquals(-5, elf.getSpellModifier(new ElectricSpell()));
        assertEquals(10, elf.getSpellModifier(new HealingSpell()));
        assertEquals(0, elf.getSpellModifier(new PowerBoostSpell()));
    }

    @Test
    public void humanHasCorrectSpellModifiers(){
        Race human = new Human();
        assertEquals(0, human.getSpellModifier(new FireSpell()));
        assertEquals(0, human.getSpellModifier(new IceSpell()));
        assertEquals(0, human.getSpellModifier(new ElectricSpell()));
        assertEquals(5, human.getSpellModifier(new HealingSpell()));
        assertEquals(0, human.getSpellModifier(new PowerBoostSpell()));
    }

    @Test
    public void elfCannotCastIceSpell(){
        Race elf = new Elf();
        Magic iceSpell = new IceSpell();
        assertFalse(elf.canCastSpell(iceSpell));
    }

    @Test
    public void dwarfCannotCastFireSpell(){
        Race dwarf = new Dwarf();
        Magic fireSpell = new FireSpell();
        assertFalse(dwarf.canCastSpell(fireSpell));
    }

    @Test
    public void elfCanCastFireSpell(){
        Race elf = new Elf();
        Magic iceSpell = new IceSpell();
        assertTrue(elf.canCastSpell(iceSpell));
    }

    @Test
    public void dwarfCanCastIceSpell(){
        Race dwarf = new Dwarf();
        Magic fireSpell = new FireSpell();
        assertTrue(dwarf.canCastSpell(fireSpell));
    }

    @Test
    public void humanCanCastIceSpell(){
        Race human = new Human();
        Magic iceSpell = new IceSpell();
        assertTrue(human.canCastSpell(iceSpell));
    }

    //test som kollar att rätt ras har/startar med rätt koordinater ex "elfStartsIn..."

    //test som kollar att rätt ras har/startar med rätt equipment

    //test som kollar att rätt ras har rätt krafter

    //test som kollar att rätt ras har rätt quests

}
