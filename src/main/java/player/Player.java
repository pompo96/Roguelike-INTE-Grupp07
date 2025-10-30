package player;

import gameObject.GameObject;
import race.Race;
import equipment.Item;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {
    private int maxLife;
    private int currentLife;
    private int movementSpeed;
    private int baseAttackPower;
    private int attackPowerEffectModifier;
    final Race race;
    static final int DEFAULT_LIFE = 100;
    static final int DEFAULT_MOVEMENT_SPEED = 10;
    static final int DEFAULT_ATTACK_POWER = 10;
    Map<String, Item> items;
    Map<String, Boolean> questProgress;
    private List<Quest> activeQuests;
    private boolean questLogOpen;
    private Map<String, Object> questItems;


    public Player(Race race, Map<String, Item> defaultItems, char symbol, String name) {
        super(0,0, symbol);
        this.race = race;
        this.maxLife = DEFAULT_LIFE + race.getLifeModifier();
        this.currentLife = this.maxLife;
        this.movementSpeed = DEFAULT_MOVEMENT_SPEED + race.getMovementModifier();
        this.baseAttackPower = DEFAULT_ATTACK_POWER + race.getAttackPowerModifier();
        this.attackPowerEffectModifier = 0;
        items = new HashMap<>();
        items.putAll(defaultItems);
        questProgress = new HashMap<>();
        this.activeQuests = new ArrayList<>();
        this.questLogOpen = false;
        this.questItems = new HashMap<>();
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
        //updateAttackPower(item.getDamageModifier());
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


    public int getBaseAttackPower() {
        return this.baseAttackPower;
    }

    public int getAttackPowerEffectModifier(){
        return this.attackPowerEffectModifier;
    }
    public void updateBaseAttackPower(int adjustment) {
        this.baseAttackPower += adjustment;
        if(this.baseAttackPower < 0) this.baseAttackPower = 0;
    }
    public void updateAttackPowerEffectModifier(int effectModifier) {
        this.attackPowerEffectModifier  = effectModifier;
    }

//    public int calculateDamage(){
//        if (items.get("weapon") == null) return 0;
//        int finalAttackPower = baseAttackPower + attackPowerEffectModifier;
//        if (finalAttackPower < 1) finalAttackPower = 1;
//        return items.get("weapon").getWeaponDamage() + finalAttackPower;
//    }


public void openQuestLog() {
    this.questLogOpen = true;
}

    public void closeQuestLog() {
        this.questLogOpen = false;
    }

    public boolean isQuestLogOpen() {
        return this.questLogOpen;
    }

    public void acceptQuest(Quest quest) {
        if (quest != null && !activeQuests.contains(quest)) {
            activeQuests.add(quest);
        }
    }

    public void abandonQuest(Quest quest) {
        activeQuests.remove(quest);
    }

    public List<Quest> getActiveQuests() {
        return new ArrayList<>(activeQuests);
    }

    public Quest getQuest(String questID) {
        for (Quest quest : activeQuests) {
            if (quest.getID().equals(questID)) {
                return quest;
            }
        }
        return null;
    }

    public int getQuestCount() {
        return activeQuests.size();
    }


    public interface Quest {
        String getID();
        String getName();
        String getDescription();
    }

    public void addQuestItem(String itemName, Object item) {
        this.questItems.put(itemName, item);
    }


    public boolean hasItem(String itemName) {
        return this.questItems.containsKey(itemName) && this.questItems.get(itemName) != null;
    }
}


