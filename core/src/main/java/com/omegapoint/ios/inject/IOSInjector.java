package com.omegapoint.ios.inject;

import com.artemis.EntitySystem;
import com.artemis.World;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.Enemies;
import com.omegapoint.core.Playfield;
import com.omegapoint.core.data.PlayNStorageEntityDatabase;
import com.omegapoint.core.screens.*;
import com.omegapoint.core.OmegaPointGame;
import com.omegapoint.core.data.EntityTemplates;
import com.omegapoint.core.data.MemoryEntityDatabase;
import com.omegapoint.core.state.*;
import com.omegapoint.core.systems.*;
import com.omegapoint.core.util.Scheduler;
import playn.core.Game;
import tripleplay.game.ScreenStack;

import java.util.List;

/**
 *
 */
public class IOSInjector {

    public static Game getGame() {
        IOSModule module = new IOSModule();

        ScreenStack screens = module.providesScreenStack();
        EventBus eventBus = module.providesEventBus();
        EntityTemplates templateManager = module.providesEntityTemplates(module.providesEntityDatabases(new PlayNStorageEntityDatabase()),
                module.providesJsonableComponentRegistry());

        IntroScreen screen = new IntroScreen(templateManager, eventBus);
        IntroGameState introGameState = new IntroGameState(screen, eventBus);
        LoadGameState loadState = new LoadGameState(new LoadScreen(), templateManager, eventBus);
        World world = new World();
        Enemies enemies = new Enemies();
        Playfield playfield = new Playfield();
        SimpleTweenSystem simpleTweenSystem = new SimpleTweenSystem();
        WaveSystem waveSystem = new WaveSystem(enemies, templateManager, eventBus);
        MovementSystem movementSystem = new MovementSystem();
        CollisionSystem collisionSystem = new CollisionSystem(eventBus, templateManager);
        EnemySystem enemySystem = new EnemySystem(templateManager, world);
        List<EntitySystem> updateSystems = module.providesUpdateSystems(simpleTweenSystem, waveSystem,
                movementSystem, collisionSystem, enemySystem);
        TextRenderSystem textRenderSystem = new TextRenderSystem(playfield);
        SpriteRenderSystem spriteRenderSystem = new SpriteRenderSystem(playfield);
        BeamRenderSystem beamRenderSystem = new BeamRenderSystem(playfield);
        AudioSystem audioSystem = new AudioSystem();
        StarRenderSystem starRenderSystem = new StarRenderSystem(playfield);
        TileRenderSystem tileRenderSystem = new TileRenderSystem(playfield);
        TileEditorRenderSystem tileEditorRenderSystem = new TileEditorRenderSystem(playfield);

        List<EntitySystem> renderSystems = module.providesRenderSystems(textRenderSystem,
                spriteRenderSystem, beamRenderSystem, audioSystem, starRenderSystem, tileRenderSystem,
                tileEditorRenderSystem);
        GameScreen gameScreen = new GameScreen(world,
                module.getSystemManager(world, simpleTweenSystem, textRenderSystem, waveSystem, movementSystem,
                        collisionSystem, spriteRenderSystem, beamRenderSystem, audioSystem, starRenderSystem,
                        tileRenderSystem, tileEditorRenderSystem, enemySystem), playfield,
                screens, updateSystems,
                renderSystems,
                templateManager, eventBus, enemies, new Scheduler());
        PlayGameState playState = new PlayGameState(gameScreen);
        return new OmegaPointGame(module.providesStateMachine(new OmegaStateMachineBuilder(screens, loadState,
                introGameState, playState, new PauseGameState(new PauseMenuScreen(eventBus)),
                new TileEditorState(new TileEditorScreen(templateManager, eventBus)),
                new DebugGameState(new DebugScreen(eventBus)))), screens, gameScreen,
                eventBus);
    }
}
