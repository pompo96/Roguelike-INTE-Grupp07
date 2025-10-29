package magic;

import equipment.Item;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.MethodSource;
import player.Player;
import race.Elf;
import race.Dwarf;
import race.Human;
import race.Race;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Player caster = new Player(new Human(), new HashMap<String, Item>(), 0, 0, 'x', "Mr x");

        Race race;
        switch (raceName) {
            case "Elf" -> race = new Elf();
            case "Dwarf" -> race = new Dwarf();
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        Player target = new Player(race, new HashMap<String, Item>(), 0, 0, 'x', "Mr x");
        int healthBeforeSpellCast = target.getCurrentLife();
        FireSpell fireSpell = new FireSpell();
        fireSpell.castSpell(caster, target);

        assertEquals(healthBeforeSpellCast - expectedDamage, target.getCurrentLife(), "FireSpell ska göra korrekt damage beroende på ras");
    }

    @ParameterizedTest(name = "IceSpell mot {0} ska ge {1} damage")
    @CsvSource({
            "Dwarf, 20",
            "Elf, 5"
    })
    void iceSpell_DealsDifferentDamageDependingOnRace(String raceName, int expectedDamage) {
        Player caster = new Player(new Human(), new HashMap<String, Item>(), 0, 0, 'x', "Mr x");

        Race race;
        switch (raceName) {
            case "Elf" -> race = new Elf();
            case "Dwarf" -> race = new Dwarf();
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        Player target = new Player(race, new HashMap<String, Item>(), 0, 0, 'x', "Mr x");
        int healthBeforeSpellCast = target.getCurrentLife();
        IceSpell iceSpell = new IceSpell();
        iceSpell.castSpell(caster, target);

        assertEquals(healthBeforeSpellCast - expectedDamage, target.getCurrentLife(), "IceSpell ska göra korrekt damage beroende på ras");
    }

    @ParameterizedTest(name = "ElectricalSpell mot {0} ska ge {1} damage")
    @CsvSource({
            "Dwarf, 5",
            "Elf, 5"
    })
    void electricSpell_DealsDifferentDamageDependingOnRace(String raceName, int expectedDamage) {
        Player caster = new Player(new Human(), new HashMap<String, Item>(), 0, 0, 'x', "Mr x");
        Race race;
        switch (raceName) {
            case "Elf" -> race = new Elf();
            case "Dwarf" -> race = new Dwarf();
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        Player target = new Player(race, new HashMap<String, Item>(), 0, 0, 'x', "Mr x");
        int healthBeforeSpellCast = target.getCurrentLife();
        ElectricSpell electricSpell = new ElectricSpell();
        electricSpell.castSpell(caster, target);
        assertEquals(healthBeforeSpellCast - expectedDamage, target.getCurrentLife());
    }

    @Test
    void buffedElectricalSpell_DecreasesTargetSpeed() {
        BuffedElectricalSpell buffedElectricalSpell = new BuffedElectricalSpell(new ElectricSpell());
        Player caster = new Player(new Human(), new HashMap<String, Item>(), 0, 0, 'x', "Mr x");
        Player target = new Player(new Human(), new HashMap<String, Item>(), 0, 0, 'x', "Mr x");

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
    void healingSpell_HealsHealthPool(String raceName, int expectedHealthIncrease) {
        Player caster = new Player(new Human(), new HashMap<String, Item>(), 0, 0, 'x', "Mr x");
        Race race;
        switch (raceName) {
            case "Elf" -> race = new Elf();
            case "Dwarf" -> race = new Dwarf();
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        Player target = new Player(race, new HashMap<String, Item>(), 0, 0, 'x', "Mr x");
        int healthDecrease = -20;
        target.updateCurrentLife(healthDecrease);
        int currentTargetHealth = target.getCurrentLife();
        Magic healingSpell = new HealingSpell();
        healingSpell.castSpell(caster, target);
        assertEquals(currentTargetHealth + expectedHealthIncrease, target.getCurrentLife(), "HealingSpell ska uppdatera en spelares liv korrekt beroende på ras");
    }

    @ParameterizedTest(name = "HealingSpell mot {0} ska öka dens liv med {1}")
    @CsvSource({
            "Dwarf, 5", //mindre healing
            "Elf, 20" //mer healing
    })
    void buffedHealingSpell_HealsHealthPoolAndIncreasesMaxHealth(String raceName, int expectedHealthIncrease) {
        Player caster = new Player(new Human(), new HashMap<String, Item>(), 0, 0, 'x', "Mr x");
        Race race;
        switch (raceName) {
            case "Elf" -> race = new Elf();
            case "Dwarf" -> race = new Dwarf();
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        Player target = new Player(race, new HashMap<String, Item>(), 0, 0, 'x', "Mr x");
        int healthDecrease = -20;
        target.updateCurrentLife(healthDecrease);
        int currentTargetMaxHealth = target.getMaxLife();
        int currentTargetHealth = target.getCurrentLife();

        Magic buffedHealingSpell = new BuffedHealingSpell(new HealingSpell());
        buffedHealingSpell.castSpell(caster, target);
        assertEquals(currentTargetMaxHealth + expectedHealthIncrease, target.getMaxLife(), "Max hp kan inte öka mer");
        assertEquals(currentTargetHealth + expectedHealthIncrease, target.getCurrentLife(), "currentHealth kan inte öka mer");
    }

    @Test
    void defaultPowerBoostSpell_IncreasesAttackPower() {
        PowerBoostSpell powerBoostSpell = new PowerBoostSpell();

        Player caster = new Player(new Human(), new HashMap<String, Item>(), 0, 0, 'x', "Mr x");
        Player target = new Player(new Human(), new HashMap<String, Item>(), 0, 0, 'x', "Mr x");

        powerBoostSpell.castSpell(caster, target);
        int expectedPowerBoost = target.getAttackPowerEffectModifier();
        assertEquals(expectedPowerBoost, 20, "Players attack power är fel mängd");
    }
}

