package magic;

import player.Player;
import race.Elf;

public class FireSpell implements Magic{
    private int damage = getBaseDamage();
    private MagicInformation info = new MagicInformation();
    public String getMagicType(){
        return "fire";
    }
    public int damageCalculation(Player caster, Player target){
        if(target.getRace() instanceof Elf){
            damage *= 2;
        }
        return damage;
    }
    public void castSpell(Player caster, Player target){
        if(checkIfAbleToCast(info)){
            //cast spell
        }
    }
    public boolean checkIfAbleToCast(MagicInformation info){
        // if(Om race tillåter det, mana cost, environment etc)
        return true;
    }
    public int checkEnvironmentBoost(MagicInformation info){
        //Om du är i gräs område -> damage boost
        //Om du är i vatten område -> damage decrease
        return 1;
    }
}

