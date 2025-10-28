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
    void initializeDefaultAi(){
        defaultMob = new PlaceholderMob(new Position(50, 50), 'p', 50, 5, true);
        defaultMockPlayer = mock(Player.class);
        defaultAi = new Ai(defaultMob, defaultMockPlayer);

    }
    @Test
    void mobIsStoredAndFetchedCorrectly() {
        assertEquals(defaultMob, defaultAi.getMob());
    }

    @Test
    void playerIsStoredAndFetchedCorrectly(){
        assertEquals(defaultMockPlayer, defaultAi.getPlayer());
    }

    @Test
    void constructorSetsStateToIdle(){
        assertEquals(MobState.IDLE, defaultAi.getState());
    }

    @Test
    void canSwitchFromIdleToPatrollingAfterSufficientFrames(){
        for(int i=0; i < 3; i++){
            defaultAi.update();
        }
        assertEquals(MobState.PATROLLING, defaultAi.getState());
    }

    @Test
    void canSwitchFromPatrollingToIdle(){
        for(int i=0; i < 3; i++){
            defaultAi.update();
        }
        Position customDestination = new Position(54, 48);
        defaultAi.setDestination(customDestination);
        defaultAi.update();
        assertEquals(MobState.IDLE, defaultAi.getState());
    }

    @Test
    void patrollingSetsDestinationNotCurrentPosition(){
        Position currentPosition = defaultAi.getMob().getCurrentPosition();
        for(int i=0; i < 3; i++){
            defaultAi.update();
        }
        assertNotEquals(currentPosition, defaultAi.getDestination());
    }

    @Test
    void patrollingMovesMob(){
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
    void onlyMovesMobTheirMovementSpeedPerFrame(){
        for(int i=0; i < 3; i++){
            defaultAi.update();
        }
        Position customDestination = new Position(100, 50 );
        Position expectedMovementInOneFrame = new Position(50 + defaultAi.getMob().getMovementSpeed(),50);
        defaultAi.setDestination(customDestination);
        defaultAi.update();
        assertEquals(expectedMovementInOneFrame, defaultAi.getMob().getCurrentPosition());
    }

    void setUpAttackReceptionScenario(){
        when(defaultMockPlayer.getX()).thenReturn(57);
        when(defaultMockPlayer.getY()).thenReturn(50);
        defaultAi.getMob().receiveAttack(5);
        defaultAi.update();
    }

    @Test
    void receivingAttackBeginsCombat(){
        setUpAttackReceptionScenario();
        assertEquals(MobState.COMBAT, defaultAi.getState());
    }

    @Test
    void beginningCombatPutsMobInPlayersAggroList(){
        setUpAttackReceptionScenario();
        verify(defaultMockPlayer).engageMob(defaultAi.getMob());
    }

    @Test
    void canChasePlayer(){
        setUpAttackReceptionScenario();
        Position expectedPosition = new Position(50 + defaultAi.getMob().getMovementSpeed(),50);
        assertEquals(expectedPosition, defaultAi.getMob().getCurrentPosition());
    }

    @Test
    void stopsChasingWhenNextToPlayer(){
        setUpAttackReceptionScenario();
        defaultAi.update();
        Position expectedPosition = new Position(56,50);
        assertEquals(expectedPosition, defaultAi.getMob().getCurrentPosition());
    }

    @Test
    void attacksWhenNextToPlayer(){
        when(defaultMockPlayer.getX()).thenReturn(51);
        when(defaultMockPlayer.getY()).thenReturn(51);
        Ai spyAi = spy(defaultAi);
        spyAi.getMob().receiveAttack(8);
        spyAi.update();
        verify(spyAi).attack();
    }

    void setUpResetScenario(){
        when(defaultMockPlayer.getX()).thenReturn(40);
        when(defaultMockPlayer.getY()).thenReturn(55);
        defaultAi.getMob().receiveAttack(13);
        defaultAi.update();
        defaultAi.update();
        when(defaultMockPlayer.getY()).thenReturn(125);
        defaultAi.update();
    }

    @Test
    void entersResetStateWhenPlayerOutOfCombatRange(){
        setUpResetScenario();
        assertEquals(MobState.RESET, defaultAi.getState());
    }

    @Test
    void resetMakesMobInvulnerable(){
        setUpResetScenario();
        int healthBeforeAttemptedAttack = defaultAi.getMob().getCurrentHealth();
        defaultAi.getMob().receiveAttack(20);
        int healthAfterAttemptedAttack = defaultAi.getMob().getCurrentHealth();
        assertEquals(healthBeforeAttemptedAttack, healthAfterAttemptedAttack);
    }

    @Test
    void resetMakesMobHealthFull(){
        setUpResetScenario();
        int expected = defaultAi.getMob().getMaximumHealth();
        int actual = defaultAi.getMob().getCurrentHealth();
        assertEquals(expected, actual);
    }

    @Test
    void resetRemovesMobFromPlayerAggroList(){
        setUpResetScenario();
        verify(defaultMockPlayer).disengageMob(defaultAi.getMob());
    }

    @Test
    void resetMakesMobReturnToSpawnPoint(){
        setUpResetScenario();
        defaultAi.update();
        defaultAi.update();
        Position expectedPosition = defaultAi.getMob().getSpawnPoint();
        Position actualPosition = defaultAi.getMob().getCurrentPosition();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    void resetBeginsIdleUponReturnToSpawnPoint(){
        setUpResetScenario();
        defaultAi.update();
        defaultAi.update();
        assertEquals(MobState.IDLE, defaultAi.getState());
    }



}
