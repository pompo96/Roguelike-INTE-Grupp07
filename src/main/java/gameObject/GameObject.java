package gameObject;

import map.Position;

public abstract class GameObject {
    protected Position position;
    private final char objectSymbol;

    protected GameObject(Position position, char objectSymbol) {
        this.position = position;
        this.objectSymbol = objectSymbol;
    }

    public char getObjectSymbol() {
        return objectSymbol;
    }

    @Override
    public String toString() {
        return String.valueOf(objectSymbol);
    }
}
