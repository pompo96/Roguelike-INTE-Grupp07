package ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AiTest {

    PlaceholderMob defaultMob;
    Ai defaultAi;
    Player defaultMockPlayer;
    PlaceholderMob neutralMob;
    Ai neutralAi;

    @BeforeEach
    void initializeDefaultAi(){
        defaultMob = new PlaceholderMob(new Position(50, 50), 'p', 50, 5, true);
        defaultMockPlayer = mock(Player.class);
        defaultAi = new Ai(defaultMob, defaultMockPlayer);
    }

    void initializeNeutralAi(){
        neutralMob = new PlaceholderMob(new Position(150, 150), 'p', 50, 5, false);
        neutralAi = new Ai(neutralMob, defaultMockPlayer);
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
        Position afterPatrol = defaultAi.getMob().getCurrentPosition();
        assertEquals(customDestination, afterPatrol);

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

    @Test
    void hostileMobEntersCombatWithPlayerInsideItsAggroRange(){
        when(defaultMockPlayer.getX()).thenReturn(53);
        when(defaultMockPlayer.getY()).thenReturn(44);
        defaultAi.update();
        assertEquals(MobState.COMBAT, defaultAi.getState());
    }

    @Test
    void neutralMobDoesNotEnterCombatWithPlayerInsideItsAggroRange(){
        initializeNeutralAi();
        when(defaultMockPlayer.getX()).thenReturn(145);
        when(defaultMockPlayer.getY()).thenReturn(149);
        neutralAi.update();
        assertEquals(MobState.IDLE, neutralAi.getState());
    }
    @Test
    void hostileMobDoesntEnterCombatWithPlayerOutsideItsAggroRange(){
        when(defaultMockPlayer.getX()).thenReturn(defaultAi.getMob().getX());
        when(defaultMockPlayer.getY()).thenReturn(defaultAi.getMob().getY() + defaultAi.getMob().getAggroRadius() + 1);
        defaultAi.update();
        assertEquals(MobState.IDLE, defaultAi.getState());
    }


    void setUpHostileReactionToSocialAggroScenario(boolean hostile){
        PlaceholderMob secondaryMob = new PlaceholderMob(new Position (45, 43), 'p', 100, 3, hostile);
        Ai secondaryAi = new Ai(secondaryMob, defaultMockPlayer);
        when(defaultMockPlayer.getX()).thenReturn(65);
        when(defaultMockPlayer.getY()).thenReturn(32);
        Set<PlaceholderMob> mockSet = new HashSet<PlaceholderMob>();
        mockSet.add(secondaryMob);
        when(defaultMockPlayer.getEngagedMobs()).thenReturn(mockSet);
        secondaryMob.receiveAttack(20);
        secondaryAi.update();
        defaultAi.update();
    }



    @Test
    void hostileMobAffectedBySocialAggroFromHostileMob(){
        setUpHostileReactionToSocialAggroScenario(true);
        assertEquals(MobState.COMBAT, defaultAi.getState());
    }

    @Test
    void hostileMobNotAffectedBySocialAggroFromNeutralMob(){
        setUpHostileReactionToSocialAggroScenario(false);
        assertEquals(MobState.IDLE, defaultAi.getState());
    }

    void setUpNeutralReactionToSocialAggroScenario(boolean hostile){
        initializeNeutralAi();
        PlaceholderMob secondaryMob = new PlaceholderMob(new Position (148, 156), 'p', 100, 3, hostile);
        Ai secondaryAi = new Ai(secondaryMob, defaultMockPlayer);
        when(defaultMockPlayer.getX()).thenReturn(135);
        when(defaultMockPlayer.getY()).thenReturn(163);
        Set<PlaceholderMob> mockSet = new HashSet<PlaceholderMob>();
        mockSet.add(secondaryMob);
        when(defaultMockPlayer.getEngagedMobs()).thenReturn(mockSet);
        secondaryAi.update();
        neutralAi.update();
    }

    @Test
    void neutralMobDoesNotReactToSocialAggroFromHostileMob(){
        setUpNeutralReactionToSocialAggroScenario(true);
        assertEquals(MobState.IDLE, neutralAi.getState());
    }

    @Test
    void neutralMobDoesNotReactToSocialAggroFromNeutralMob(){
        setUpNeutralReactionToSocialAggroScenario(false);
        assertEquals(MobState.IDLE, neutralAi.getState());
    }


}
