package story;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import race.Race;
import player.Player;

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
    }


    @Test
    public void quest_hasCorrectID() {
        assertEquals("DungeonBeat", questDungeonBeat.getID(), "Quest borde ha rätt ID");
    }

    @Test
    public void quest_hasCorrectName() {
        assertEquals("Beat the Dungeon", questDungeonBeat.getName(), "Quest borde ha rätt namn");
    }

    @Test
    public void quest_hasCorrectDescription() {
        assertEquals("Defeat all enemies in the dungeon", questDungeonBeat.getDescription(), "Quest borde ha rätt beskrivning");
    }

    @Test
    public void quest_implementsPlayerQuest() {
        assertTrue(questDungeonBeat instanceof Player.Quest, "Quest borde implementera Player.Quest");
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
    public void npc_hasCorrectName() {
        assertEquals("Xerxes", npcXerxes.getName(), "NPC borde ha rätt namn");
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
    public void multipleNPC_canGiveDifferentQuests() {
        npcXerxes.giveQuestToPlayer(player);
        npcCyrus.giveQuestToPlayer(player);
        assertEquals(2, player.getQuestCount(), "Player borde ha 2 quests från olika NPCs");
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
    public void npc_canCheckIfPlayerHasRequiredItem() {
        assertFalse(npcCyrus.completeQuestIfPlayerHasItem(player), "NPC borde returnera false om player inte har ringen");
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