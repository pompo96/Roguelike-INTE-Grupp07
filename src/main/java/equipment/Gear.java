package equipment;

public class Gear extends Item{

    private final boolean itemExists;
    private int id;
    private String name;
    private String type; //"weapon", "armor", "shoes"
    private int customValue;

    public Gear(String type, int id, String name){
        super(0,0, 'I');
        this.itemExists = true;
        this.type = type;
        this.id = id;
        this.name = name;
    }
    public Gear(String type, int id, String name, int customValue){
        super(0,0, 'I');
        this.itemExists = true;
        this.type = type;
        this.id = id;
        this.name = name;
        this.customValue = customValue;
    }

    public int getLifeModifier() { return type.equals("armour") ? customValue : 0; }
    public int getMovementModifier() { return type.equals("shoes") ? customValue : 0; }
    public int getWeaponDamage() { return type.equals("weapon") ? customValue : 0; }

    @Override
    public int getItemID() {
        return 0;
    }

    public boolean getValue() {
        return itemExists;
    }

    public int getItemId() {
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
