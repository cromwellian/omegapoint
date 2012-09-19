package com.omegapoint.core.achievements;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.Playfield;
import com.omegapoint.core.components.InventoryComponent;
import com.omegapoint.core.events.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Monitor game state for achivements.
 */
public class AchievementManagerSystem extends EntityProcessingSystem {
    private final EventBus eventBus;

    private int totalEnemiesKilled = 0 ;
    public int enemiesKilledSinceSpawn = 0;
    private ComponentMapper<InventoryComponent> inventoryMapper;
    private InventoryComponent inventoryComponent;
    private int playerKilled;
    private int bulletsFired;

    private List<Achievement> achievements = new ArrayList<Achievement>();

    @Inject
    public AchievementManagerSystem(EventBus eventBus) {
        super(InventoryComponent.class);
        this.eventBus = eventBus;
        eventBus.addHandler(EnemyKilledEvent.TYPE, new EnemyKilledHandler() {

            @Override
            public void onEnemyKilled(EnemyKilledEvent event) {
              totalEnemiesKilled++;
              enemiesKilledSinceSpawn++;
              checkAchievements();
            }
        });

        eventBus.addHandler(PlayerKilledEvent.TYPE, new PlayerKilledHandler() {
            @Override
            public void onPlayerKilled(PlayerKilledEvent event) {
               playerKilled++;
               enemiesKilledSinceSpawn = 0;
               checkAchievements();
            }
        });

        eventBus.addHandler(BulletDeleteEvent.TYPE, new BulletDeleteHandler() {
            @Override
            public void onBulletDelete(BulletDeleteEvent bulletDeleteEvent) {
                bulletsFired++;
                checkAchievements();
            }
        });

        installAchievements();
    }

    private void installAchievements() {
        achievements.add(new Achievement() {
            @Override
            public boolean conditionSatisfied() {
                return inventoryComponent.score() >= 500;
            }

            @Override
            public String getDisplayMessage() {
                return "Ten Thousand Points!";
            }

            @Override
            public String achievementId() {
                return "CgkIs-HH9_YMEAEQjMjkogU";
            }
        });

        achievements.add(new Achievement() {
            @Override
            public boolean conditionSatisfied() {
                return inventoryComponent.score() >= 50000;
            }

            @Override
            public String getDisplayMessage() {
                return "Fifty Thousand Points!";
            }

            @Override
            public String achievementId() {
                return "CgkIs-HH9_YMEAEQ5ayfi_______AQ";
            }
        });

        achievements.add(new Achievement() {
            @Override
            public boolean conditionSatisfied() {
                return false;
            }

            @Override
            public String getDisplayMessage() {
                return "Five in a Row!";
            }

            @Override
            public String achievementId() {
                return "CgkIs-HH9_YMEAEQmejb6P7_____AQ";
            }
        });

        achievements.add(new Achievement() {
            @Override
            public boolean conditionSatisfied() {
                return enemiesKilledSinceSpawn >= 50;
            }

            @Override
            public String getDisplayMessage() {
                return "Fifty for Fifty!";
            }

            @Override
            public String achievementId() {
                return "CgkIs-HH9_YMEAEQ3fD3Qg";
            }
        });

        achievements.add(new Achievement() {
            @Override
            public boolean conditionSatisfied() {
                return totalEnemiesKilled >= 50 && 100 * totalEnemiesKilled/bulletsFired > 75;
            }

            @Override
            public String getDisplayMessage() {
                return "Seventy Percent Accuracy!";
            }

            @Override
            public String achievementId() {
                return "CgkIs-HH9_YMEAEQqfKu9fv_____AQ";
            }
        });
    }

    private void checkAchievements() {
        Iterator<Achievement> it = achievements.iterator();
        while (it.hasNext()) {
            Achievement achievement = it.next();
            if (achievement.conditionSatisfied()) {
                eventBus.fireEvent(new AchievementAchievedEvent(achievement));
                it.remove();
            }
        }
    }

    @Override
    protected void initialize() {
      inventoryMapper = new ComponentMapper<InventoryComponent>(InventoryComponent.class, world);
    }

    @Override
    protected void process(Entity e) {
      this.inventoryComponent = inventoryMapper.get(e);
    }
}
