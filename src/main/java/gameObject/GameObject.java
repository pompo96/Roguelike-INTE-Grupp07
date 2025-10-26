package gameObject;

public abstract class GameObject {
    protected int y, x;
    private final char objectSymbol;

    protected GameObject(int y, int x, char objectSymbol) {
        this.y = y;
        this.x = x;
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
