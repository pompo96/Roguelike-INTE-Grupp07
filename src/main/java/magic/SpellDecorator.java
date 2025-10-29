package magic;

import player.Player;

public abstract class SpellDecorator implements Magic {
    protected Magic decoratedSpell;

    public SpellDecorator(Magic decoratedSpell) {
        this.decoratedSpell = decoratedSpell;
    }

    @Override
    public String getMagicType() {
        return decoratedSpell.getMagicType();
    }

    @Override
    public int getNumberOfUses() {
        return decoratedSpell.getNumberOfUses();
    }
    @Override
    public boolean checkIfAbleToCast(){
        return decoratedSpell.checkIfAbleToCast();
    }
    @Override
    public int checkEnvironmentBoost(){
        return decoratedSpell.checkEnvironmentBoost();
    }
    @Override
    public void castSpell(Player caster, Player target) {
        decoratedSpell.castSpell(caster, target);
    }

    @Override
    public void accept(SpellVisitor visitor, Player caster, Player target) {
        decoratedSpell.accept(visitor, caster, target);
    }
}
