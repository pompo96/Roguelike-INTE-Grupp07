package equipment;

public class Gear {

    private final boolean itemExists;
    private int id;
    private String name;
    private int damage;
    private String type; //"weapon", "armor", "shoes"


    public Gear(){
        this.itemExists = true;
    }
    public Gear(int id, String name, int damage){
        this.itemExists = true;
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.type = "weapon";
    }

    public boolean getValue() {
        return itemExists;
    }
    public int getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getDamage() {
        return damage;
    }
    public String getType() {
        return type;
    }

}
