package equipment;

import gameObject.GameObject;

public abstract class Item extends GameObject {
    public Item(int y, int x, char symbol) {
        super(y, x, symbol);
    }

    public abstract int getLifeModifier();
    public abstract int getMovementModifier();
    public abstract int getWeaponDamage();

    public abstract String getName();
}