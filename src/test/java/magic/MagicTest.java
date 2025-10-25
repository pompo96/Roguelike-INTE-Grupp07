package magic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import player.Player;
import race.Elf;
import race.Dwarf;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MagicTest {
    @ParameterizedTest
    @MethodSource("spellProvider")
    void spells_ShouldNotCastMoreThanItsSpecifiedAmount(Magic spell) {
        Player caster = mock(Player.class);
        Player target = mock(Player.class);
        int amountOfUses = spell.getNumberOfUses();
        for (int i = 0; i < amountOfUses; i++) {
            spell.castSpell(caster, target);
        }

        int damage = spell.castSpell(caster, target);

        assertEquals(0, damage, spell.getClass().getSimpleName() + " får inte användas fler gånger än tillåtet");
    }

    static List<Magic> spellProvider() {
        return List.of(new FireSpell(), new IceSpell());
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

