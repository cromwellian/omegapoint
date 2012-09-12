package com.omegapoint.core.components;

import com.omegapoint.core.BulletCollisionPredicate;
import com.omegapoint.core.EnemyCollisionPredicate;
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
 * Hardcoded {@link EntityDatabase} to be loaded prior to mutable databases.
 */
public class StaticEntityDatabase implements EntityDatabase {
    @Override
    public Collection<String> getTemplates() {
        return Arrays.asList(toString(makePlayerShip()), toString(makeEnemyShip1()),
                toString(makeExplosion()), toString(makeTitleMusic()), toString(makeTitleText()),
                toString(makeTopBounds()), toString(makeLeftBounds()), toString(makeRightBounds()),
                toString(makeBottomBounds()), toString(makeLaser()), toString(makeBeam()),
                toString(makeTiles()),
                toString(makeWave()));
    }

    private Json.Object makeEnemyShip1() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "enemy1");
        obj.put(SpriteComponent.NAME, new SpriteComponent("images/tarentulaAlpha.png", 60, 60, 10, 4, 0, -1, false).toJson());
        obj.put(MovementComponent.NAME, new MovementComponent(-5, 0, MovementComponent.MotionType.SINUSOIDAL, false).toJson());
        obj.put(EntityTemplate.GROUP, "ENEMY");
        obj.put(CollisionComponent.NAME, new CollisionComponent(0, 0, 72, 72, new EnemyCollisionPredicate()).toJson());
        // note, relative coordinates
        obj.put(PositionComponent.NAME, new PositionComponent(100, 50, 0).toJson());
        return obj;
    }

    private Json.Object makeWave() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "wave1");
        List<WaveComponent.SpawnComponent> spawns = new ArrayList<WaveComponent.SpawnComponent>();
        for (int i = 0; i < 5; i++) {
            spawns.add(new WaveComponent.SpawnComponent("enemy1", i == 4 ? 2000 : 300));
        }
        obj.put(WaveComponent.NAME, new WaveComponent(spawns, "wave1", "foo").toJson());
        return obj;
    }


    private Json.Object makeExplosion() {
        Json.Object obj = json().createObject();
        obj.put(EntityTemplate.NAME, "explosion");
        obj.put(SpriteComponent.NAME, new SpriteComponent("images/explode2Alpha.png", 80, 80, 4, 11, 0, 8, false).toJson());
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
        rightBounds.put(CollisionComponent.NAME, new CollisionComponent(0, 0, 100, graphics().height() + 200).toJson());
        rightBounds.put(PositionComponent.NAME, new PositionComponent(graphics().width(), -100, 0).toJson());
        rightBounds.put(EntityTemplate.GROUP, "BOUNDS");
        return rightBounds;
    }

    private Json.Object makeLeftBounds() {
        Json.Object leftBounds = json().createObject();
        leftBounds.put(EntityTemplate.NAME, "leftBounds");
        leftBounds.put(CollisionComponent.NAME, new CollisionComponent(0, 0, 100, graphics().height() + 200).toJson());
        leftBounds.put(PositionComponent.NAME, new PositionComponent(-100, -100, 0).toJson());
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
        titleText.put(TextComponent.NAME, new TextComponent("OMEGA POINT", PlayN.graphics().createFont("spaceage", Font.Style.PLAIN, 80),
                TextFormat.Alignment.CENTER, 0xff000080).toJson());
        titleText.put(PositionComponent.NAME, new PositionComponent(0, 0, 0).toJson());
        return titleText;
    }

    private Json.Object makeTitleMusic() {
        Json.Object titleMusic = json().createObject();
        titleMusic.put(EntityTemplate.NAME, "backgroundMusic");
        titleMusic.put(AudioComponent.NAME, new AudioComponent("sounds/cybernoid2", 0.5).toJson());
        return titleMusic;
    }

    private Json.Object makePlayerShip() {
        Json.Object ship = json().createObject();
        ship.put(EntityTemplate.NAME, "playerShip");
        ship.put(SpriteComponent.NAME, new SpriteComponent("images/ship.png").toJson());
        ship.put(PositionComponent.NAME, new PositionComponent(0, 0, -Math.PI / 2).toJson());
        return ship;
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

    private String toString(Json.Object obj) {
        Json.Writer sink = json().newWriter().object();
        obj.write(sink);
        sink.end();
        return sink.write();
    }
}
