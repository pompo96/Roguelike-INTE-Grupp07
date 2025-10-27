package utrustning;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtrustningTest {


    @Test
    public void weaponHadIDNameStatsAndTypeBasic(){
        Utrustning sword = new Utrustning("weapon", 1, "Flamesword");
        assertTrue(sword.getValue());
        assertEquals(1, sword.getID());
        assertEquals("Flamesword", sword.getRealName());
        assertEquals(0, sword.getDamageModifier());
        assertEquals("weapon", sword.getName());
    }
    @Test
    public void weaponHadIDNameStatsAndType(){
        Utrustning sword = new Utrustning("weapon", 1, "Flamesword", 55);
        assertTrue(sword.getValue());
        assertEquals(1, sword.getID());
        assertEquals("Flamesword", sword.getRealName());
        assertEquals(55, sword.getDamageModifier());
        assertEquals("weapon", sword.getName());
    }

    @Test
    public void armorHadIDNameStatsAndTypeBasic(){
        Utrustning armour = new Utrustning("armour", 2, "Chestplate");
        assertTrue(armour.getValue());
        assertEquals(2, armour.getID());
        assertEquals("Chestplate", armour.getRealName());
        assertEquals(0, armour.getLifeModifier());
        assertEquals("armour", armour.getName());
    }
    @Test
    public void armorHadIDNameStatsAndType(){
        Utrustning armour = new Utrustning("armour", 2, "Chestplate", 20);
        assertTrue(armour.getValue());
        assertEquals(2, armour.getID());
        assertEquals("Chestplate", armour.getRealName());
        assertEquals(20, armour.getLifeModifier());
        assertEquals("armour", armour.getName());
    }

    @Test
    public void shoesHadIDNameStatsAndTypeBasic(){
        Utrustning shoes = new Utrustning("shoes", 3,"Sandals");
        assertTrue(shoes.getValue());
        assertEquals(3, shoes.getID());
        assertEquals("Sandals", shoes.getRealName());
        assertEquals(0, shoes.getMovementModifier());
        assertEquals("shoes", shoes.getName());
    }
    @Test
    public void shoesHadIDNameStatsAndType(){
        Utrustning shoes = new Utrustning("shoes", 3,"Sandals", 5);
        assertTrue(shoes.getValue());
        assertEquals(3, shoes.getID());
        assertEquals("Sandals", shoes.getRealName());
        assertEquals(5, shoes.getMovementModifier());
        assertEquals("shoes", shoes.getName());
    }



}
