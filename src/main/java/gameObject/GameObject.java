package gameObject;

public abstract class GameObject {
    private int y, x;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
