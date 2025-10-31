package achievement;

import magic.*;
import java.util.HashMap;
import java.util.Map;

public class AchievementManager {

    private final Map<String, Achievement> achievements;
    private int totalPoints = 0;

    public AchievementManager(){
        achievements = new HashMap<>();
        achievements.put("first_fire_spell", new Achievement("first_fire_spell", "Fire Caster", "Use your first fire spell", 100));
        achievements.put("first_healing_spell", new Achievement("first_healing_spell", "Healing Caster", "Use your first healing spell", 100));
    }

    public Achievement getAchievement(String id){
        return achievements.get(id);
    }

    public int getTotalPoints(){
        return totalPoints;
    }

    public boolean isUnlocked(String id){
        Achievement achieve = achievements.get(id);
        return achieve != null && achieve.isUnlocked();
    }

    public void onSpellCast(Magic spell){
        if (spell instanceof FireSpell){
            unlockAchievement("first_fire_spell");
        }else if(spell instanceof HealingSpell){
            unlockAchievement("first_healing_spell");
        }
    }

    public void unlockAchievement(String id){
        Achievement achieve = achievements.get(id);
        if(achieve !=null && !achieve.isUnlocked()){
            achieve.unlock();
            totalPoints += achieve.getPoints();
        }
    }
}
