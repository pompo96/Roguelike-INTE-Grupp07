package story;


import player.Player;


public class Story {


    public static class Quest implements Player.Quest {
        private String id;
        private String name;
        private String description;


        public Quest(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
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
                player.acceptQuest(this.quest);
            }
        }



        public boolean completeQuestIfPlayerHasItem(Player player) {
            if (player.hasItem(this.requiredItemName)) {
                player.questWasCompleted(this.quest.getID());
                return true;
            }
            return false;
        }
    }
}
