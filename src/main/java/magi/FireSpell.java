package magi;

public class FireSpell implements Magic{
    private MagicInformation info = new MagicInformation();
    public String getMagicType(){
        return "fire";
    }
    public int getManaCost(){
        return 10;
    }
    public void castSpell(){
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

