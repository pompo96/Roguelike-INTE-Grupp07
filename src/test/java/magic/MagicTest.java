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

import static org.junit.jupiter.api.Assertions.*;


class MagicTest {


    @ParameterizedTest
    @MethodSource("spellProvider")
    void spells_ShouldNotCastMoreThanItsSpecifiedAmount(Magic spell) {
        Player caster = new Player(new Human(), new HashMap<String, Item>(), '@', "name");
        Player target = new Player(new Human(), new HashMap<String, Item>(), '@', "name");
        int amountOfUses = spell.getNumberOfUses();
        for (int i = 0; i < amountOfUses; i++) {
            spell.castSpell(caster, target);
        }

        assertThrows(IllegalStateException.class, () -> {
            spell.castSpell(caster, target);
        });
    }

    @ParameterizedTest(name = "{0} ska ha korrekt magicType")
    @MethodSource("spellProvider")
    void allSpells_ShouldHaveCorrectMagicType(Magic spell) {
        String expectedType = switch (spell.getClass().getSimpleName()) {
            case "FireSpell", "BuffedFireSpell" -> "FireSpell";
            case "IceSpell" -> "IceSpell";
            case "ElectricSpell", "BuffedElectricalSpell" -> "ElectricSpell";
            case "HealingSpell", "BuffedHealingSpell" -> "HealingSpell";
            case "PowerBoostSpell" -> "PowerBoostSpell";
            default -> throw new IllegalArgumentException("Saknar förväntad magicType för " + spell.getClass().getSimpleName());
        };

        assertEquals(expectedType, spell.getMagicType(),
                () -> spell.getClass().getSimpleName() + " returnerade fel magicType");
    }

    static List<Magic> spellProvider() {
        return List.of(new FireSpell(), new IceSpell(), new HealingSpell(), new ElectricSpell(), new PowerBoostSpell());
    }

    @ParameterizedTest(name = "FireSpell mot {0} ska ge {1} damage")
    @CsvSource({
            "Elf, 20",     // extra damage
            "Dwarf, 5", // mindre damage
            "Human, 10"
    })
    void fireSpell_DealsDifferentDamageDependingOnRace(String raceName, int expectedDamage) {
        Player caster = new Player(new Human(), new HashMap<String, Item>(), '@', "name");

        Race race;
        switch (raceName) {
            case "Elf" -> race = new Elf();
            case "Dwarf" -> race = new Dwarf();
            case "Human" -> race = new Human();
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        Player target = new Player(race, new HashMap<String, Item>(), '@', "name");
        int healthBeforeSpellCast = target.getCurrentLife();
        FireSpell fireSpell = new FireSpell();
        fireSpell.castSpell(caster, target);

        assertEquals(healthBeforeSpellCast - expectedDamage, target.getCurrentLife(), "FireSpell ska göra korrekt damage beroende på ras");
    }

    @ParameterizedTest(name = "IceSpell mot {0} ska ge {1} damage")
    @CsvSource({
            "Dwarf, 20",
            "Elf, 5",
            "Human, 10"
    })
    void iceSpell_DealsDifferentDamageDependingOnRace(String raceName, int expectedDamage) {
        Player caster = new Player(new Human(), new HashMap<String, Item>(), '@', "name");

        Race race;
        switch (raceName) {
            case "Elf" -> race = new Elf();
            case "Dwarf" -> race = new Dwarf();
            case "Human" -> race = new Human();
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        Player target = new Player(race, new HashMap<String, Item>(), '@', "name");
        int healthBeforeSpellCast = target.getCurrentLife();
        IceSpell iceSpell = new IceSpell();
        iceSpell.castSpell(caster, target);

        assertEquals(healthBeforeSpellCast - expectedDamage, target.getCurrentLife(), "IceSpell ska göra korrekt damage beroende på ras");
    }

    @ParameterizedTest(name = "ElectricalSpell mot {0} ska ge {1} damage")
    @CsvSource({
            "Dwarf, 5",
            "Elf, 5",
            "Human, 10"
    })
    void electricSpell_DealsDifferentDamageDependingOnRace(String raceName, int expectedDamage) {
        Player caster = new Player(new Human(), new HashMap<String, Item>(), '@', "name");
        Race race;
        switch (raceName) {
            case "Elf" -> race = new Elf();
            case "Dwarf" -> race = new Dwarf();
            case "Human" -> race = new Human();
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        Player target = new Player(race, new HashMap<String, Item>(), '@', "name");
        int healthBeforeSpellCast = target.getCurrentLife();
        ElectricSpell electricSpell = new ElectricSpell();
        electricSpell.castSpell(caster, target);
        assertEquals(healthBeforeSpellCast - expectedDamage, target.getCurrentLife());
    }

    @Test
    void buffedElectricalSpell_DecreasesTargetSpeed() {
        BuffedElectricalSpell buffedElectricalSpell = new BuffedElectricalSpell(new ElectricSpell());
        Player caster = new Player(new Human(), new HashMap<String, Item>(), '@', "name");
        Player target = new Player(new Human(), new HashMap<String, Item>(), '@', "name");

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
    @Test
    void lightEffect_DoesNotWorkWhenNotDark(){
        Magic fireSpell = new FireSpell();
        LightEffect fireSpellWithLightSource = new LightEffect(fireSpell);

        boolean lightSource = fireSpellWithLightSource.castLightSource(false);
        assertFalse(lightSource);
    }
    @ParameterizedTest(name = "HealingSpell mot {0} ska öka dens liv med {1}")
    @CsvSource({
            "Dwarf, 5", //mindre healing
            "Elf, 20", //mer healing
            "Human, 10"
    })
    void healingSpell_HealsHealthPool(String raceName, int expectedHealthIncrease) {
        Player caster = new Player(new Human(), new HashMap<String, Item>(), '@', "name");
        Race race;
        switch (raceName) {
            case "Elf" -> race = new Elf();
            case "Dwarf" -> race = new Dwarf();
            case "Human" -> race = new Human();
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        Player target = new Player(race, new HashMap<String, Item>(), '@', "name");
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
            "Elf, 20", //mer healing
            "Human, 10"
    })
    void buffedHealingSpell_HealsHealthPoolAndIncreasesMaxHealth(String raceName, int expectedHealthIncrease) {
        Player caster = new Player(new Human(), new HashMap<String, Item>(), '@', "name");
        Race race;
        switch (raceName) {
            case "Elf" -> race = new Elf();
            case "Dwarf" -> race = new Dwarf();
            case "Human" -> race = new Human();
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        Player target = new Player(race, new HashMap<String, Item>(), '@', "name");
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

        Player caster = new Player(new Human(), new HashMap<String, Item>(), '@', "name");
        Player target = new Player(new Human(), new HashMap<String, Item>(), '@', "name");

        powerBoostSpell.castSpell(caster, target);
        int expectedPowerBoost = target.getAttackPowerEffectModifier();
        assertEquals(20, expectedPowerBoost, "Players attack power är fel mängd");
    }
    @ParameterizedTest(name = "PowerBoost mot {0} ska öka dens liv med {1}")
    @CsvSource({
            "Dwarf, 20",
            "Elf, 5",
            "Human, 10"
    })
    void powerBoostSpell_IncreasesAttackPowerDependingOnRace(String raceName, int expectedPowerBoost){
        Player caster = new Player(new Human(), new HashMap<String, Item>(), '@', "name");
        Race race;
        switch (raceName) {
            case "Elf" -> race = new Elf();
            case "Dwarf" -> race = new Dwarf();
            case "Human" -> race = new Human();
            default -> throw new IllegalArgumentException("Okänd ras: " + raceName);
        }
        Player target = new Player(race, new HashMap<String, Item>(), '@', "name");
        PowerBoostSpell powerBoostSpell = new PowerBoostSpell();
        powerBoostSpell.castSpell(caster, target);
        int actualPowerBoost = target.getAttackPowerEffectModifier();
        assertEquals(target.getBaseAttackPower() + expectedPowerBoost, actualPowerBoost + race.getAttackPowerModifier(), "Din PowerBoost är för fel");
    }
}

