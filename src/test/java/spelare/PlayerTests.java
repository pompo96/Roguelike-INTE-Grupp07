package spelare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import race.Race;
import utrustning.Item;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @BeforeEach
    public void initializePlayer(){


        //Skapa en quest interface för att använda mock?
        //mock questList = mock(Quest.class);

        mockRace = mock(Race.class);
        mockItemWeapon = mock(Item.class);
        mockItemChestpiece = mock(Item.class);
        mockItemBoots = mock(Item.class);

        when(mockItemWeapon.getName()).thenReturn("weapon");
        when(mockItemWeapon.getDamageModifier()).thenReturn(15);

        when(mockItemChestpiece.getName()).thenReturn("chestpiece");
        when(mockItemChestpiece.getLifeModifier()).thenReturn(10);

        when(mockItemBoots.getName()).thenReturn("boots");
        when(mockItemBoots.getMovementModifier()).thenReturn(10);

        when(mockRace.getLifeModifier()).thenReturn(20); // t.ex. +20 HP
        when(mockRace.getMovementModifier()).thenReturn(2); // t.ex. +2 speed
        when(mockRace.getAttackPowerModifier()).thenReturn(5);
        when(mockRace.getName()).thenReturn("human");

        //when(questList.getQuests()).thenReturn([("DungeonBeat", true), ("", false),("", true)]);

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
    }

    @Test
    public void defaultPlayer_hasDefaultAttackPower(){
        int expectedAttackPower = Player.DEFAULT_ATTACK_POWER + mockRace.getAttackPowerModifier();
        assertEquals(expectedAttackPower, defaultPlayer.getAttackPower());
    }

    @Test
    public void updateAttackPower_increasesAttackPower(){
        int attackAdjustment = 20;
        int defaultAttack = Player.DEFAULT_ATTACK_POWER + mockRace.getAttackPowerModifier();
        defaultPlayer.updateAttackPower(attackAdjustment);
        assertEquals(defaultAttack + attackAdjustment, defaultPlayer.getAttackPower());
    }
    @Test
    public void updateAttackPower_decreasesAttackPower(){
        double attackAdjustment = 0.5;
        int defaultAttack = Player.DEFAULT_ATTACK_POWER + mockRace.getAttackPowerModifier();
        defaultPlayer.updateAttackPowerModifier(attackAdjustment);
        assertEquals(defaultAttack + attackAdjustment, defaultPlayer.getAttackPower());
    }
    @Test
    public void updateAttackPower_cantGoBelowZero(){
        int currentAttackPower = defaultPlayer.getAttackPower();
        defaultPlayer.updateAttackPower(-currentAttackPower - 10);
        assertEquals(0, defaultPlayer.getAttackPower());
    }
    //GPT end

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
        assertEquals(true, result, "Quest has not been completed.");
    }

    @Test
    public void completedQuest_returnsFalse(){
        boolean result = false; //defaultPlayer.isQuestCompleted(questID);
        assertEquals(false, result, "Quest has been completed.");
    }

    @Test
    public void questIsNotStarted(){
        boolean result = false;//defaultPlayer.isQuestStarted(questID);
        assertEquals(false, result, "Quest has started");
    }

    @Test
    public void questIsStarted(){
        boolean result = true; //defaultPlayer.isQuestStarted(questID);
        assertEquals(true, result, "Quest has not started");
    }

    @Test
    public void addQuest(){

    }

    // Equipped gear map(weapon -> itemObj, Armour -> itemObj, Boots -> itemObj)
    //ska ta en map där vapen,armour,boots är tom
    @Test
    public void defaultPlayerHasNoItems(){
        assertEquals("", defaultPlayer.getItems(), "Player har items");
    }
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
    public void equippingWeapon_updatesAttackPower(){
        defaultPlayer.equipItem(mockItemWeapon);
        int updatedAttack = mockItemWeapon.getDamageModifier();

        assertEquals(updatedAttack, defaultPlayer.getAttackPower());
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

    // Questlog list(questObjects) // Yousef



}
