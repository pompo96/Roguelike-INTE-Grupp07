package ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AiTest {

    PlaceholderMob defaultMob;
    Ai defaultAi;
    Player defaultMockPlayer;

    @BeforeEach
    void InitializeDefaultAi(){
        defaultMob = new PlaceholderMob(new Position(1, 1), 'p', 50, true);
        defaultMockPlayer = mock(Player.class);
        defaultAi = new Ai(defaultMob, defaultMockPlayer);

    }
    @Test
    void testMobIsStoredAndFetchedCorrectly() {
        assertEquals(defaultMob, defaultAi.getMob());
    }

    @Test
    void testPlayerIsStoredAndFetchedCorrectly(){
        assertEquals(defaultMockPlayer, defaultAi.getPlayer());
    }

}
