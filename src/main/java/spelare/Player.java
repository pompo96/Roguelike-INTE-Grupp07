/*//Double check if the hashmap for quest was done right
package spelare;

import race.Race;
import equipment.Item;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private int maxLife;
    private int currentLife;
    private int movementSpeed;
    private int attackPower;
    private double attackPowerModifier;
    final Race race;
    static final int DEFAULT_LIFE = 100;
    static final int DEFAULT_MOVEMENT_SPEED = 10;
    static final int DEFAULT_ATTACK_POWER = 0;
    Map<String, Item> items;
    Map<String, Boolean> questProgress;
    private Map<Integer, Item> inventory = new HashMap<>();


    public Player(Race race) {
        this(race, emptyItemList());
    }

    public Player(Race race, Map<String, Item> defaultItems) {
        this.race = race;
        this.maxLife = DEFAULT_LIFE + race.getLifeModifier();
        this.currentLife = this.maxLife;
        this.movementSpeed = DEFAULT_MOVEMENT_SPEED + race.getMovementModifier();
        this.attackPower = DEFAULT_ATTACK_POWER + race.getAttackPowerModifier();
        this.attackPowerModifier = 1;
        items = new HashMap<>();
        items.putAll(defaultItems);
        questProgress = new HashMap<>();
    }

    private static Map<String, Item> emptyItemList() {
        Map<String, Item> emptyItemMap = new HashMap<>();
        emptyItemMap.put("weapon", null);
        emptyItemMap.put("chestpiece", null);
        emptyItemMap.put("boots", null);
        return emptyItemMap;
    }

    public void setQuest(Map<String, Boolean> questProgress){
        this.questProgress = questProgress;
    }

    public void questWasCompleted(String questID){
        if (questID == null || questID.trim().isEmpty()){
            System.out.println("This questID was either an empty string or null");
            return; //Maybe return an exception instead? These checks are for the developers (us) only. Now gotta make some tests for the null values and mocking.
        }
        questProgress.put(questID, true);
        System.out.println(questProgress);
    }
    //Should be used when the player talks to an NPC that gives them a quest
    public void questWasStarted(String questID){
        if (questID == null || questID.trim().isEmpty()){
            System.out.println("This questID was either an empty string or null");
            return;
        }
        questProgress.put(questID, false);
    }
    public boolean isQuestStarted(String questID){
        if (questID == null || questID.trim().isEmpty()){
            return false;
        }
        System.out.println(questProgress);
        return questProgress.containsKey(questID);
    }
    public boolean isQuestComplete(String questID){
        if (questID == null || questID.trim().isEmpty()){
            System.out.println("This questID was either an empty string or null");
            return false;
        }
        System.out.println(questProgress);
        return questProgress.getOrDefault(questID, false);

    }

    public int getMaxLife() {
        return this.maxLife;
    }

    public int getCurrentLife() {
        return this.currentLife;
    }

    public void updateMaxLife(int maxLifeAdjustment) {
        this.maxLife += maxLifeAdjustment;
    }

    public void updateCurrentLife(int currentLifeAdjustment) {
        int finalCurrentLife = currentLife + currentLifeAdjustment;
        if (finalCurrentLife > this.maxLife) {
            finalCurrentLife = this.maxLife;
        }
        this.currentLife = finalCurrentLife;
    }

    public Race getRace() {
        return this.race;
    }

    public Map<String, Item> getItems() {
        return this.items;
    }

    public void equipItem(Item item) {
        if(!items.containsKey(item.getName())){
            return;
        }
        switch(item.getName()){
            case "weapon":
                equipWeapon(item);
                break;
            case "chestpiece":
                equipChestpiece(item);
                break;
            case "boots":
                equipBoots(item);
                break;
            default:
                break;
        }


    }

    public void equipWeapon(Item item) {
        items.put(item.getName(), item);
        updateAttackPower(item.getWeaponDamage());
    }
    public void equipChestpiece(Item item){
        items.put(item.getName(), item);
        updateMaxLife(item.getLifeModifier());
    }

    public void equipBoots(Item item){
        items.put(item.getName(), item);
        updateMovementSpeed(item.getMovementModifier());
    }

    public void addToInventory(Item item) {
        inventory.put(item.getItemID(), item);
    }

    public int getInventorySize() {
        return inventory.size();
    }

    public Item getItemFromInventory(int id) {
        return inventory.get(id);
    }

    public int getMovementSpeed() {
        return this.movementSpeed;
    }

    public void updateMovementSpeed(int adjustment) {
        this.movementSpeed += adjustment;
        if (this.movementSpeed < 0) this.movementSpeed = 0;
    }

    public int getAttackPower() {
        return (int) (attackPower*attackPowerModifier);
    }

    public void updateAttackPower(int weaponAttackPower) {
        this.attackPower = weaponAttackPower;
    }
    public void updateAttackPowerModifier(double damageMultiplier) {
        this.attackPowerModifier  *= damageMultiplier;
    }
}
*/