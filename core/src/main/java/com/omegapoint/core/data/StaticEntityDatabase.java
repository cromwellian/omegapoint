package com.omegapoint.core.data;

import com.omegapoint.core.components.*;
import com.omegapoint.core.predicates.BulletCollisionPredicate;
import com.omegapoint.core.predicates.EnemyBulletCollisionPredicate;
import com.omegapoint.core.predicates.EnemyCollisionPredicate;
import com.omegapoint.core.tween.TextColorChanger;
import com.omegapoint.core.util.JsonUtil;
import playn.core.Font;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.TextFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.json;

/**
 * Hardcoded {@link com.omegapoint.core.data.EntityDatabase} to be loaded prior to mutable databases.
 */
public class StaticEntityDatabase implements EntityDatabase {
    @Override
    public Collection<String> getTemplates() {
        return Arrays.asList(JsonUtil.toString(makePlayerShip()),
                JsonUtil.toString(makeInventory()),
                JsonUtil.toString(makeExplosion()), JsonUtil.toString(makeTitleMusic()), JsonUtil.toString(makeTitleText()),
                JsonUtil.toString(makeTopBounds()), JsonUtil.toString(makeLeftBounds()), JsonUtil.toString(makeRightBounds()),
                JsonUtil.toString(makeBottomBounds()), JsonUtil.toString(makeLaser()), JsonUtil.toString(makeBeam()),
                JsonUtil.toString(makeTiles()),
                JsonUtil.toString(makeEnemyShip1()),
                JsonUtil.toString(makeEnemyShip2()),
                JsonUtil.toString(makeEnemyShip3()),
                JsonUtil.toString(makeEnemyShip4()),
                JsonUtil.toString(makeWave1()),
                JsonUtil.toString(makeWave2()),
                JsonUtil.toString(makeWave3()),
                JsonUtil.toString(makeWave4()),
                JsonUtil.toString(makeTitleCredits()),
                JsonUtil.toString(makeEnemyShot1()),
                JsonUtil.toString(makeEnemyShot2()),
                JsonUtil.toString(makeShield()));
    }

    private Json.Object makeShield() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "shield");
        obj.put(EntityTemplate.GROUP, "PASSIVES");
        obj.put(SpriteComponent.NAME, new SpriteComponent("images/shieldAlphaGreen.png", 48, 48, 10, 4, 0, 0, 16, true).toJson());
        return obj;
    }

    @Override
    public void persist(Collection<EntityTemplate> toBePersisted) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private Json.Object makeEnemyShip1() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "enemy1");
        obj.put(SpriteComponent.NAME, new SpriteComponent("images/shipsAlpha.png", 36, 36, 10, 32, 0, 0, -1, false).toJson());
        obj.put(MovementComponent.NAME, new MovementComponent(-5, 0, MovementComponent.MotionType.LINEAR, false).toJson());
        obj.put(EntityTemplate.GROUP, "ENEMY");
        obj.put(CollisionComponent.NAME, new CollisionComponent(0, 0, 36, 36, new EnemyCollisionPredicate()).toJson());
        obj.put(EnemyComponent.NAME, new EnemyComponent(EnemyComponent.EnemyType.BASIC, 100, 100, EnemyComponent.AiType.NONE, 0, "enemyShot1").toJson());
        // note, relative coordinates
        obj.put(PositionComponent.NAME, new PositionComponent(100, 50, -Math.PI/2).toJson());
        return obj;
    }

    private Json.Object makeEnemyShip2() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "enemy2");
        obj.put(SpriteComponent.NAME, new SpriteComponent("images/shipsAlpha.png", 36, 36, 10, 32, 40, 0, -1, false).toJson());
        obj.put(MovementComponent.NAME, new MovementComponent(-5, 0, MovementComponent.MotionType.LINEAR, false).toJson());
        obj.put(EntityTemplate.GROUP, "ENEMY");
        obj.put(CollisionComponent.NAME, new CollisionComponent(0, 0, 36, 36, new EnemyCollisionPredicate()).toJson());
        // note, relative coordinates
        obj.put(PositionComponent.NAME, new PositionComponent(100, 25, -Math.PI/2).toJson());
        obj.put(EnemyComponent.NAME, new EnemyComponent(EnemyComponent.EnemyType.BASIC, 100, 200, EnemyComponent.AiType.SHOOT_LEFT, 1000,
                "enemyShot2").toJson());

        return obj;
    }

    private Json.Object makeEnemyShip3() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "enemy3");
        obj.put(SpriteComponent.NAME, new SpriteComponent("images/tarentulaAlpha.png", 60, 60, 10, 4, 0, 0, -1, false).toJson());
        obj.put(MovementComponent.NAME, new MovementComponent(-10, 0, MovementComponent.MotionType.SINUSOIDAL, false).toJson());
        obj.put(EntityTemplate.GROUP, "ENEMY");
        obj.put(CollisionComponent.NAME, new CollisionComponent(0, 0, 72, 72, new EnemyCollisionPredicate()).toJson());
        // note, relative coordinates
        obj.put(EnemyComponent.NAME, new EnemyComponent(EnemyComponent.EnemyType.BASIC, 100, 500, EnemyComponent.AiType.SHOOT_LEFT, 1000, "enemyShot1").toJson());

        obj.put(PositionComponent.NAME, new PositionComponent(100, 70, 0).toJson());
        return obj;
    }


    private Json.Object makeEnemyShip4() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "enemy4");
        obj.put(SpriteComponent.NAME, new SpriteComponent("images/shipsAlpha.png", 36, 36, 10, 32, 80, 0, -1, false).toJson());
        obj.put(MovementComponent.NAME, new MovementComponent(-10, 0, MovementComponent.MotionType.SINUSOIDAL, false).toJson());
        obj.put(EntityTemplate.GROUP, "ENEMY");
        obj.put(CollisionComponent.NAME, new CollisionComponent(0, 0, 36, 36, new EnemyCollisionPredicate()).toJson());
        // note, relative coordinates
        obj.put(PositionComponent.NAME, new PositionComponent(100, 50, 0).toJson());
        obj.put(EnemyComponent.NAME, new EnemyComponent(EnemyComponent.EnemyType.BASIC, 100, 100, EnemyComponent.AiType.SHOOT_LEFT, 1000, "enemyShot2").toJson());
        return obj;
    }

    private Json.Object makeEnemyShot1() {
        Json.Object shot = json().createObject();

        shot.put(EntityTemplate.NAME, "enemyShot1");
        shot.put(EntityTemplate.GROUP, "ENEMYBULLET");
        shot.put(SpriteComponent.NAME, new SpriteComponent("images/bombsAlpha.png", 16, 16, 10, 13, 0, 10, 33, true).toJson());
        shot.put(CollisionComponent.NAME, new CollisionComponent(0, 0, 16, 16, new EnemyBulletCollisionPredicate()).toJson());
        shot.put(DamageComponent.NAME, new DamageComponent().toJson());
        return shot;
    }

    private Json.Object makeEnemyShot2() {
        Json.Object shot = json().createObject();

        shot.put(EntityTemplate.NAME, "enemyShot2");
        shot.put(EntityTemplate.GROUP, "ENEMYBULLET");
        shot.put(SpriteComponent.NAME, new SpriteComponent("images/bombsAlpha.png", 16, 16, 10, 13, 120, 130, 33, true).toJson());
        shot.put(CollisionComponent.NAME, new CollisionComponent(0, 0, 16, 16, new EnemyBulletCollisionPredicate()).toJson());
        shot.put(DamageComponent.NAME, new DamageComponent().toJson());
        return shot;
    }

    private Json.Object makeWave1() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "wave1");
        List<WaveComponent.SpawnComponent> spawns = new ArrayList<WaveComponent.SpawnComponent>();
        for (int i = 0; i < 5; i++) {
            spawns.add(new WaveComponent.SpawnComponent("enemy1", i == 0 ? 2000 : 300));
        }
        obj.put(WaveComponent.NAME, new WaveComponent(spawns, "wave2", "foo").toJson());
        return obj;
    }

    private Json.Object makeWave2() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "wave2");
        List<WaveComponent.SpawnComponent> spawns = new ArrayList<WaveComponent.SpawnComponent>();
        for (int i = 0; i < 5; i++) {
            spawns.add(new WaveComponent.SpawnComponent("enemy2", i == 0 ? 2000 : 300));
        }
        obj.put(WaveComponent.NAME, new WaveComponent(spawns, "wave3", "foo").toJson());
        return obj;
    }

    private Json.Object makeWave3() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "wave3");
        List<WaveComponent.SpawnComponent> spawns = new ArrayList<WaveComponent.SpawnComponent>();
        for (int i = 0; i < 5; i++) {
            spawns.add(new WaveComponent.SpawnComponent("enemy3", i == 0 ? 2000 : 300));
        }
        obj.put(WaveComponent.NAME, new WaveComponent(spawns, "wave4", "foo").toJson());
        return obj;
    }

    private Json.Object makeWave4() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "wave4");
        List<WaveComponent.SpawnComponent> spawns = new ArrayList<WaveComponent.SpawnComponent>();
        for (int i = 0; i < 5; i++) {
            spawns.add(new WaveComponent.SpawnComponent("enemy" + (i % 4 + 1), i == 0 ? 2000 : 300));
        }
        obj.put(WaveComponent.NAME, new WaveComponent(spawns, "wave1", "foo").toJson());
        return obj;
    }

    private Json.Object makeExplosion() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "explosion");
        obj.put(SpriteComponent.NAME, new SpriteComponent("images/explode2Alpha.png", 80, 80, 4, 11, 0, 0, 8, false).toJson());
        obj.put(AudioComponent.NAME, new AudioComponent("sounds/bomb").toJson());
        return obj;
    }

    private Json.Object makeTiles() {
        Json.Object tiles = json().createObject();
        tiles.put(EntityTemplate.NAME, "level1tiles");
        tiles.put(TileComponent.NAME, new TileComponent("images/defaultTileset.png", 16, 16, 10, 19, 0, 2,
                new TileComponent.TileArrangement(makeLevel1())).toJson());
        return tiles;
    }

    private TileComponent.TileRow[] makeLevel1() {
        TileComponent.TileRow[] rows = new TileComponent.TileRow[PlayN.graphics().height() / 16 / 4];
        for (int i = 0; i < rows.length; i++) {
            int[] indices = new int[100];
            for (int j = 0; j < indices.length; j++) {
                indices[j] = (int) (Math.random() * 90);
                if (Math.random() > 0.2) {
                    // testing empty space
                    indices[j] = TileComponent.EMPTY_SPACE;
                }
            }
            rows[i] = new TileComponent.TileRow(indices);
        }
        return rows;
    }

    // TODO(cromwellian) handle resize
    private Json.Object makeBottomBounds() {
        Json.Object bottomBounds = json().createObject();
        bottomBounds.put(EntityTemplate.NAME, "bottomBounds");
        bottomBounds.put(CollisionComponent.NAME, new CollisionComponent(0, 0, graphics().width() + 200, 100).toJson());
        bottomBounds.put(PositionComponent.NAME, new PositionComponent(-100, graphics().height(), 0).toJson());
        bottomBounds.put(EntityTemplate.GROUP, "BOUNDS");
        return bottomBounds;
    }

    private Json.Object makeRightBounds() {
        Json.Object rightBounds = json().createObject();
        rightBounds.put(EntityTemplate.NAME, "rightBounds");
        rightBounds.put(CollisionComponent.NAME, new CollisionComponent(0, 0, graphics().width(), graphics().height() + 200).toJson());
        rightBounds.put(PositionComponent.NAME, new PositionComponent((int) (1.5 * graphics().width()), graphics().height()/2, 0).toJson());
        rightBounds.put(EntityTemplate.GROUP, "BOUNDS");
        return rightBounds;
    }

    private Json.Object makeLeftBounds() {
        Json.Object leftBounds = json().createObject();
        leftBounds.put(EntityTemplate.NAME, "leftBounds");
        leftBounds.put(CollisionComponent.NAME, new CollisionComponent(0, 0, graphics().width(), graphics().height() + 200).toJson());
        leftBounds.put(PositionComponent.NAME, new PositionComponent(-100 - graphics().width()/2, graphics().height()/2, 0).toJson());
        leftBounds.put(EntityTemplate.GROUP, "BOUNDS");
        return leftBounds;
    }

    private Json.Object makeTopBounds() {
        Json.Object topBounds = json().createObject();
        topBounds.put(EntityTemplate.NAME, "topBounds");
        topBounds.put(CollisionComponent.NAME, new CollisionComponent(0, 0, graphics().width() + 200, 100).toJson());
        topBounds.put(PositionComponent.NAME, new PositionComponent(-100, -100, 0).toJson());
        topBounds.put(EntityTemplate.GROUP, "BOUNDS");
        return topBounds;
    }

    private Json.Object makeTitleText() {
        Json.Object titleText = json().createObject();
        titleText.put(EntityTemplate.NAME, "titleText");
        titleText.put(TextComponent.NAME, new TextComponent("OMEGA POINT", PlayN.graphics().createFont("Space Age", Font.Style.PLAIN, 80),
                TextFormat.Alignment.CENTER, 0xff000080).toJson());
        titleText.put(PositionComponent.NAME, new PositionComponent(0, 0, 0).toJson());
        titleText.put(SimpleTweenComponent.NAME, new SimpleTweenComponent(0.0f, 1.0f, 250, new TextColorChanger(0xff000080, 0xff0000ff), true, true).toJson());
        return titleText;
    }

    private Json.Object makeTitleCredits() {
        Json.Object titleText = json().createObject();
        titleText.put(EntityTemplate.NAME, "titleTextCredits");
        titleText.put(TextComponent.NAME, new TextComponent("by Ray Cromwell", PlayN.graphics().createFont("Space Age", Font.Style.PLAIN, 30),
                TextFormat.Alignment.CENTER, 0xff802020).toJson());
        titleText.put(PositionComponent.NAME, new PositionComponent(0, 75, 0).toJson());
        titleText.put(SimpleTweenComponent.NAME, new SimpleTweenComponent(0.0f, 1.0f, 500, new TextColorChanger(0xff800080, 0xff0000ff), true, true).toJson());
        return titleText;
    }


    private Json.Object makeTitleMusic() {
        Json.Object titleMusic = json().createObject();
        titleMusic.put(EntityTemplate.NAME, "backgroundMusic");
        titleMusic.put(AudioComponent.NAME, new AudioComponent("sounds/angryrobot3", 0.7).toJson());
        return titleMusic;
    }

    private Json.Object makePlayerShip() {
        Json.Object ship = json().createObject();
        ship.put(EntityTemplate.NAME, "playerShip");
        ship.put(SpriteComponent.NAME, new SpriteComponent("images/ship.png").toJson());
        ship.put(PositionComponent.NAME, new PositionComponent(0, 0, -Math.PI / 2).toJson());
        return ship;
    }

    private Json.Object makeInventory() {
        Json.Object inventory = json().createObject();
        inventory.put(EntityTemplate.NAME, "inventory");
        inventory.put(InventoryComponent.NAME, new InventoryComponent(0, 0).toJson());
        return inventory;
    }

    private Json.Object makeLaser() {
        Json.Object laserShot = json().createObject();

        laserShot.put(EntityTemplate.NAME, "laserShot");
        laserShot.put(MovementComponent.NAME, new MovementComponent(20, 0, MovementComponent.MotionType.LINEAR, false).toJson());
        laserShot.put(AudioComponent.NAME, new AudioComponent("sounds/laser").toJson());
        laserShot.put(SpriteComponent.NAME, new SpriteComponent("images/lasershot.png").toJson());
        laserShot.put(CollisionComponent.NAME, new CollisionComponent(0, 0, 32, 8, new BulletCollisionPredicate()).toJson());
        laserShot.put(DamageComponent.NAME, new DamageComponent().toJson());
        return laserShot;
    }

    private Json.Object makeBeam() {
        Json.Object beamShot = json().createObject();

        beamShot.put(EntityTemplate.NAME, "beamShot");
        beamShot.put(MovementComponent.NAME, new MovementComponent(20, 0, MovementComponent.MotionType.LINEAR, false).toJson());
        beamShot.put(AudioComponent.NAME, new AudioComponent("sounds/laserheavy").toJson());
        beamShot.put(BeamComponent.NAME, new BeamComponent(128, 0xffff0000).toJson());
        beamShot.put(CollisionComponent.NAME, new CollisionComponent(0, 0, 32, 8, new BulletCollisionPredicate()).toJson());
        beamShot.put(DamageComponent.NAME, new DamageComponent().toJson());
        return beamShot;
    }

}
