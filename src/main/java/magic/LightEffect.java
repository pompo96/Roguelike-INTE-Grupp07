package magic;

public class LightEffect extends SpellDecorator {
    private boolean moon;
    public LightEffect(Magic decoratedSpell) {
        super(decoratedSpell);
    }

    public boolean castLightSource(boolean moon){
        boolean castedLight = false;
        if(moon){
            castedLight = true;
        }
        if(!moon){
            castedLight = false;
        }
        return castedLight;
    }
}
