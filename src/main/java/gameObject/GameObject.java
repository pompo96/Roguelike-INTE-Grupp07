package gameObject;

public abstract class GameObject {
    protected int y, x;
    private final char objectSymbol;
    private final String name;

    protected GameObject(int y, int x, char objectSymbol, String name) {
        this.y = y;
        this.x = x;
        this.objectSymbol = objectSymbol;
        this.name = name;
    }

    public char getObjectSymbol() {
        return objectSymbol;
    }

    @Override
    public String toString() {
        return String.valueOf(objectSymbol);
    }
}
