package magic;
import player.Player;
import race.Elf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import equipment.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MagicTest {
    @Test
    public void fireSpell_HasTypeFire(){
        FireSpell fireSpell = new FireSpell();
        assertEquals("fire", fireSpell.getMagicType());
    }
    @Test
    public void fireSpell_DealsExtraDamageToElf(){
        int fireSpellDamageAgainstElf = 20;

        Player caster = mock(Player.class);
        Player target = mock(Player.class);
        when(target.getRace()).thenReturn(new Elf());

        FireSpell fireSpell = new FireSpell();

        int damageOnElf = fireSpell.castSpell(caster, target);

        assertEquals(fireSpellDamageAgainstElf, damageOnElf, "Fire Damage ska bli dubbelt mot elfs");
    }

}

