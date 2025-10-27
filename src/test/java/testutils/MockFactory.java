package testutils;

import equipment.Item;
import player.Player;
import race.Race;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class MockFactory {
    public static Player createMockPlayer() {
        Race mockRace = mock(Race.class);
        when(mockRace.getLifeModifier()).thenReturn(20);
        when(mockRace.getMovementModifier()).thenReturn(2);
        when(mockRace.getAttackPowerModifier()).thenReturn(5);
        when(mockRace.getName()).thenReturn("human");

        Map<String, Item> defaultItems = new HashMap<>();
        defaultItems.put("weapon", createMockWeapon("defaultWeapon", 10));
        defaultItems.put("chestpiece", createMockChestpiece("defaultChestpiece", 10));
        defaultItems.put("boots", createMockBoots("defaultBoots", 10));

        return new Player(mockRace, defaultItems, '@', "MockPlayer");
    }

    public static Item createMockWeapon(String name, int damage) {
        Item weapon = mock(Item.class);
        when(weapon.getName()).thenReturn(name);
        when(weapon.getWeaponDamage()).thenReturn(damage);
        when(weapon.getMovementModifier()).thenReturn(0);
        when(weapon.getLifeModifier()).thenReturn(0);
        return weapon;
    }

    public static Item createMockChestpiece(String name, int life) {
        Item chest = mock(Item.class);
        when(chest.getName()).thenReturn(name);
        when(chest.getLifeModifier()).thenReturn(life);
        when(chest.getWeaponDamage()).thenReturn(0);
        when(chest.getMovementModifier()).thenReturn(0);
        return chest;
    }

    public static Item createMockBoots(String name, int movementSpeed) {
        Item boots = mock(Item.class);
        when(boots.getName()).thenReturn(name);
        when(boots.getMovementModifier()).thenReturn(movementSpeed);
        when(boots.getLifeModifier()).thenReturn(0);
        when(boots.getWeaponDamage()).thenReturn(0);
        return boots;
    }

    public static Player.Quest createMockQuest(String id, String name, String desc) {
        Player.Quest quest = mock(Player.Quest.class);
        when(quest.getID()).thenReturn(id);
        when(quest.getName()).thenReturn(name);
        when(quest.getDescription()).thenReturn(desc);
        return quest;
    }

}
