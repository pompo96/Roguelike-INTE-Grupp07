package magic;

public interface Magic {
    String getMagicType();
    int getManaCost();
    void castSpell();
    boolean checkIfAbleToCast(MagicInformation info);
    int checkEnvironmentBoost(MagicInformation info);

}
