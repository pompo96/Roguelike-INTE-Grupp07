package equipment;

import static org.mockito.Mockito.*;


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
    public void shopReturnsZeroForMissingItemPrice() {
        assertEquals(0, shopStock.getItemPrice(999));
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
    public void shopReturnsCorrectPriceForExistingItem() {
        Gear sword = new Gear("weapon", 1, "Flamesword", 55);
        shopStock.addItem(sword, 150);
        assertEquals(150, shopStock.getItemPrice(1));
    }


    @Test
    public void purchaseItemRemovesFromShopAndAddsToPlayerInventory() {
        Player mockPlayer = mock(Player.class);

        Gear sword = new Gear("weapon", 1, "Iron Sword", 15);
        shopStock.addItem(sword, 100);

        boolean purchased = shopStock.purchaseItem(1, mockPlayer);

        assertTrue(purchased);
        assertEquals(0, shopStock.getStockSize());
        assertFalse(shopStock.hasItem(1));

        verify(mockPlayer).addToInventory(sword);
    }

    @Test
    public void purchaseNonExistentItemReturnsFalse() {
        Player mockPlayer = mock(Player.class);

        boolean result = shopStock.purchaseItem(999, mockPlayer);

        assertFalse(result);
        verify(mockPlayer, never()).addToInventory(any());
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
    }

    @Test
    public void getCustomValue(){
        Gear shoes = new Gear("shoes", 3,"Sandals", 5);
        assertEquals(5, shoes.getCustomValue());
    }



}
