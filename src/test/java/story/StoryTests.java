package story;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import race.Race;
import spelare.Player;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoryTests {
    Story.LostAndFoundQuest questLostAndFound;
    Story.DungeonQuest questDungeonBeat;
    Story.TimedQuest questTimedForest;
    Story.NPC npcXerxes;
    Story.NPC npcCyrus;
    Story.QuestItem doransRing;
    Player player;
    Race mockRace;

    Story.DungeonDoor mockDoor;
    Story.Enemy mockEnemy;
    Story.Boss mockBoss;

    @BeforeEach
    public void initialize() {
        mockRace = mock(Race.class);
        when(mockRace.getLifeModifier()).thenReturn(20);
        when(mockRace.getMovementModifier()).thenReturn(2);
        when(mockRace.getAttackPowerModifier()).thenReturn(5);

        player = new Player(mockRace);

        questDungeonBeat = new Story.DungeonQuest("DungeonBeat", "Beat the Dungeon", "Clear dungeon and defeat boss", 3);
        questLostAndFound = new Story.LostAndFoundQuest("LostAndFound", "Find the Lost Artifact", "Search for artifact", "DoransRing");

        doransRing = new Story.QuestItem("DoransRing", "A magical ring with ancient power");

        npcXerxes = new Story.NPC("Xerxes", questDungeonBeat);
        npcCyrus = new Story.NPC("Cyrus", questLostAndFound);

        mockDoor = mock(Story.DungeonDoor.class);
        mockEnemy = mock(Story.Enemy.class);
        mockBoss = mock(Story.Boss.class);
    }

    @Test
    public void quest_startsInNotStartedState() {
        assertEquals(Story.QuestState.NOT_STARTED, questDungeonBeat.getState());
    }

    @Test
    public void quest_canBeStarted() {
        questDungeonBeat.startQuest();
        assertEquals(Story.QuestState.ACTIVE, questDungeonBeat.getState());
    }

    @Test
    public void dungeonDoor_unlocksWithCorrectKeyhole() {
        questDungeonBeat.startQuest();

        assertTrue(questDungeonBeat.unlockDungeonDoor(mockDoor, true));
        verify(mockDoor, times(1)).unlock();
        assertTrue(questDungeonBeat.isDoorUnlocked());
    }

    @Test
    public void dungeonDoor_wipesWhenWrongKeyhole() {
        questDungeonBeat.startQuest();

        assertFalse(questDungeonBeat.unlockDungeonDoor(mockDoor, false));
        verify(mockDoor, never()).unlock();
        assertFalse(questDungeonBeat.isDoorUnlocked());
    }

    @Test
    public void dungeonQuest_tracksCombatDamageInFightLog() {
        when(mockEnemy.getDamage()).thenReturn(10);
        when(mockEnemy.getName()).thenReturn("Goblin");
        when(mockEnemy.getHealth()).thenReturn(50);
        when(mockEnemy.isDead()).thenReturn(true);

        questDungeonBeat.startQuest();
        questDungeonBeat.unlockDungeonDoor(mockDoor, true);

        Story.FightLog fightLog = questDungeonBeat.getFightLog();

        questDungeonBeat.fightEnemy(mockEnemy, player);
        questDungeonBeat.fightEnemy(mockEnemy, player);
        questDungeonBeat.fightEnemy(mockEnemy, player);

        assertEquals(3, fightLog.getLogSize());
        assertTrue(fightLog.getLogEntries().get(0).contains("-10HP from Goblin"));
        assertTrue(fightLog.getLogEntries().get(1).contains("-10HP from Goblin"));
        assertTrue(fightLog.getLogEntries().get(2).contains("-10HP from Goblin"));
    }

    @Test
    public void dungeonQuest_tracksEnemyKills() {
        when(mockEnemy.getDamage()).thenReturn(10);
        when(mockEnemy.getName()).thenReturn("Goblin");
        when(mockEnemy.getHealth()).thenReturn(50);
        when(mockEnemy.isDead()).thenReturn(true);

        questDungeonBeat.startQuest();
        questDungeonBeat.unlockDungeonDoor(mockDoor, true);

        questDungeonBeat.fightEnemy(mockEnemy, player);
        questDungeonBeat.fightEnemy(mockEnemy, player);

        assertEquals(2, questDungeonBeat.getEnemiesKilled());
    }

    @Test
    public void boundaryTest_zeroDamageEnemyStillLogsAttack() {
        Story.Enemy harmlessEnemy = mock(Story.Enemy.class);
        when(harmlessEnemy.getDamage()).thenReturn(0);
        when(harmlessEnemy.getName()).thenReturn("Bunny");
        when(harmlessEnemy.getHealth()).thenReturn(1);
        when(harmlessEnemy.isDead()).thenReturn(true);

        questDungeonBeat.startQuest();
        questDungeonBeat.unlockDungeonDoor(mockDoor, true);

        questDungeonBeat.fightEnemy(harmlessEnemy, player);

        assertEquals(1, questDungeonBeat.getEnemiesKilled());
        assertTrue(questDungeonBeat.getFightLog().getLogEntries().get(0).contains("-0HP from Bunny"));
    }

    @Test
    public void dungeonQuest_canStartBossFightAfterClearingEnemies() {
        when(mockEnemy.getDamage()).thenReturn(10);
        when(mockEnemy.getName()).thenReturn("Goblin");
        when(mockEnemy.getHealth()).thenReturn(50);
        when(mockEnemy.isDead()).thenReturn(true);

        questDungeonBeat.startQuest();
        questDungeonBeat.unlockDungeonDoor(mockDoor, true);

        questDungeonBeat.fightEnemy(mockEnemy, player);
        questDungeonBeat.fightEnemy(mockEnemy, player);
        questDungeonBeat.fightEnemy(mockEnemy, player);

        assertTrue(questDungeonBeat.startBossFight());
    }

    @Test
    public void dungeonQuest_completesWhenBossDefeated() {
        when(mockEnemy.getDamage()).thenReturn(10);
        when(mockEnemy.getName()).thenReturn("Goblin");
        when(mockEnemy.getHealth()).thenReturn(50);
        when(mockEnemy.isDead()).thenReturn(true);

        when(mockBoss.getDamage()).thenReturn(20);
        when(mockBoss.getName()).thenReturn("Dragon");
        when(mockBoss.getHealth()).thenReturn(200);
        when(mockBoss.isDead()).thenReturn(true);

        questDungeonBeat.startQuest();
        questDungeonBeat.unlockDungeonDoor(mockDoor, true);

        questDungeonBeat.fightEnemy(mockEnemy, player);
        questDungeonBeat.fightEnemy(mockEnemy, player);
        questDungeonBeat.fightEnemy(mockEnemy, player);

        questDungeonBeat.fightBoss(mockBoss, player);

        verify(mockBoss, times(1)).takeDamage(200);
        assertEquals(Story.QuestState.COMPLETED, questDungeonBeat.getState());
    }

    @Test
    public void dungeonQuest_playerDiesFromEnemy_triggersWipe() {
        when(mockEnemy.getDamage()).thenReturn(10);
        when(mockEnemy.getName()).thenReturn("Goblin");
        when(mockEnemy.getHealth()).thenReturn(50);
        when(mockEnemy.isDead()).thenReturn(true);

        questDungeonBeat.startQuest();
        questDungeonBeat.unlockDungeonDoor(mockDoor, true);
        questDungeonBeat.fightEnemy(mockEnemy, player);

        questDungeonBeat.playerDiesFromEnemy(player);

        assertEquals(0, player.getCurrentLife());
        assertEquals(0, questDungeonBeat.getEnemiesKilled());
        assertFalse(questDungeonBeat.isDoorUnlocked());
        assertEquals(0, questDungeonBeat.getFightLog().getLogSize());
    }

    @Test
    public void dungeonQuest_playerDiesFromBoss_triggersWipe() {
        when(mockEnemy.getDamage()).thenReturn(10);
        when(mockEnemy.getName()).thenReturn("Goblin");
        when(mockEnemy.getHealth()).thenReturn(50);
        when(mockEnemy.isDead()).thenReturn(true);

        questDungeonBeat.startQuest();
        questDungeonBeat.unlockDungeonDoor(mockDoor, true);

        for (int i = 0; i < 3; i++) {
            questDungeonBeat.fightEnemy(mockEnemy, player);
        }

        questDungeonBeat.playerDiesFromBoss(player);

        assertEquals(0, player.getCurrentLife());
        assertFalse(questDungeonBeat.isDoorUnlocked());
    }

    @Test
    public void timedQuest_completesWhenAllRequirementsMet() {
        questTimedForest = new Story.TimedQuest("ForestClear", "Clear the Forest",
                "Kill 3 enemies within 60s without losing more than 40HP", 3, 40, 60);

        Story.Enemy enemy1 = mock(Story.Enemy.class);
        Story.Enemy enemy2 = mock(Story.Enemy.class);
        Story.Enemy enemy3 = mock(Story.Enemy.class);

        when(enemy1.getDamage()).thenReturn(10);
        when(enemy1.getName()).thenReturn("Wolf");
        when(enemy1.getHealth()).thenReturn(30);
        when(enemy1.isDead()).thenReturn(true);

        when(enemy2.getDamage()).thenReturn(12);
        when(enemy2.getName()).thenReturn("Bear");
        when(enemy2.getHealth()).thenReturn(40);
        when(enemy2.isDead()).thenReturn(true);

        when(enemy3.getDamage()).thenReturn(8);
        when(enemy3.getName()).thenReturn("Boar");
        when(enemy3.getHealth()).thenReturn(25);
        when(enemy3.isDead()).thenReturn(true);

        questTimedForest.startQuest();

        questTimedForest.enemyAttacksPlayer(enemy1, player);
        questTimedForest.playerKillsEnemy(enemy1);

        questTimedForest.enemyAttacksPlayer(enemy2, player);
        questTimedForest.playerKillsEnemy(enemy2);

        questTimedForest.enemyAttacksPlayer(enemy3, player);
        questTimedForest.playerKillsEnemy(enemy3);

        assertEquals(Story.QuestState.COMPLETED, questTimedForest.getState());
        assertEquals(3, questTimedForest.getEnemiesKilled());
        assertEquals(30, questTimedForest.getHpLost());

        Story.FightLog log = questTimedForest.getFightLog();
        assertEquals(3, log.getLogSize());
        assertTrue(log.getLogEntries().get(0).contains("-10HP from Wolf"));
        assertTrue(log.getLogEntries().get(1).contains("-12HP from Bear"));
        assertTrue(log.getLogEntries().get(2).contains("-8HP from Boar"));
    }

    @Test
    public void timedQuest_failsWhenTooMuchHpLost() {
        questTimedForest = new Story.TimedQuest("ForestClear", "Clear the Forest",
                "Kill 3 enemies within 60s without losing more than 40HP", 3, 40, 60);

        Story.Enemy strongEnemy = mock(Story.Enemy.class);
        when(strongEnemy.getDamage()).thenReturn(50);
        when(strongEnemy.getName()).thenReturn("Ogre");

        questTimedForest.startQuest();
        questTimedForest.enemyAttacksPlayer(strongEnemy, player);

        assertEquals(Story.QuestState.FAILED, questTimedForest.getState());
        assertEquals(50, questTimedForest.getHpLost());
    }

    @Test
    public void timedQuest_failsWhenTimeExpires() {
        questTimedForest = new Story.TimedQuest("ForestClear", "Clear the Forest",
                "Kill 3 enemies within 60s without losing more than 40HP", 3, 40, 60);

        Story.Enemy enemy = mock(Story.Enemy.class);
        when(enemy.getDamage()).thenReturn(10);
        when(enemy.getName()).thenReturn("Wolf");
        when(enemy.getHealth()).thenReturn(30);
        when(enemy.isDead()).thenReturn(true);

        questTimedForest.startQuest();
        questTimedForest.enemyAttacksPlayer(enemy, player);
        questTimedForest.playerKillsEnemy(enemy);

        questTimedForest.simulateTimeElapsed(61000);
        questTimedForest.enemyAttacksPlayer(enemy, player);

        assertEquals(Story.QuestState.FAILED, questTimedForest.getState());
        assertTrue(questTimedForest.isTimeExpired());
    }

    @ParameterizedTest(name = "Damage={0} → HP Loss={1} → Expected={2}")
    @CsvSource({
            "10, 30, COMPLETED",
            "15, 45, FAILED",
            "20, 60, FAILED"
    })
    public void parameterizedTest_hpLossAffectsTimedQuestOutcome(int damagePerEnemy, int totalDamage, String expectedState) {
        questTimedForest = new Story.TimedQuest("ForestClear", "Clear Forest",
                "Test quest", 3, 40, 60);

        Story.Enemy testEnemy = mock(Story.Enemy.class);
        when(testEnemy.getDamage()).thenReturn(damagePerEnemy);
        when(testEnemy.getName()).thenReturn("TestEnemy");
        when(testEnemy.getHealth()).thenReturn(50);
        when(testEnemy.isDead()).thenReturn(true);

        questTimedForest.startQuest();

        for (int i = 0; i < 3; i++) {
            questTimedForest.enemyAttacksPlayer(testEnemy, player);
            if (questTimedForest.getState() == Story.QuestState.ACTIVE) {
                questTimedForest.playerKillsEnemy(testEnemy);
            }
        }

        assertEquals(Story.QuestState.valueOf(expectedState), questTimedForest.getState());
    }

    @Test
    public void lostAndFound_completesWithItem() {
        player.addQuestItem("DoransRing", doransRing);
        questLostAndFound.startQuest();

        assertTrue(questLostAndFound.completeWithItem(player));
        assertEquals(Story.QuestState.COMPLETED, questLostAndFound.getState());
    }

    @Test
    public void lostAndFound_cannotCompleteWithoutItem() {
        questLostAndFound.startQuest();

        assertFalse(questLostAndFound.completeWithItem(player));
        assertNotEquals(Story.QuestState.COMPLETED, questLostAndFound.getState());
    }

    @Test
    public void npc_givesQuestToPlayer() {
        npcXerxes.giveQuestToPlayer(player);

        assertEquals(1, player.getQuestCount());
        assertEquals(Story.QuestState.ACTIVE, questDungeonBeat.getState());
    }

    @Test
    public void npc_rewardsPlayerOnCompletion() {
        npcXerxes.giveQuestToPlayer(player);
        questDungeonBeat.completeQuest();

        int lifeBefore = player.getMaxLife();
        npcXerxes.rewardPlayer(player, 50);

        assertEquals(lifeBefore + 50, player.getMaxLife());
    }
}