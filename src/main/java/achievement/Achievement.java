package achievement;

public class Achievement {

    private final String id;
    private final String name;
    private final String description;
    private final int points;
    private boolean unlocked;

    public Achievement(String id, String name, String description, int points){
        this.id = id;
        this.name = name;
        this.description = description;
        this.points = points;
        this.unlocked = false;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getPoints(){
        return points;
    }

    public boolean isUnlocked(){
        return unlocked;
    }

    public void unlock(){
        this.unlocked = true;
    }

}
