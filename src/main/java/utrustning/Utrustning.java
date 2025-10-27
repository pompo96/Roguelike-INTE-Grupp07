package utrustning;

public class Utrustning implements Item{

    private final boolean itemExists;
    private int id;
    private String name;
    private String type; //"weapon", "armor", "shoes"
    private int customValue;

    public Utrustning(String type, int id, String name){
        this.itemExists = true;
        this.type = type;
        this.id = id;
        this.name = name;
    }
    public Utrustning(String type, int id, String name, int customValue){
        this.itemExists = true;
        this.type = type;
        this.id = id;
        this.name = name;
        this.customValue = customValue;
    }

    public int getLifeModifier() { return type.equals("armour") ? customValue : 0; }
    public int getMovementModifier() { return type.equals("shoes") ? customValue : 0; }
    public int getDamageModifier() { return type.equals("weapon") ? customValue : 0; }

    public boolean getValue() {
        return itemExists;
    }
    public int getID() {
        return id;
    }
    public String getRealName() {
        return name;
    }
    public String getName() {
        return type;
    }
    public int getCustomValue(){
        return customValue;
    }

}
