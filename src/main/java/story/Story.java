package story;

import player.Player;
import java.util.ArrayList;
import java.util.List;

public class Story {

    public static class FightLog {
        private List<String> logEntries;

        public FightLog() {
            this.logEntries = new ArrayList<>();
        }

        public void logDamage(String source, int damage) {
            logEntries.add("-" + damage + "HP from " + source);
        }

        public List<String> getLogEntries() {
            return new ArrayList<>(logEntries);
        }

        public void clearLog() {
            logEntries.clear();
        }

        public int getLogSize() {
            return logEntries.size();
        }
    }

    public static abstract class Quest implements Player.Quest {
        protected String id;
        protected String name;
        protected String description;
        protected boolean isActive;
        protected boolean isCompleted;
        protected boolean isFailed;
        protected FightLog fightLog;

        protected Quest(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.isActive = false;
            this.isCompleted = false;
            this.isFailed = false;
            this.fightLog = new FightLog();
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

        public boolean isActive() {
            return this.isActive;
        }

        public boolean isCompleted() {
            return this.isCompleted;
        }

        public boolean isFailed() {
            return this.isFailed;
        }

        public FightLog getFightLog() {
            return this.fightLog;
        }

        public void startQuest() {
            if (!isActive && !isCompleted && !isFailed) {
                this.isActive = true;
            }
        }

        public void completeQuest() {
            if (isActive) {
                this.isActive = false;
                this.isCompleted = true;
            }
        }

        public void failQuest() {
            if (isActive) {
                this.isActive = false;
                this.isFailed = true;
            }
        }
    }

    public static class DungeonQuest extends Quest {
        private int enemiesKilled;
        private int requiredEnemies;
        private boolean doorUnlocked;
        private boolean bossDefeated;

        public DungeonQuest(String id, String name, String description, int requiredEnemies) {
            super(id, name, description);
            this.enemiesKilled = 0;
            this.requiredEnemies = requiredEnemies;
            this.doorUnlocked = false;
            this.bossDefeated = false;
        }

        public boolean unlockDungeonDoor(DungeonDoor door, boolean correctKeyhole) {
            if (isActive && !doorUnlocked) {
                if (correctKeyhole) {
                    door.unlock();
                    this.doorUnlocked = true;
                    return true;
                } else {
                    this.onPlayerDeath();
                    return false;
                }
            }
            return false;
        }

        public void fightEnemy(Enemy enemy, Player player) {
            if (!isActive || !doorUnlocked) {
                return;
            }

            int damage = enemy.getDamage();
            player.updateCurrentLife(-damage);
            fightLog.logDamage(enemy.getName(), damage);

            enemy.takeDamage(enemy.getHealth());
            if (enemy.isDead()) {
                enemiesKilled++;
            }
        }

        public boolean areAllEnemiesDead() {
            return enemiesKilled >= requiredEnemies;
        }

        public boolean startBossFight() {
            return isActive && areAllEnemiesDead();
        }

        public boolean fightBoss(Boss boss, Player player) {
            if (!isActive || !areAllEnemiesDead()) {
                return false;
            }

            int damage = boss.getDamage();
            player.updateCurrentLife(-damage);
            fightLog.logDamage(boss.getName(), damage);

            boss.takeDamage(boss.getHealth());
            if (boss.isDead()) {
                this.bossDefeated = true;
                this.completeQuest();
                return true;
            }
            return false;
        }

        public void playerDiesFromEnemy(Player player) {
            if (isActive) {
                player.updateCurrentLife(-player.getCurrentLife());
                this.onPlayerDeath();
            }
        }

        public void playerDiesFromBoss(Player player) {
            if (isActive) {
                player.updateCurrentLife(-player.getCurrentLife());
                this.onPlayerDeath();
            }
        }

        public void onPlayerDeath() {
            if (isActive) {
                this.enemiesKilled = 0;
                this.doorUnlocked = false;
                this.bossDefeated = false;
                this.fightLog.clearLog();
            }
        }

        public int getEnemiesKilled() {
            return this.enemiesKilled;
        }

        public boolean isDoorUnlocked() {
            return this.doorUnlocked;
        }
    }

    public static class TimedQuest extends Quest {
        private int enemiesKilled;
        private int requiredEnemies;
        private int hpLost;
        private int maxHpLoss;
        private long startTime;
        private long timeLimit;
        private boolean timerStarted;

        public TimedQuest(String id, String name, String description, int requiredEnemies, int maxHpLoss, long timeLimitSeconds) {
            super(id, name, description);
            this.enemiesKilled = 0;
            this.requiredEnemies = requiredEnemies;
            this.hpLost = 0;
            this.maxHpLoss = maxHpLoss;
            this.timeLimit = timeLimitSeconds * 1000;
            this.timerStarted = false;
        }

        @Override
        public void startQuest() {
            super.startQuest();
            this.startTime = System.currentTimeMillis();
            this.timerStarted = true;
        }

        public void enemyAttacksPlayer(Enemy enemy, Player player) {
            if (!isActive) {
                return;
            }

            if (isTimeExpired()) {
                this.failQuest();
                return;
            }

            int damage = enemy.getDamage();
            player.updateCurrentLife(-damage);
            hpLost += damage;
            fightLog.logDamage(enemy.getName(), damage);

            if (hpLost > maxHpLoss) {
                this.failQuest();
            }
        }

        public void playerKillsEnemy(Enemy enemy) {
            if (!isActive) {
                return;
            }

            if (isTimeExpired()) {
                this.failQuest();
                return;
            }

            enemy.takeDamage(enemy.getHealth());
            if (enemy.isDead()) {
                enemiesKilled++;

                if (enemiesKilled >= requiredEnemies && hpLost <= maxHpLoss && !isTimeExpired()) {
                    this.completeQuest();
                }
            }
        }

        public boolean isTimeExpired() {
            if (!timerStarted) {
                return false;
            }
            return (System.currentTimeMillis() - startTime) > timeLimit;
        }

        public void simulateTimeElapsed(long milliseconds) {
            this.startTime -= milliseconds;
        }

        public int getEnemiesKilled() {
            return this.enemiesKilled;
        }

        public int getHpLost() {
            return this.hpLost;
        }
    }

    public static class LostAndFoundQuest extends Quest {
        private String requiredItemName;

        public LostAndFoundQuest(String id, String name, String description, String requiredItemName) {
            super(id, name, description);
            this.requiredItemName = requiredItemName;
        }

        public boolean completeWithItem(Player player) {
            if (isActive && player.hasItem(requiredItemName)) {
                this.completeQuest();
                player.questWasCompleted(this.id);
                return true;
            }
            return false;
        }
    }

    public interface DungeonDoor {
        void unlock();
    }

    public interface Enemy {
        void takeDamage(int damage);
        int getDamage();
        int getHealth();
        String getName();
        boolean isDead();
    }

    public interface Boss {
        void takeDamage(int damage);
        int getHealth();
        int getDamage();
        String getName();
        boolean isDead();
    }

    public static class QuestItem {
        private String itemName;
        private String itemDescription;

        public QuestItem(String itemName, String itemDescription) {
            this.itemName = itemName;
            this.itemDescription = itemDescription;
        }
    }

    public static class NPC {
        private String name;
        private Quest quest;

        public NPC(String name, Quest quest) {
            this.name = name;
            this.quest = quest;
        }

        public void giveQuestToPlayer(Player player) {
            if (this.quest != null) {
                this.quest.startQuest();
                player.acceptQuest(this.quest);
            }
        }

        public void rewardPlayer(Player player, int lifeBonus) {
            if (this.quest.isCompleted()) {
                player.updateMaxLife(lifeBonus);
            }
        }
    }
}