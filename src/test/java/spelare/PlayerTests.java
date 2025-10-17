package spelare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import race.Race;
import utrustning.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Spelare attribut
 * currentLife
 * maxLife (default += raceModifier)
 * //100 liv, base movement, inga koordinater, inget equipped gear, inget yrke, behöver och ras
 * Race (Race objekt)
 * Story progress map (questname -> completed(boolean))
 * Movement speed (deault += raceModifier)
 * Koordinater ??
 * Equipped gear map(weapon -> itemObj, Armour -> itemObj, Boots -> itemObj)
 * Questlog list(questObjects)
 */

public class PlayerTests {
    Player defaultPlayer;
    Race mockRace;
    Item mockItemWeapon;
    Item mockItemChestpiece;
    Item mockItemBoots;
    Player.Quest mockQuest1;
    Player.Quest mockQuest2;
    //Quest questList;

    @BeforeEach
    public void initializePlayer(){


        //Skapa en quest interface för att använda mock?
        //mock questList = mock(Quest.class);

        mockRace = mock(Race.class);
        mockItemWeapon = mock(Item.class);
        mockItemChestpiece = mock(Item.class);
        mockItemBoots = mock(Item.class);

        when(mockItemWeapon.getName()).thenReturn("weapon");
        when(mockItemWeapon.getWeaponDamage()).thenReturn(15);

        when(mockItemChestpiece.getName()).thenReturn("chestpiece");
        when(mockItemChestpiece.getLifeModifier()).thenReturn(10);

        when(mockItemBoots.getName()).thenReturn("boots");
        when(mockItemBoots.getMovementModifier()).thenReturn(10);

        when(mockRace.getLifeModifier()).thenReturn(20); // t.ex. +20 HP
        when(mockRace.getMovementModifier()).thenReturn(2); // t.ex. +2 speed
        when(mockRace.getAttackPowerModifier()).thenReturn(5);
        when(mockRace.getName()).thenReturn("human");

        mockQuest1 = mock(Player.Quest.class);
        when(mockQuest1.getID()).thenReturn("DungeonBeat");
        when(mockQuest1.getName()).thenReturn("Beat the Dungeon");
        when(mockQuest1.getDescription()).thenReturn("Defeat all enemies in the dungeon");

        mockQuest2 = mock(Player.Quest.class);
        when(mockQuest2.getID()).thenReturn("LostAndFound");
        when(mockQuest2.getName()).thenReturn("Find the Lost Artifact");
        when(mockQuest2.getDescription()).thenReturn("Search for the artifact in the forest");

        //when(questList.getQuests()).thenReturn([("DungeonBeat", true), ("LostAndFound", false),("Tutorial", true)]);

        defaultPlayer = new Player(mockRace);
    }

    @Test
    public void defaultPlayer_hasDefaultHp(){
        int totalLife = Player.DEFAULT_LIFE + mockRace.getLifeModifier();
        assertEquals(totalLife, defaultPlayer.getMaxLife());
    }

    @Test
    public void updateMaxLife_updatesMaxLife() {
        int lifeAdjustment = 10;
        int defaultLife = Player.DEFAULT_LIFE + mockRace.getLifeModifier();
        defaultPlayer.updateMaxLife(10);

        assertEquals(defaultLife+lifeAdjustment, defaultPlayer.getMaxLife());
    }

    @Test
    public void defaultPlayer_hasDefaultCurrentLife(){
        int defaultLife = Player.DEFAULT_LIFE + mockRace.getLifeModifier();
        assertEquals(defaultLife, defaultPlayer.getCurrentLife());
    }

    @Test
    public void updateCurrentLife_updatesCurrentLife(){
        int defaultLife = Player.DEFAULT_LIFE + mockRace.getLifeModifier();
        int lifeAdjustment = -10;
        defaultPlayer.updateCurrentLife(lifeAdjustment);
        assertEquals(defaultLife+lifeAdjustment, defaultPlayer.getCurrentLife());
    }

    @Test
    public void updateCurrentLife_limitedByMaxLife(){
        defaultPlayer.updateCurrentLife(10);
        assertEquals(defaultPlayer.getMaxLife(), defaultPlayer.getCurrentLife());
    }


    //GPT start
    // Prompt -> "implement movement speed tests like we implemented life tests"
    @Test
    public void defaultPlayer_hasDefaultMovementSpeed() {
        int expectedSpeed = Player.DEFAULT_MOVEMENT_SPEED + mockRace.getMovementModifier();
        assertEquals(expectedSpeed, defaultPlayer.getMovementSpeed());
    }

    @Test
    public void updateMovementSpeed_increasesSpeed() {
        int speedAdjustment = 5;
        int defaultSpeed = Player.DEFAULT_MOVEMENT_SPEED + mockRace.getMovementModifier();

        defaultPlayer.updateMovementSpeed(speedAdjustment);

        assertEquals(defaultSpeed + speedAdjustment, defaultPlayer.getMovementSpeed());
    }

    @Test
    public void updateMovementSpeed_decreasesSpeed() {
        int speedAdjustment = -3;
        int defaultSpeed = Player.DEFAULT_MOVEMENT_SPEED + mockRace.getMovementModifier();

        defaultPlayer.updateMovementSpeed(speedAdjustment);

        assertEquals(defaultSpeed + speedAdjustment, defaultPlayer.getMovementSpeed());
    }    //GPT end

    @Test
    public void defaultPlayer_hasDefaultAttackPower(){
        int expectedAttackPower = Player.DEFAULT_ATTACK_POWER + mockRace.getAttackPowerModifier();
        assertEquals(expectedAttackPower, defaultPlayer.getBaseAttackPower());
    }

    @Test
    public void updateAttackPower_increasesBaseAttackPower(){
        int attackAdjustment = 20;
        int defaultAttack = Player.DEFAULT_ATTACK_POWER + mockRace.getAttackPowerModifier();
        defaultPlayer.updateBaseAttackPower(attackAdjustment);
        assertEquals(defaultAttack + attackAdjustment, defaultPlayer.getBaseAttackPower());
    }
    @Test
    public void updateBaseAttackPower_decreasesBaseAttackPower(){
        int attackAdjustment = 2;
        int defaultAttack = Player.DEFAULT_ATTACK_POWER + mockRace.getAttackPowerModifier();
        defaultPlayer.updateBaseAttackPower(attackAdjustment);
        assertEquals(defaultAttack + attackAdjustment, defaultPlayer.getBaseAttackPower());
    }

    @Test
    public void updateBaseAttackPower_cantGoBelowZero(){
        int currentAttackPower = defaultPlayer.getBaseAttackPower();
        defaultPlayer.updateBaseAttackPower(-currentAttackPower - 10);
        assertEquals(0, defaultPlayer.getBaseAttackPower());
    }

    @Test
    public void newDefaultCharacter_hasZeroAttackEffectModifier()
    {
        assertEquals(0, defaultPlayer.getAttackPowerEffectModifier());
    }

    @Test
    public void updatingAttackEffectModifier_setsAttackEffectModifier(){
        int effectValue = 2;
        defaultPlayer.updateAttackPowerEffectModifier(effectValue);
        assertEquals(effectValue, defaultPlayer.getAttackPowerEffectModifier());
    }

    @Test
    public  void updatingAttackEffectModifier_overridesPreviousValue(){
        int firstEffectValue = 3;
        int secondEffectValue = -4;
        defaultPlayer.updateAttackPowerEffectModifier(firstEffectValue);
        defaultPlayer.updateAttackPowerEffectModifier(secondEffectValue);
        assertEquals(secondEffectValue, defaultPlayer.getAttackPowerEffectModifier());
    }

    // Race (Race objekt)
    // getter for race
    //
    @Test
    public void getRace_returnsRace(){
        Race race = defaultPlayer.getRace();
        assertEquals(mockRace, race);
    }
    // Story progress map (questname -> completed(boolean))

    @Test
    public void completedQuest_returnsTrue(){
        boolean result = true; //defaultPlayer.isQuestCompleted(questID);
        assertTrue(result, "Quest has not been completed.");
    }

    @Test
    public void completedQuest_returnsFalse(){
        boolean result = false; //defaultPlayer.isQuestCompleted(questID);
        assertFalse(result, "Quest has been completed.");
    }

    @Test
    public void questIsNotStarted(){
        boolean result = false;//defaultPlayer.isQuestStarted(questID);
        assertFalse(result, "Quest has started");
    }

    @Test
    public void questIsStarted(){
        boolean result = true; //defaultPlayer.isQuestStarted(questID);
        assertTrue(result, "Quest has not started");
    }

    @Test
    public void addQuest(){

    }

    // Equipped gear map(weapon -> itemObj, Armour -> itemObj, Boots -> itemObj)
    //ska ta en map där vapen,armour,boots är tom
    // todo player ska ha en default item list
//    @Test
//    public void defaultPlayerHasNoItems(){
//        assertEquals("", defaultPlayer.getItems(), "Player har items");
//    }

    @Test
    public void playerWithStarterItems_hasItems(){
        Map<String, Item> items = new HashMap<>();
        items.put("weapon", mockItemWeapon);
        items.put("chestpiece", mockItemChestpiece);
        items.put("boots", mockItemBoots);
        int sizeOfMap = items.size();
        Player player = new Player(mockRace, items);
        assertEquals(player.getItems().size(), sizeOfMap, "getItems metoden returnerar för många nyckelvärden");
        assertEquals(player.getItems().get("helmet"), items.get("helmet"), "Det är inte en helmet");
        assertEquals(player.getItems().get("chestpiece"), items.get("chestpiece"), "Det är inte en chestpiece");
        assertEquals(player.getItems().get("boot"), items.get("boot"), "Det är int en boot");
    }

    @Test
    public void equipItem_properlyEquipsItem(){
        defaultPlayer.equipItem(mockItemWeapon);
        Map<String, Item> items = defaultPlayer.getItems();
        assertEquals(items.get("weapon"), mockItemWeapon);
    }

    //equipitems test + player metod se till att life/movement speed updateras korrekt
    @Test
    public void equippingWeapon_updatesWeapon(){
        defaultPlayer.equipItem(mockItemWeapon);
        Map<String, Item> items = defaultPlayer.getItems();
        Item weapon = items.get(mockItemWeapon.getName());
        assertEquals(weapon, mockItemWeapon);
    }

    @Test
    public void equippingChest_updatesLife(){
        int baseLife = defaultPlayer.getMaxLife();
        defaultPlayer.equipItem(mockItemChestpiece);
        int newLife = baseLife + mockItemChestpiece.getLifeModifier();

        assertEquals(newLife, defaultPlayer.getMaxLife());

    }

    @Test
    public void equippingBoots_updatesMovementSpeed(){
        int baseMovementSpeed = defaultPlayer.getMovementSpeed();
        defaultPlayer.equipItem(mockItemBoots);
        int newMovementSpeed = baseMovementSpeed + mockItemBoots.getMovementModifier();

        assertEquals(newMovementSpeed, defaultPlayer.getMovementSpeed());
    }

    @Test
    public void questLog_startsClosedByDefault() {
        assertFalse(defaultPlayer.isQuestLogOpen(), "QuestLog borde vara stängd från början");
    }

    @Test
    public void openQuestLog_opensTheLog() {
        defaultPlayer.openQuestLog();
        assertTrue(defaultPlayer.isQuestLogOpen(), "QuestLog borde vara öppen");
    }

    @Test
    public void closeQuestLog_closesTheLog() {
        defaultPlayer.openQuestLog();
        defaultPlayer.closeQuestLog();
        assertFalse(defaultPlayer.isQuestLogOpen(), "QuestLog borde vara stängd");
    }

    @Test
    public void acceptQuest_addsQuestToLog() {
        defaultPlayer.acceptQuest(mockQuest1);
        assertEquals(1, defaultPlayer.getQuestCount(), "QuestLog borde innehålla 1 quest");
    }

    @Test
    public void acceptMultipleQuests_addsAllQuests() {
        defaultPlayer.acceptQuest(mockQuest1);
        defaultPlayer.acceptQuest(mockQuest2);
        assertEquals(2, defaultPlayer.getQuestCount(), "QuestLog borde innehålla 2 quests");
    }

    @Test
    public void acceptDuplicateQuest_doesNotAddDuplicate() {
        defaultPlayer.acceptQuest(mockQuest1);
        defaultPlayer.acceptQuest(mockQuest1);
        assertEquals(1, defaultPlayer.getQuestCount(), "QuestLog borde inte ha dubbletter");
    }

    @Test
    public void getActiveQuests_returnsAllQuests() {
        defaultPlayer.acceptQuest(mockQuest1);
        defaultPlayer.acceptQuest(mockQuest2);
        List<Player.Quest> quests = defaultPlayer.getActiveQuests();
        assertEquals(2, quests.size(), "Borde returnera 2 quests");
    }

    @Test
    public void getQuest_returnsQuestByID() {
        defaultPlayer.acceptQuest(mockQuest1);
        Player.Quest foundQuest = defaultPlayer.getQuest("DungeonBeat");
        assertEquals(mockQuest1, foundQuest, "Borde hitta rätt quest");
    }

    @Test
    public void getQuest_returnsNullIfNotFound() {
        defaultPlayer.acceptQuest(mockQuest1);
        Player.Quest foundQuest = defaultPlayer.getQuest("NonExistent");
        assertNull(foundQuest, "Borde returnera null för icke existerande quest");
    }

    @Test
    public void abandonQuest_removesQuestFromLog() {
        defaultPlayer.acceptQuest(mockQuest1);
        defaultPlayer.acceptQuest(mockQuest2);
        defaultPlayer.abandonQuest(mockQuest1);
        assertEquals(1, defaultPlayer.getQuestCount(), "QuestLog borde innehålla 1 quest efter abandon");
    }

    @Test
    public void abandonQuest_correctQuestRemoved() {
        defaultPlayer.acceptQuest(mockQuest1);
        defaultPlayer.acceptQuest(mockQuest2);
        defaultPlayer.abandonQuest(mockQuest1);

        Player.Quest remaining = defaultPlayer.getQuest("LostAndFound");
        assertEquals(mockQuest2, remaining, "LostAndFound borde fortfarande vara i loggen");
    }



    // Questlog list(questObjects) // Yousef



}
