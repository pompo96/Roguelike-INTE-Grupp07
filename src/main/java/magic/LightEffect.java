package magic;

public class LightEffect extends SpellDecorator {
    public LightEffect(Magic decoratedSpell) {
        super(decoratedSpell);
    }

    public boolean castLightSource(boolean moon){
        boolean castedLight = false;
        if(moon){
            castedLight = true;
        }
        return castedLight;
    }
}
