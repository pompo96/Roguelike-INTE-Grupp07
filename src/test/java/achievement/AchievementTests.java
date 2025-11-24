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
    public void totalPointsStartsWithZero(){
        assertEquals(0, manager.getTotalPoints());
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

    @Test
    public void getAchievementWithInvalidIdShouldReturnNull(){
        assertNull(manager.getAchievement("invalid_id"));
    }

    //fick inte full täckning på isUnlocked()-metoden då jag hade missat ett test som kollar
    //testar ett ogiltigt id som därav gör att achievement == null. Jag hade endast test som
    //kollade vad som hände när ett achievement redan var unlocked, och därav även != null.
    @Test
    public void isUnlockedWithInvalidIdReturnsFalse(){
        assertFalse(manager.isUnlocked("invalid_id"));
    }

    //ännu en metod (unlockAchievement()) som inte hade full branch-covarage, endast 3/4
    //Även detta pga att ett ogiltigt Id aldrig testades, utan metoden testades med giltiga id:n,
    //där achievements antingen redan va unlocked och inte gick att låsa upp eller att det var en
    //spell som inte var någon achievement att använda
    @Test
    public void unlockAchievementWithInvalidIdDoesNothing(){
        int pointsBefore = manager.getTotalPoints();
        manager.unlockAchievement("invalid_id");
        assertEquals(pointsBefore, manager.getTotalPoints());
    }
}
