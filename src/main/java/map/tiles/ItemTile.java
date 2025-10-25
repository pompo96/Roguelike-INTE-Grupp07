package map.tiles;

import equipment.Item;
import map.tileFactory.Tile;

public class ItemTile extends Tile {
    private Item item;

    public ItemTile(int y, int x, Item item) {
        this.item = item;
        super(y, x, true, item.getSymbol());
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
