package magic;

import player.Player;

public abstract class SpellDecorator implements Magic {
    protected Magic decoratedSpell;
    public SpellDecorator(Magic decoratedSpell){
        this.decoratedSpell = decoratedSpell;
    }
    @Override
    public String getMagicType(){
        return decoratedSpell.getMagicType();
    }
    @Override
    public int castSpell(Player caster, Player target){
        return decoratedSpell.castSpell(caster, target);
    }
    @Override
    public boolean checkIfAbleToCast(){
        return decoratedSpell.checkIfAbleToCast();
    }
    @Override
    public int checkEnvironmentBoost(){
        return decoratedSpell.checkEnvironmentBoost();
    }
}
