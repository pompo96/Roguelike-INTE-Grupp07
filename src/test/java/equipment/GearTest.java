package equipment;

import org.junit.jupiter.api.Test;
import player.Player;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GearTest {

    private Shop shopStock = new Shop();;
    private Gear sword;



    @Test
    public void shopStockExists(){
        assertNotNull(shopStock);
    }


    @Test
    public void shopCanAddItemsToStock() {
        sword = new Gear("weapon", 1, "Flamesword", 55);
        Gear armour = new Gear("armour", 2, "Cocochest");
        shopStock.addItem(sword, 100);
        shopStock.addItem(armour, 100);

        assertTrue(shopStock.hasItem(1));
        assertTrue(shopStock.hasItem(2));
        assertEquals(2, shopStock.getStockSize());
    }



    @Test
    public void weaponHadIDNameStatsAndTypeBasic(){
        Gear sword = new Gear("weapon", 1, "Flamesword");
        assertTrue(sword.getValue());
        assertEquals(1, sword.getItemID());
        assertEquals("Flamesword", sword.getRealName());
        assertEquals(0, sword.getWeaponDamage());
        assertEquals("weapon", sword.getName());
    }
    @Test
    public void weaponHadIDNameStatsAndType(){
        Gear sword = new Gear("weapon", 1, "Flamesword", 55);
        assertTrue(sword.getValue());
        assertEquals(1, sword.getItemID());
        assertEquals("Flamesword", sword.getRealName());
        assertEquals(55, sword.getWeaponDamage());
        assertEquals("weapon", sword.getName());
    }

    @Test
    public void armorHadIDNameStatsAndTypeBasic(){
        Gear armour = new Gear("armour", 2, "Chestplate");
        assertTrue(armour.getValue());
        assertEquals(2, armour.getItemID());
        assertEquals("Chestplate", armour.getRealName());
        assertEquals(0, armour.getLifeModifier());
        assertEquals("armour", armour.getName());
    }
    @Test
    public void armorHadIDNameStatsAndType(){
        Gear armour = new Gear("armour", 2, "Chestplate", 20);
        assertTrue(armour.getValue());
        assertEquals(2, armour.getItemID());
        assertEquals("Chestplate", armour.getRealName());
        assertEquals(20, armour.getLifeModifier());
        assertEquals("armour", armour.getName());
    }


    @Test
    public void shoesHadIDNameStatsAndTypeBasic(){
        Gear shoes = new Gear("shoes", 3,"Sandals");
        assertTrue(shoes.getValue());
        assertEquals(3, shoes.getItemID());
        assertEquals("Sandals", shoes.getRealName());
        assertEquals(0, shoes.getMovementModifier());
        assertEquals("shoes", shoes.getName());
    }
    @Test
    public void shoesHadIDNameStatsAndType(){
        Gear shoes = new Gear("shoes", 3,"Sandals", 5);
        assertTrue(shoes.getValue());
        assertEquals(3, shoes.getItemID());
        assertEquals("Sandals", shoes.getRealName());
        assertEquals(5, shoes.getMovementModifier());
        assertEquals("shoes", shoes.getName());
    }

    @Test
    public void itemInInventory(){
        Map<Integer, Item> inventory = new HashMap<>();
        Gear shoes = new Gear("shoes", 3,"Sandals", 5);
        inventory.put(shoes.getItemID(), shoes);

        assertEquals(3, shoes.getItemID());
        //push

    }



}
