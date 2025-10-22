package story;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import race.Race;
import spelare.Player;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoryTests {
    Story.Quest questDungeonBeat;
    Story.Quest questLostAndFound;
    Story.NPC npcXerxes;
    Story.NPC npcCyrus;
    Story.QuestItem doransRing;
    Player player;
    Race mockRace;

    Story.Area mockArea1;
    Story.Area mockArea2;

    @BeforeEach
    public void initialize() {
        mockRace = mock(Race.class);
        when(mockRace.getLifeModifier()).thenReturn(20);
        when(mockRace.getMovementModifier()).thenReturn(2);
        when(mockRace.getAttackPowerModifier()).thenReturn(5);

        questDungeonBeat = new Story.Quest("DungeonBeat", "Beat the Dungeon", "Defeat all enemies in the dungeon");
        questLostAndFound = new Story.Quest("LostAndFound", "Find the Lost Artifact", "Search for the artifact in the forest");


        doransRing = new Story.QuestItem("DoransRing", "A magical ring with ancient power");


        npcXerxes = new Story.NPC("Xerxes", questDungeonBeat, null);
        npcCyrus = new Story.NPC("Cyrus", questLostAndFound, "DoransRing");

        player = new Player(mockRace);

        mockArea1 = mock(Story.Area.class);
        mockArea2 = mock(Story.Area.class);
    }


    @Test
    public void quest_hasCorrectID() {
        assertEquals("DungeonBeat", questDungeonBeat.getID(), "Quest borde ha rätt ID");
    }


    @Test
    public void quest_startsInNotStartedState() {
        assertEquals(Story.QuestState.NOT_STARTED, questDungeonBeat.getState(), "Quest borde starta i NOT_STARTED state");
    }

    @Test
    public void quest_canBeStarted() {
        questDungeonBeat.startQuest();
        assertEquals(Story.QuestState.ACTIVE, questDungeonBeat.getState(), "Quest borde vara ACTIVE efter start");
    }


    @Test
    public void quest_canBeCompleted() {
        questDungeonBeat.startQuest();
        questDungeonBeat.completeQuest();
        assertEquals(Story.QuestState.COMPLETED, questDungeonBeat.getState(), "Quest borde vara COMPLETED");
    }

    @Test
    public void quest_cannotBeCompletedWithoutStarting() {
        questDungeonBeat.completeQuest();
        assertEquals(Story.QuestState.NOT_STARTED, questDungeonBeat.getState(), "Quest borde fortfarande vara NOT_STARTED");
    }

    @Test
    public void quest_canBeFailed() {
        questDungeonBeat.startQuest();
        questDungeonBeat.failQuest();
        assertEquals(Story.QuestState.FAILED, questDungeonBeat.getState(), "Quest borde vara FAILED");
    }


    @Test
    public void questItem_hasCorrectName() {
        assertEquals("DoransRing", doransRing.getItemName(), "QuestItem borde ha rätt namn");
    }

    @Test
    public void questItem_hasDescription() {
        assertEquals("A magical ring with ancient power", doransRing.getItemDescription(), "QuestItem borde ha beskrivning");
    }



    @Test
    public void npc_hasQuest() {
        assertNotNull(npcXerxes.getQuest(), "NPC borde ha en quest");
    }

    @Test
    public void npc_questIsCorrect() {
        assertEquals("DungeonBeat", npcXerxes.getQuest().getID(), "NPC borde ha rätt quest");
    }


    @Test
    public void npc_canGiveQuestToPlayer() {
        npcXerxes.giveQuestToPlayer(player);
        assertEquals(1, player.getQuestCount(), "Player borde ha 1 quest");
    }

    @Test
    public void npc_questAppearsInPlayerQuestLog() {
        npcXerxes.giveQuestToPlayer(player);
        Player.Quest foundQuest = player.getQuest("DungeonBeat");
        assertEquals("Beat the Dungeon", foundQuest.getName(), "Quest borde finnas i player questlog");
    }



    @Test
    public void player_canAbandonQuestFromNPC() {
        npcXerxes.giveQuestToPlayer(player);
        Player.Quest quest = player.getQuest("DungeonBeat");
        player.abandonQuest(quest);
        assertEquals(0, player.getQuestCount(), "Player borde ha 0 quests efter abandon");
    }


    @Test
    public void player_doesNotHaveItemByDefault() {
        assertFalse(player.hasItem("DoransRing"), "Player borde inte ha Dorans Ring från början");
    }


    @Test
    public void npc_completeQuestWhenPlayerHasItem() {
        player.addQuestItem("DoransRing", doransRing);
        npcCyrus.giveQuestToPlayer(player);

        assertTrue(npcCyrus.completeQuestIfPlayerHasItem(player), "NPC borde returnera true om player har ringen");
        assertTrue(player.isQuestComplete("LostAndFound"), "Quest borde vara markerad som slutförd");
    }

    @Test
    public void player_cannotCompleteQuestWithoutItem() {
        npcCyrus.giveQuestToPlayer(player);

        assertFalse(npcCyrus.completeQuestIfPlayerHasItem(player), "NPC borde inte acceptera quest utan ringen");
        assertFalse(player.isQuestComplete("LostAndFound"), "Quest borde inte vara slutförd utan ringen");
    }


}