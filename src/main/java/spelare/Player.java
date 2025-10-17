package spelare;

import race.Race;
import utrustning.Item;

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

    public void questWasCompleted(String questID){
        questProgress.put(questID, true);
    }
    public boolean isQuestStarted(String questID){
        return questProgress.containsKey(questID);
    }
    public boolean isQuestComplete(String questID){
        return questProgress.get(questID);
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
        updateAttackPower(item.getDamageModifier());
    }
    public void equipChestpiece(Item item){
        items.put(item.getName(), item);
        updateMaxLife(item.getLifeModifier());
    }

    public void equipBoots(Item item){
        items.put(item.getName(), item);
        updateMovementSpeed(item.getMovementModifier());
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
