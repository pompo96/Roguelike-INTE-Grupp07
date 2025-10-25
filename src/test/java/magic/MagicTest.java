package magic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;
import player.Player;
import race.Elf;
import race.Dwarf;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MagicTest {
    @Test
    public void fireSpell_HasTypeFire() {
        FireSpell fireSpell = new FireSpell();
        assertEquals("fire", fireSpell.getMagicType());
    }

    @Test
    public void fireSpell_CanNotCastMoreThanItsAmount(){//Är inte färdig
        FireSpell fireSpell = new FireSpell();
        Player caster = mock(Player.class);
        Player target = mock(Player.class);

        fireSpell.castSpell(caster, target);
        fireSpell.castSpell(caster, target);
        fireSpell.castSpell(caster, target);
        int spellDamageAfterAmountOfUses = fireSpell.castSpell(caster, target);

        assertEquals(0, spellDamageAfterAmountOfUses, "En firespell får inte användas mer än 3 gånger");
    }

    @ParameterizedTest(name = "FireSpell mot {0} ska ge {1} damage")
    @CsvSource({
            "Elf, 20",     // extra damage
            "Dwarf, 5"     // mindre damage
    })
    void fireSpell_DealsDifferentDamageDependingOnRace(String raceName, int expectedDamage) {
        Player caster = mock(Player.class);
        Player target = mock(Player.class);

        // Returnera rätt typ av ras beroende på strängvärde
        switch (raceName) {
            case "Elf" -> when(target.getRace()).thenReturn(new Elf());
            case "Dwarf" -> when(target.getRace()).thenReturn(new Dwarf());
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }

        FireSpell fireSpell = new FireSpell();
        int actualDamage = fireSpell.castSpell(caster, target);

        assertEquals(expectedDamage, actualDamage,
                "FireSpell ska göra korrekt damage beroende på ras");
    }

    @ParameterizedTest(name = "IceSpell mot {0} ska ge {1} damage")
    @CsvSource({
            "Dwarf, 20",   // extra damage
            "Elf, 5"       // mindre damage
    })
    void iceSpell_DealsDifferentDamageDependingOnRace(String raceName, int expectedDamage) {
        Player caster = mock(Player.class);
        Player target = mock(Player.class);

        switch (raceName) {
            case "Elf" -> when(target.getRace()).thenReturn(new Elf());
            case "Dwarf" -> when(target.getRace()).thenReturn(new Dwarf());
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }

        IceSpell iceSpell = new IceSpell();
        int actualDamage = iceSpell.castSpell(caster, target);

        assertEquals(expectedDamage, actualDamage,
                "IceSpell ska göra korrekt damage beroende på ras");
    }

    @Test
    public void lightEffect_WorksWhenDark() {
        Magic fireSpell = new FireSpell();
        LightEffect fireSpellWithLightSource = new LightEffect(fireSpell);

        boolean lightSource = fireSpellWithLightSource.castLightSource(true);

        assertTrue(lightSource, "Solen är ute och inte månen, alltså kan du inte använda en lighteffect");
    }
}

