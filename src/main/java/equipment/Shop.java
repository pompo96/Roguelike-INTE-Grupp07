package equipment;

import player.Player;

import java.util.HashMap;
import java.util.Map;

public class Shop{

    private Map<Integer, Item> stock = new HashMap<>();
    private Map<Integer, Integer> prices = new HashMap<>(); // itemID to price

    public void addItem(Item item, int price) {
        stock.put(item.getItemID(), item);
        prices.put(item.getItemID(), price);
    }

    public boolean purchaseItem(int itemID, Player player) {
        if (!stock.containsKey(itemID)) {return false;}

        Item item = stock.remove(itemID);
        prices.remove(itemID);
        player.addToInventory(item);
        return true;
    }

    public boolean hasItem(int itemID) {
        return stock.containsKey(itemID);
    }

    public int getItemPrice(int itemID) {
        return prices.getOrDefault(itemID, 0);
    }

    public int getStockSize() {
        return stock.size();
    }


}
