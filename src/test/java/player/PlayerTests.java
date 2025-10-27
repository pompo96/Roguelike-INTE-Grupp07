package player;

import equipment.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import race.Race;
import testutils.MockFactory;


import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlayerTests {

    private Player mockPlayer;
    private Player.Quest mockQuest1;
    private Player.Quest mockQuest2;

    @BeforeEach
    void setUp() {
        mockPlayer = MockFactory.createMockPlayer();
        mockQuest1 = MockFactory.createMockQuest("DungeonBeat", "Beat the Dungeon", "Defeat all enemies in the dungeon");
        mockQuest2 = MockFactory.createMockQuest("LostAndFound", "Find the Lost Artifact", "Search for the artifact in the forest");
    }

    // --- Life tests ---

    @Test
    public void defaultPlayer_hasDefaultHp() {
        int totalLife = Player.DEFAULT_LIFE + mockPlayer.getRace().getLifeModifier();
        assertEquals(totalLife, mockPlayer.getMaxLife());
    }

    @Test
    public void updateMaxLife_updatesMaxLife() {
        int lifeAdjustment = 10;
        int expected = mockPlayer.getMaxLife() + lifeAdjustment;
        mockPlayer.updateMaxLife(lifeAdjustment);
        assertEquals(expected, mockPlayer.getMaxLife());
    }

    @Test
    public void defaultPlayer_hasDefaultCurrentLife() {
        assertEquals(mockPlayer.getMaxLife(), mockPlayer.getCurrentLife());
    }

    @Test
    public void updateCurrentLife_updatesCurrentLife() {
        int adjustment = -10;
        int expected = mockPlayer.getCurrentLife() + adjustment;
        mockPlayer.updateCurrentLife(adjustment);
        assertEquals(expected, mockPlayer.getCurrentLife());
    }

    @Test
    public void updateCurrentLife_limitedByMaxLife() {
        mockPlayer.updateCurrentLife(9999);
        assertEquals(mockPlayer.getMaxLife(), mockPlayer.getCurrentLife());
    }

    // --- Movement speed tests ---

    @Test
    public void defaultPlayer_hasDefaultMovementSpeed() {
        int expected = Player.DEFAULT_MOVEMENT_SPEED + mockPlayer.getRace().getMovementModifier();
        assertEquals(expected, mockPlayer.getMovementSpeed());
    }

    @Test
    public void updateMovementSpeed_increasesSpeed() {
        int start = mockPlayer.getMovementSpeed();
        mockPlayer.updateMovementSpeed(5);
        assertEquals(start + 5, mockPlayer.getMovementSpeed());
    }

    @Test
    public void updateMovementSpeed_decreasesSpeed() {
        int start = mockPlayer.getMovementSpeed();
        mockPlayer.updateMovementSpeed(-3);
        assertEquals(start - 3, mockPlayer.getMovementSpeed());
    }

    // --- Attack power tests ---

    @Test
    public void defaultPlayer_hasDefaultAttackPower() {
        int expected = Player.DEFAULT_ATTACK_POWER + mockPlayer.getRace().getAttackPowerModifier();
        assertEquals(expected, mockPlayer.getBaseAttackPower());
    }

    @Test
    public void updateAttackPower_increasesBaseAttackPower() {
        int base = mockPlayer.getBaseAttackPower();
        mockPlayer.updateBaseAttackPower(10);
        assertEquals(base + 10, mockPlayer.getBaseAttackPower());
    }

    @Test
    public void updateBaseAttackPower_cantGoBelowZero() {
        mockPlayer.updateBaseAttackPower(-9999);
        assertEquals(0, mockPlayer.getBaseAttackPower());
    }

    @Test
    public void attackEffectModifier_startsAtZero() {
        assertEquals(0, mockPlayer.getAttackPowerEffectModifier());
    }

    @Test
    public void updatingAttackEffectModifier_setsNewValue() {
        mockPlayer.updateAttackPowerEffectModifier(3);
        assertEquals(3, mockPlayer.getAttackPowerEffectModifier());
    }

    // --- Race getter ---

    @Test
    public void getRace_returnsRace() {
        Race race = mockPlayer.getRace();
        assertNotNull(race);
        assertEquals("human", race.getName());
    }

    // --- Equipment tests ---

    @Test
    public void playerWithStarterItems_hasItems() {
        Map<String, Item> items = mockPlayer.getItems();
        assertEquals(3, items.size(), "Player should start with 3 equipped items");
        assertTrue(items.containsKey("weapon"));
        assertTrue(items.containsKey("chestpiece"));
        assertTrue(items.containsKey("boots"));
    }

    @Test
    public void equippingWeapon_updatesWeaponSlot() {
        Item newWeapon = MockFactory.createMockWeapon("weapon", 10);
        mockPlayer.equipItem(newWeapon);
        assertEquals(newWeapon, mockPlayer.getItems().get("weapon"));
    }

    @Test
    public void equippingChestpiece_updatesLife() {
        int before = mockPlayer.getMaxLife();
        Item newChest = MockFactory.createMockChestpiece("chestpiece", 10);
        mockPlayer.equipItem(newChest);
        int expected = before + newChest.getLifeModifier();
        assertEquals(expected, mockPlayer.getMaxLife());
    }

    @Test
    public void equippingBoots_updatesMovementSpeed() {
        int before = mockPlayer.getMovementSpeed();
        Item newBoots = MockFactory.createMockBoots("boots", 10);
        mockPlayer.equipItem(newBoots);
        int expected = before + newBoots.getMovementModifier();
        assertEquals(expected, mockPlayer.getMovementSpeed());
    }

    // --- Quest log tests ---
    @Test
    public void questLog_startsClosedByDefault() {
        assertFalse(mockPlayer.isQuestLogOpen());
    }

    @Test
    public void openQuestLog_opensTheLog() {
        mockPlayer.openQuestLog();
        assertTrue(mockPlayer.isQuestLogOpen());
    }

    @Test
    public void closeQuestLog_closesTheLog() {
        mockPlayer.openQuestLog();
        mockPlayer.closeQuestLog();
        assertFalse(mockPlayer.isQuestLogOpen());
    }

    @Test
    public void acceptQuest_addsQuestToLog() {
        mockPlayer.acceptQuest(mockQuest1);
        assertEquals(1, mockPlayer.getQuestCount());
    }

    @Test
    public void acceptMultipleQuests_addsAllQuests() {
        mockPlayer.acceptQuest(mockQuest1);
        mockPlayer.acceptQuest(mockQuest2);
        assertEquals(2, mockPlayer.getQuestCount());
    }

    @Test
    public void acceptDuplicateQuest_doesNotAddDuplicate() {
        mockPlayer.acceptQuest(mockQuest1);
        mockPlayer.acceptQuest(mockQuest1);
        assertEquals(1, mockPlayer.getQuestCount());
    }

    @Test
    public void getActiveQuests_returnsAllQuests() {
        mockPlayer.acceptQuest(mockQuest1);
        mockPlayer.acceptQuest(mockQuest2);
        List<Player.Quest> quests = mockPlayer.getActiveQuests();
        assertEquals(2, quests.size());
    }

    @Test
    public void getQuest_returnsQuestByID() {
        mockPlayer.acceptQuest(mockQuest1);
        Player.Quest found = mockPlayer.getQuest("DungeonBeat");
        assertEquals(mockQuest1, found);
    }

    @Test
    public void getQuest_returnsNullIfNotFound() {
        mockPlayer.acceptQuest(mockQuest1);
        Player.Quest found = mockPlayer.getQuest("Nonexistent");
        assertNull(found);
    }

    @Test
    public void abandonQuest_removesQuestFromLog() {
        mockPlayer.acceptQuest(mockQuest1);
        mockPlayer.acceptQuest(mockQuest2);
        mockPlayer.abandonQuest(mockQuest1);
        assertEquals(1, mockPlayer.getQuestCount());
    }

    @Test
    public void abandonQuest_correctQuestRemoved() {
        mockPlayer.acceptQuest(mockQuest1);
        mockPlayer.acceptQuest(mockQuest2);
        mockPlayer.abandonQuest(mockQuest1);
        Player.Quest remaining = mockPlayer.getQuest("LostAndFound");
        assertEquals(mockQuest2, remaining);
    }
}
