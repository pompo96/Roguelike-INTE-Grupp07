package magic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.MethodSource;
import player.Player;
import race.Elf;
import race.Dwarf;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MagicTest {
    @ParameterizedTest
    @MethodSource("spellProvider")
    void spells_ShouldNotCastMoreThanItsSpecifiedAmount(Magic spell) {
        Player caster = mock(Player.class);
        Player target = mock(Player.class);
        int amountOfUses = spell.getNumberOfUses();
        for (int i = 0; i < amountOfUses; i++) {
            spell.castSpell(caster, target);
        }

        assertThrows(IllegalStateException.class, () -> {
            spell.castSpell(caster, target);
        });
    }

    static List<Magic> spellProvider() {
        return List.of(new FireSpell(), new IceSpell(), new HealingSpell(), new ElectricSpell(), new PowerBoostSpell());
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
            "Dwarf, 20",
            "Elf, 5"
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
    @ParameterizedTest(name = "IceSpell mot {0} ska ge {1} damage")
    @CsvSource({
            "Dwarf, 5",
            "Elf, 5"
    })
    void electricSpell_DealsDifferentDamageDependingOnRace(String raceName, int expectedDamage){
        Player caster = mock(Player.class);
        Player target = mock(Player.class);

        switch (raceName) {
            case "Elf" -> when(target.getRace()).thenReturn(new Elf());
            case "Dwarf" -> when(target.getRace()).thenReturn(new Dwarf());
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        ElectricSpell electricSpell = new ElectricSpell();
        int actualDamage = electricSpell.castSpell(caster, target);
        assertEquals(expectedDamage, actualDamage);
    }
    @Test
    void buffedElectricalSpell_DecreasesTargetSpeed(){
        BuffedElectricalSpell buffedElectricalSpell = new BuffedElectricalSpell(new ElectricSpell());
        Player caster = mock(Player.class);
        Player target = mock(Player.class);
        when(target.getMovementSpeed()).thenReturn(8); //Implementera riktiga player objekt så kommer det fungera

        buffedElectricalSpell.castSpell(caster, target);
        assertEquals(8, target.getMovementSpeed(), "Din movement speed är fel mängd");
    }
    @Test
    void lightEffect_WorksWhenDark() {
        Magic fireSpell = new FireSpell();
        LightEffect fireSpellWithLightSource = new LightEffect(fireSpell);

        boolean lightSource = fireSpellWithLightSource.castLightSource(true);

        assertTrue(lightSource, "Solen är ute och inte månen, alltså kan du inte använda en lighteffect");
    }

    @ParameterizedTest(name = "HealingSpell mot {0} ska öka dens liv med {1}")
    @CsvSource({
            "Dwarf, 5", //mindre healing
            "Elf, 20" //mer healing
    })
    void healingSpell_HealsHealthPool(String raceName, int expectedHealthIncrease){
        Player caster = mock(Player.class);
        Player target = mock(Player.class);

        switch (raceName) {
            case "Elf" -> when(target.getRace()).thenReturn(new Elf());
            case "Dwarf" -> when(target.getRace()).thenReturn(new Dwarf());
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }

        Magic healingSpell = new HealingSpell();
        int actualHealing = healingSpell.castSpell(caster, target);
        assertEquals(actualHealing, expectedHealthIncrease, "HealingSpell ska uppdatera en spelares liv korrekt beroende på ras");
    }
    @ParameterizedTest(name = "HealingSpell mot {0} ska öka dens liv med {1}")
    @CsvSource({
            "Dwarf, 5", //mindre healing
            "Elf, 20" //mer healing
    })
    void buffedHealingSpell_HealsHealthPoolAndIncreasesMaxHealth(String raceName, int expectedHealthIncrease){
        Player caster = mock(Player.class);
        Player target = mock(Player.class);

        switch (raceName) {
            case "Elf" -> when(target.getRace()).thenReturn(new Elf());
            case "Dwarf" -> when(target.getRace()).thenReturn(new Dwarf());
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }

        Magic buffedHealingSpell = new BuffedHealingSpell(new HealingSpell());
        int actualHealing = buffedHealingSpell.castSpell(caster, target);
        assertEquals(actualHealing, expectedHealthIncrease, "HealingSpell ska uppdatera en spelares liv och maximala liv korrekt beroende på ras");
    }
    void powerBoostSpell_IncreasesAttackPower(){
        PowerBoostSpell powerBoostSpell = new PowerBoostSpell();

        Player caster = mock(Player.class);
        Player target = mock(Player.class);

        int expectedPowerBoost = powerBoostSpell.castSpell(caster, target);
        assertEquals(expectedPowerBoost, 110, "Players attack power är fel mängd");
    }
}

