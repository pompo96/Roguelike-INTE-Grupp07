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
        defaultMob = new PlaceholderMob(new Position(50, 50), 'p', 50, 5, true);
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

    @Test
    void testConstructorSetsStateToIdle(){
        assertEquals(MobState.IDLE, defaultAi.getState());
    }

    @Test
    void testCanSwitchFromIdleToPatrollingAfterSufficientFrames(){
        for(int i=0; i < 3; i++){
            defaultAi.update();
        }
        assertEquals(MobState.PATROLLING, defaultAi.getState());
    }

    @Test
    void testCanSwitchFromPatrollingToIdle(){
        for(int i=0; i < 3; i++){
            defaultAi.update();
        }
        Position customDestination = new Position(54, 48);
        defaultAi.setDestination(customDestination);
        defaultAi.update();
        assertEquals(MobState.IDLE, defaultAi.getState());
    }

    @Test
    void testPatrollingSetsDestinationNotCurrentPosition(){
        Position currentPosition = defaultAi.getMob().getCurrentPosition();
        for(int i=0; i < 3; i++){
            defaultAi.update();
        }
        assertNotEquals(currentPosition, defaultAi.getDestination());
    }

    @Test
    void testPatrollingMovesMob(){
        for(int i=0; i < 3; i++){
            defaultAi.update();
        }
        Position customDestination = new Position(53, 53);
        defaultAi.setDestination(customDestination);
        defaultAi.update();
        Position afterPatroll = defaultAi.getMob().getCurrentPosition();
        assertEquals(customDestination, afterPatroll);

    }

    @Test
    void testOnlyMovesMobTheirMovementSpeedPerFrame(){
        for(int i=0; i < 3; i++){
            defaultAi.update();
        }
        Position customDestination = new Position(60, 50 );
        Position expectedMovementInOneFrame = new Position(50 + defaultAi.getMob().getMovementSpeed(),50);
        defaultAi.setDestination(customDestination);
        defaultAi.update();
        assertEquals(expectedMovementInOneFrame, defaultAi.getMob().getCurrentPosition());
    }


}
