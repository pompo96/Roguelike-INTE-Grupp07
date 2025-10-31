package ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.HashSet;
import java.util.List;
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
        Ai.removeAllMobs();
        defaultMob = new PlaceholderMob(new Position(50, 50), 'p', 50, 5, true);
        defaultMockPlayer = mock(Player.class);
        defaultAi = new Ai(defaultMob, defaultMockPlayer);
    }

    void initializeNeutralAi(){
        neutralMob = new PlaceholderMob(new Position(150, 150), 'p', 50, 5, false);
        neutralAi = new Ai(neutralMob, defaultMockPlayer);
    }

    void updateUntilIdle(){
        while(defaultAi.getState() != MobState.IDLE)
            defaultAi.update();

    }
    void updateUntilPatrolling(){
        for(int i = 0; i < 3; i++){
            defaultAi.update();
        }
    }

    void setMockPosition_inCombatRadius_outOfAggroRadius(){
        when(defaultMockPlayer.getX()).thenReturn(75);
        when(defaultMockPlayer.getY()).thenReturn(45);
    }
    void setMockPosition_inAggroRadius(){
        when(defaultMockPlayer.getX()).thenReturn(46);
        when(defaultMockPlayer.getY()).thenReturn(53);
    }
    void setMockPosition_outOfCombatRadius(){
        when(defaultMockPlayer.getX()).thenReturn(120);
        when(defaultMockPlayer.getY()).thenReturn(13);
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
    void neutralAndDefaultAiStoredInStaticList(){
        initializeNeutralAi();
        List<Ai> aiList = Ai.getAllMobs();
        assertTrue(aiList.contains(defaultAi) && aiList.contains(neutralAi));
    }

    @Test
    void staticRemove_RemovesAiFromStaticList(){
        Ai.remove(defaultAi);
        assertFalse(Ai.getAllMobs().contains(defaultAi));
    }

    @Test
    void staticRemoveAllMobs_RemovesAllMobs(){
        initializeNeutralAi();
        Ai.removeAllMobs();
        assertTrue(Ai.getAllMobs().isEmpty());
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
        updateUntilPatrolling();
        Position customDestination = new Position(54, 48);
        defaultAi.setDestination(customDestination);
        updateUntilIdle();
        assertEquals(MobState.IDLE, defaultAi.getState());
    }

    @Test
    void patrollingSetsDestinationNotCurrentPosition(){
        Position currentPosition = defaultAi.getMob().getCurrentPosition();
        updateUntilPatrolling();
        assertNotEquals(currentPosition, defaultAi.getDestination());
    }

    @Test
    void patrollingMovesMob(){
        updateUntilPatrolling();
        Position customDestination = new Position(53, 53);
        defaultAi.setDestination(customDestination);
        defaultAi.update();
        Position afterPatrol = defaultAi.getMob().getCurrentPosition();
        assertEquals(customDestination, afterPatrol);
    }

    @Test
    void onlyMovesMobTheirMovementSpeedPerFrame(){
        updateUntilPatrolling();
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
        setMockPosition_inAggroRadius();
        defaultAi.getMob().receiveAttack(13);
        defaultAi.update();
        defaultAi.update();
        setMockPosition_outOfCombatRadius();
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
        updateUntilIdle();
        assertEquals(MobState.IDLE, defaultAi.getState());
    }

    @Test
    void hostileMobEntersCombatWithPlayerInsideItsAggroRange(){
        setMockPosition_inAggroRadius();
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
    void hostileMobDoesNotEnterCombatWithPlayerOutsideItsAggroRange(){
        setMockPosition_inCombatRadius_outOfAggroRadius();
        defaultAi.update();
        assertEquals(MobState.IDLE, defaultAi.getState());
    }


    void setUpHostileReactionToSocialAggroScenario(boolean hostile){
        PlaceholderMob secondaryMob = new PlaceholderMob(new Position (45, 43), 'p', 100, 3, hostile);
        Ai secondaryAi = new Ai(secondaryMob, defaultMockPlayer);
        setMockPosition_inCombatRadius_outOfAggroRadius();
        Set<PlaceholderMob> mockSet = new HashSet<>();
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
        Set<PlaceholderMob> mockSet = new HashSet<>();
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

    @Test
    void damagingMobToZeroHealthBeginsDeadState(){
        defaultAi.getMob().receiveAttack(100);
        defaultAi.update();
        assertEquals(MobState.DEAD, defaultAi.getState());
    }

    @Test
    void deadMobDespawnsAfterSufficientFrames(){
        defaultAi.getMob().receiveAttack(100);
        for (int i = 0; i <= 5; i++){
            defaultAi.update();
        }
        assertFalse(Ai.getAllMobs().contains(defaultAi));
    }

    @Test
    void a1_oneshotFromIdle(){
        boolean correctPath = true;
        updateUntilPatrolling();
        if(defaultAi.getState() != MobState.PATROLLING)
            correctPath = false;
        updateUntilIdle();
        if(defaultAi.getState() != MobState.IDLE)
            correctPath = false;
        defaultAi.getMob().receiveAttack(100);
        defaultAi.update();
        if(defaultAi.getState() != MobState.DEAD)
            correctPath = false;
        assertTrue(correctPath);
    }

    @Test
    void a2_oneshotFromPatrollingAfterReset(){
        boolean correctPath = true;
        setMockPosition_inAggroRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.COMBAT)
            correctPath = false;
        setMockPosition_outOfCombatRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.RESET)
            correctPath = false;
        updateUntilIdle();
        if(defaultAi.getState() != MobState.IDLE)
            correctPath = false;
        defaultAi.getMob().receiveAttack(150);
        defaultAi.update();
        if(defaultAi.getState() != MobState.DEAD)
            correctPath = false;
        assertTrue(correctPath);
    }

    @Test
    void d1_oneshotFromPatrolling(){
        boolean correctPath = true;
        updateUntilPatrolling();
        if(defaultAi.getState() != MobState.PATROLLING)
            correctPath = false;
        defaultAi.getMob().receiveAttack(113);
        defaultAi.update();
        if(defaultAi.getState() != MobState.DEAD)
            correctPath = false;
        assertTrue(correctPath);
    }

    @Test
    void k1_afterReset_aggrozoneTriggersCombatFromIdleThenDies(){
        boolean correctPath = true;
        setMockPosition_inAggroRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.COMBAT)
            correctPath = false;
        setMockPosition_outOfCombatRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.RESET)
            correctPath = false;
        updateUntilIdle();
        if(defaultAi.getState() != MobState.IDLE)
            correctPath = false;
        setMockPosition_inAggroRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.COMBAT)
            correctPath = false;
        defaultAi.getMob().receiveAttack(546);
        defaultAi.update();
        if(defaultAi.getState() != MobState.DEAD)
            correctPath = false;
        assertTrue(correctPath);
    }

    @Test
    void k2_afterReset_receivingAttackTriggersCombatFromIdleThenDies(){
        boolean correctPath = true;
        setMockPosition_inCombatRadius_outOfAggroRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.IDLE)
            correctPath = false;
        defaultAi.getMob().receiveAttack(25);
        defaultAi.update();
        if(defaultAi.getState() != MobState.COMBAT)
            correctPath = false;
        setMockPosition_outOfCombatRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.RESET)
            correctPath = false;
        updateUntilIdle();
        if(defaultAi.getState() != MobState.IDLE)
            correctPath = false;
        setMockPosition_inCombatRadius_outOfAggroRadius();
        defaultAi.getMob().receiveAttack(20);
        defaultAi.update();
        if(defaultAi.getState() != MobState.COMBAT)
            correctPath = false;
        defaultAi.getMob().receiveAttack(120);
        defaultAi.update();
        if(defaultAi.getState() != MobState.DEAD)
            correctPath = false;
        assertTrue(correctPath);
    }

    @Test
    void k3_afterReset_receivesAttackTriggeringCombatFromPatrollingThenDies(){
        boolean correctPath = true;
        updateUntilPatrolling();
        if(defaultAi.getState() != MobState.PATROLLING)
            correctPath = false;
        setMockPosition_inCombatRadius_outOfAggroRadius();
        defaultAi.getMob().receiveAttack(10);
        defaultAi.update();
        if(defaultAi.getState() != MobState.COMBAT)
            correctPath = false;
        setMockPosition_outOfCombatRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.RESET)
            correctPath = false;
        updateUntilIdle();
        if(defaultAi.getState() != MobState.IDLE)
            correctPath = false;
        updateUntilPatrolling();
        if(defaultAi.getState() != MobState.PATROLLING)
            correctPath = false;
        setMockPosition_inCombatRadius_outOfAggroRadius();
        defaultAi.getMob().receiveAttack(20);
        defaultAi.update();
        if(defaultAi.getState() != MobState.COMBAT)
            correctPath = false;
        defaultAi.getMob().receiveAttack(160);
        defaultAi.update();
        if(defaultAi.getState() != MobState.DEAD)
            correctPath = false;
        assertTrue(correctPath);
    }

    @Test
    void k4_afterMultipleResets_aggrozoneTriggersCombatFromPatrollingThenDies(){
        boolean correctPath = true;
        Position spawn = defaultAi.getMob().getSpawnPoint();
        updateUntilPatrolling();
        if(defaultAi.getState() != MobState.PATROLLING)
            correctPath = false;
        defaultAi.setDestination(spawn);
        updateUntilIdle();
        if(defaultAi.getState() != MobState.IDLE)
            correctPath = false;
        setMockPosition_inCombatRadius_outOfAggroRadius();
        defaultAi.getMob().receiveAttack(5);
        defaultAi.update();
        if(defaultAi.getState() != MobState.COMBAT)
            correctPath = false;
        setMockPosition_outOfCombatRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.RESET)
            correctPath = false;
        updateUntilIdle();
        if(defaultAi.getState() != MobState.IDLE)
            correctPath = false;
        updateUntilPatrolling();
        if(defaultAi.getState() != MobState.PATROLLING)
            correctPath = false;
        defaultAi.setDestination(spawn);
        updateUntilIdle();
        setMockPosition_inAggroRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.COMBAT)
            correctPath = false;
        setMockPosition_outOfCombatRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.RESET)
            correctPath = false;
        updateUntilIdle();
        if(defaultAi.getState() != MobState.IDLE)
            correctPath = false;
        updateUntilPatrolling();
        if(defaultAi.getState() != MobState.PATROLLING)
            correctPath = false;
        setMockPosition_inAggroRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.COMBAT)
            correctPath = false;
        setMockPosition_outOfCombatRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.RESET)
            correctPath = false;
        updateUntilIdle();
        if(defaultAi.getState() != MobState.IDLE)
            correctPath = false;
        updateUntilPatrolling();
        if(defaultAi.getState() != MobState.PATROLLING)
            correctPath = false;
        setMockPosition_inAggroRadius();
        defaultAi.update();
        if(defaultAi.getState() != MobState.COMBAT)
            correctPath = false;
        defaultAi.getMob().receiveAttack(143);
        defaultAi.update();
        if(defaultAi.getState() != MobState.DEAD)
            correctPath = false;
        assertTrue(correctPath);
    }
}
