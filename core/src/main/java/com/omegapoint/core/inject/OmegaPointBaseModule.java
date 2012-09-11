package com.omegapoint.core.inject;

import com.artemis.EntitySystem;
import com.artemis.SystemManager;
import com.artemis.World;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.omegapoint.core.BulletCollisionPredicate;
import com.omegapoint.core.GameScreen;
import com.omegapoint.core.components.*;
import com.omegapoint.core.systems.*;
import playn.core.PlayN;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class OmegaPointBaseModule {
    public void bindAll(BindProvider binder) {
        binder.bind(World.class).in(Singleton.class);
        binder.bind(EnemySystem.class).in(Singleton.class);
        binder.bind(SimpleTweenSystem.class).in(Singleton.class);
        binder.bind(MovementSystem.class).in(Singleton.class);
        binder.bind(CollisionSystem.class).in(Singleton.class);
        binder.bind(SpriteRenderSystem.class).in(Singleton.class);
        binder.bind(TextRenderSystem.class).in(Singleton.class);
        binder.bind(BeamRenderSystem.class).in(Singleton.class);
        binder.bind(AudioSystem.class).in(Singleton.class);
        binder.bind(StarRenderSystem.class).in(Singleton.class);
        binder.bind(TileRenderSystem.class).in(Singleton.class);
        binder.bind(GameScreen.class).in(Singleton.class);
        binder.bind(Playfield.class).in(Singleton.class);
//        binder.bind(ScreenStack.class).to(ScreenStackImpl.class).in(Singleton.class);
//        binder.bind(EntityDatabase.class).to(StaticEntityDatabase.class).in(Singleton.class);
//        binder.bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public SystemManager getSystemManager(World world,
                                          SimpleTweenSystem simpleTweenSystem,
                                          TextRenderSystem textRenderSystem,
                                          EnemySystem enemySystem,
                                          MovementSystem movementSystem,
                                          CollisionSystem collisionSystem,
                                          SpriteRenderSystem spriteRenderSystem,
                                          BeamRenderSystem beamRenderSystem,
                                          AudioSystem audioSystem,
                                          StarRenderSystem starRenderSystem,
                                          TileRenderSystem tileRenderSystem) {
        SystemManager sm = world.getSystemManager();
        sm.setSystem(simpleTweenSystem);
        sm.setSystem(textRenderSystem);
        sm.setSystem(enemySystem);
        sm.setSystem(movementSystem);
        sm.setSystem(collisionSystem);
        sm.setSystem(spriteRenderSystem);
        sm.setSystem(beamRenderSystem);
        sm.setSystem(audioSystem);
        sm.setSystem(starRenderSystem);
        sm.setSystem(tileRenderSystem);
        return sm;
    }


    @Provides
    @Singleton
    @Named("renderSystems")
    public List<EntitySystem> providesRenderSystems(TextRenderSystem textRenderSystem,
                                                    SpriteRenderSystem spriteRenderSystem,
                                                    BeamRenderSystem beamRenderSystem,
                                                    AudioSystem audioSystem,
                                                    StarRenderSystem starRenderSystem,
                                                    TileRenderSystem tileRenderSystem) {
        ArrayList<EntitySystem> list = new ArrayList<EntitySystem>();
        list.add(textRenderSystem);
        list.add(starRenderSystem);
        list.add(spriteRenderSystem);
        list.add(beamRenderSystem);
        list.add(audioSystem);
        list.add(tileRenderSystem);
        return list;
    }

    @Provides
    @Singleton
    @Named("updateSystems")
    public List<EntitySystem> providesUpdateSystems(SimpleTweenSystem simpleTweenSystem,
                                                    EnemySystem enemySystem,
                                                    MovementSystem movementSystem,
                                                    CollisionSystem collisionSystem) {
        ArrayList<EntitySystem> list = new ArrayList<EntitySystem>();
        list.add(simpleTweenSystem);
        list.add(enemySystem);
        list.add(movementSystem);
        list.add(collisionSystem);
        return list;
    }

    @Provides
    @Singleton
    @Named("jsonableComponentRegistry")
    public JsonableRegistry<BaseComponent> providesJsonableComponentRegistry() {
        JsonableRegistry<BaseComponent> registry = new JsonableRegistry<BaseComponent>();

        registry.register(MovementComponent.NAME, new MovementComponent.Codec());
        registry.register(PositionComponent.NAME, new PositionComponent.Codec());
        registry.register(AudioComponent.NAME, new AudioComponent.Codec());
        registry.register(SpriteComponent.NAME, new SpriteComponent.Codec());
        registry.register(TextComponent.NAME, new TextComponent.Codec());
        registry.register(BeamComponent.NAME, new BeamComponent.Codec());
        registry.register(DamageComponent.NAME, new DamageComponent.Codec());
        registry.register(EnemyComponent.NAME, new EnemyComponent.Codec());
        registry.register(CollisionComponent.NAME, new CollisionComponent.Codec());
        registry.register(TileComponent.NAME, new TileComponent.Codec());
        return registry;
    }


    @Provides
    @Singleton
    public EntityTemplates providesEntityTemplates(@Named("entityDatabases") List<EntityDatabase> databases,
                                                   @Named("jsonableComponentRegistry") JsonableRegistry<BaseComponent> registry) {
        EntityTemplates templates = new EntityTemplates(registry);
        for (EntityDatabase database : databases) {
            for (String entityTemplateJson : database.getTemplates()) {
                templates.parseAndRegister(entityTemplateJson);
            }
        }
        return templates;
    }

    @Provides
    @Named("persistentEntityDatabase")
    @Singleton
    public EntityDatabase providesPersistentEntityDatabase() {
        return new MemoryEntityDatabase();
    }

    @Provides
    @Singleton
    @Named("entityDatabases")
    public List<EntityDatabase> providesEntityDatabases(@Named("persistentEntityDatabase") EntityDatabase persistent) {
        CollisionPredicates.register(BulletCollisionPredicate.NAME, new BulletCollisionPredicate());
        ArrayList<EntityDatabase> list = new ArrayList<EntityDatabase>();
        list.add(new StaticEntityDatabase());
        list.add(persistent);
        return list;
    }

    @Provides
    @Singleton
    public EventBus providesEventBus() {
        return new SimpleEventBus();
    }


    @Provides
    @Singleton
    public ScreenStack providesScreenStack() {
        return new ScreenStackImpl();
    }

    public static class ScreenStackImpl extends ScreenStack {
        @Override
        protected void handleError(RuntimeException error) {
            PlayN.log().debug(error.toString(), error);
        }
    }
}
