package story;

import spelare.Player;
import java.util.ArrayList;
import java.util.List;

public class Story {

    public enum QuestState {
        NOT_STARTED,
        ACTIVE,
        COMPLETED,
        FAILED
    }


    public enum DungeonStage {
        SEARCHING_FOR_KEY_FRAGMENT_1,
        FRAGMENT_1_FOUND,
        SEARCHING_FOR_KEY_FRAGMENT_2,
        BOTH_FRAGMENTS_FOUND,
        KEY_ASSEMBLED,
    }


    public static class Quest implements Player.Quest {
        private String id;
        private String name;
        private String description;
        private QuestState state;

        public Quest(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.state = QuestState.NOT_STARTED;
        }

        @Override
        public String getID() {
            return this.id;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        public QuestState getState() {
            return this.state;
        }

        public void startQuest() {
            if (this.state == QuestState.NOT_STARTED) {
                this.state = QuestState.ACTIVE;
            }
        }

        public void completeQuest() {
            if (this.state == QuestState.ACTIVE) {
                this.state = QuestState.COMPLETED;
            }
        }

        public void failQuest() {
            if (this.state == QuestState.ACTIVE) {
                this.state = QuestState.FAILED;
            }
        }
    }


    public static class DungeonQuest extends Quest {
        private DungeonStage currentStage;
        private boolean hasFragment1;
        private boolean hasFragment2;
        private boolean hasCompleteKey;


        public DungeonQuest(String id, String name, String description) {
            super(id, name, description);
            this.currentStage = null;
            this.hasFragment1 = false;
            this.hasFragment2 = false;
            this.hasCompleteKey = false;
        }

        @Override
        public void startQuest() {
            super.startQuest();
            if (this.getState() == QuestState.ACTIVE) {
                this.currentStage = DungeonStage.SEARCHING_FOR_KEY_FRAGMENT_1;
            }
        }

        public DungeonStage getCurrentStage() {
            return this.currentStage;
        }

        public boolean findKeyFragment1(Area area) {
            if (currentStage == DungeonStage.SEARCHING_FOR_KEY_FRAGMENT_1 && area.hasKeyFragment1()) {
                this.hasFragment1 = true;
                this.currentStage = DungeonStage.FRAGMENT_1_FOUND;
                return true;
            }
            return false;
        }

        public void searchForFragment2() {
            if (currentStage == DungeonStage.FRAGMENT_1_FOUND) {
                this.currentStage = DungeonStage.SEARCHING_FOR_KEY_FRAGMENT_2;
            }
        }

        public boolean findKeyFragment2(Area area) {
            if (currentStage == DungeonStage.SEARCHING_FOR_KEY_FRAGMENT_2 && area.hasKeyFragment2()) {
                this.hasFragment2 = true;
                this.currentStage = DungeonStage.BOTH_FRAGMENTS_FOUND;
                return true;
            }
            return false;
        }

        public boolean assembleKey() {
            if (currentStage == DungeonStage.BOTH_FRAGMENTS_FOUND && hasFragment1 && hasFragment2) {
                this.hasCompleteKey = true;
                this.currentStage = DungeonStage.KEY_ASSEMBLED;
                return true;
            }
            return false;
        }

        public void approachDungeonDoor() {
            if (currentStage == DungeonStage.KEY_ASSEMBLED) {
                this.currentStage = DungeonStage.AT_DUNGEON_DOOR;
            }
        }

        public boolean hasFragment1() {
            return this.hasFragment1;
        }

        public boolean hasFragment2() {
            return this.hasFragment2;
        }

        public boolean hasCompleteKey() {
            return this.hasCompleteKey;
        }
    }

    public interface Area {
        boolean hasKeyFragment1();
        boolean hasKeyFragment2();
    }

    public static class QuestItem {
        private String itemName;
        private String itemDescription;

        public QuestItem(String itemName, String itemDescription) {
            this.itemName = itemName;
            this.itemDescription = itemDescription;
        }

        public String getItemName() {
            return this.itemName;
        }

        public String getItemDescription() {
            return this.itemDescription;
        }
    }

    public static class NPC {
        private String name;
        private Quest quest;
        private String requiredItemName;

        public NPC(String name, Quest quest, String requiredItemName) {
            this.name = name;
            this.quest = quest;
            this.requiredItemName = requiredItemName;
        }

        public String getName() {
            return this.name;
        }

        public Quest getQuest() {
            return this.quest;
        }

        public void giveQuestToPlayer(Player player) {
            if (this.quest != null) {
                this.quest.startQuest();
                player.acceptQuest(this.quest);
            }
        }

        public boolean completeQuestIfPlayerHasItem(Player player) {
            if (player.hasItem(this.requiredItemName)) {
                this.quest.completeQuest();
                player.questWasCompleted(this.quest.getID());
                return true;
            }
            return false;
        }


    }

}