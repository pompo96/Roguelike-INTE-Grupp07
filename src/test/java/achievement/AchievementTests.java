package achievement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import magic.*;



public class AchievementTests {

    private AchievementManager manager;

    @BeforeEach
    public void setup(){
        manager = new AchievementManager();
    }

    @Test
    public void fireSpellAchievementObjectCreationTest(){
        Achievement fire = manager.getAchievement("first_fire_spell");

        assertNotNull(fire);
        assertEquals("first_fire_spell", fire.getId());
        assertEquals("Fire Caster", fire.getName());
        assertEquals("Use your first fire spell", fire.getDescription());
        assertEquals(100, fire.getPoints());

    }

    @Test
    public void healingSpellAchievementObjectCreationTest(){
        Achievement healing = manager.getAchievement("first_healing_spell");

        assertNotNull(healing);
        assertEquals("first_healing_spell", healing.getId());
        assertEquals("Healing Caster", healing.getName());
        assertEquals("Use your first healing spell", healing.getDescription());
        assertEquals(100, healing.getPoints());

    }

    @Test
    public void firstFireSpellUnlockesFireAchievement(){
        FireSpell fireSpell = new FireSpell();
        manager.onSpellCast(fireSpell);

        assertTrue(manager.isUnlocked("first_fire_spell"));
        assertFalse(manager.isUnlocked("first_healing_spell"));
        assertEquals(100, manager.getTotalPoints());

    }

    @Test
    public void firstHealingSpellUnlockesHealingAchievement(){
        HealingSpell healingSpell = new HealingSpell();
        manager.onSpellCast(healingSpell);

        assertTrue(manager.isUnlocked("first_healing_spell"));
        assertFalse(manager.isUnlocked("first_fire_spell"));
        assertEquals(100, manager.getTotalPoints());

    }

    @Test
    public void unlockingMultipleAchievementsAddsPointsCorrect(){
        FireSpell fireSpell = new FireSpell();
        HealingSpell healingSpell = new HealingSpell();
        manager.onSpellCast( fireSpell);
        manager.onSpellCast(healingSpell);

        assertTrue(manager.isUnlocked("first_fire_spell"));
        assertTrue(manager.isUnlocked("first_healing_spell"));
        assertEquals(200, manager.getTotalPoints());

    }

    @Test
    public void completingSameAchievementsTwiceDoesNotAddPoints(){
        FireSpell fireSpell = new FireSpell();
        manager.onSpellCast(fireSpell);

        assertTrue(manager.isUnlocked("first_fire_spell"));
        assertEquals(100, manager.getTotalPoints());
    }

    @Test
    public void otherSpellsDoesNotUnlockAchievement(){
        Magic[] otherSpells = new Magic[]{new IceSpell(), new ElectricSpell(), new PowerBoostSpell()};

        for(Magic spell : otherSpells){
            AchievementManager tempManager = new AchievementManager();
            tempManager.onSpellCast(spell);

            assertFalse(tempManager.isUnlocked("first_fire_spell"));
            assertFalse(tempManager.isUnlocked("first_healing_spell"));
            assertEquals(0, tempManager.getTotalPoints());
        }
    }
}
