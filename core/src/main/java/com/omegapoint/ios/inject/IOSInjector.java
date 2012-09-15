package com.omegapoint.ios.inject;

import com.artemis.EntitySystem;
import com.artemis.World;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.Enemies;
import com.omegapoint.core.Playfield;
import com.omegapoint.core.screens.GameScreen;
import com.omegapoint.core.OmegaPointGame;
import com.omegapoint.core.data.EntityTemplates;
import com.omegapoint.core.data.MemoryEntityDatabase;
import com.omegapoint.core.screens.IntroScreen;
import com.omegapoint.core.screens.LoadScreen;
import com.omegapoint.core.screens.PauseMenuScreen;
import com.omegapoint.core.state.*;
import com.omegapoint.core.systems.*;
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
        EntityTemplates templateManager = module.providesEntityTemplates(module.providesEntityDatabases(new MemoryEntityDatabase()),
                module.providesJsonableComponentRegistry());

        IntroScreen screen = new IntroScreen(templateManager, eventBus);
        IntroGameState introGameState = new IntroGameState(screen, eventBus);
        LoadGameState loadState = new LoadGameState(new LoadScreen(), eventBus);
        World world = new World();
        Enemies enemies = new Enemies();
        Playfield playfield = new Playfield();
        SimpleTweenSystem simpleTweenSystem = new SimpleTweenSystem();
        WaveSystem enemySystem = new WaveSystem(enemies, templateManager, eventBus);
        MovementSystem movementSystem = new MovementSystem();
        CollisionSystem collisionSystem = new CollisionSystem(eventBus, templateManager);
        List<EntitySystem> updateSystems = module.providesUpdateSystems(simpleTweenSystem, enemySystem,
                movementSystem, collisionSystem);
        TextRenderSystem textRenderSystem = new TextRenderSystem(playfield);
        SpriteRenderSystem spriteRenderSystem = new SpriteRenderSystem(playfield);
        BeamRenderSystem beamRenderSystem = new BeamRenderSystem(playfield);
        AudioSystem audioSystem = new AudioSystem();
        StarRenderSystem starRenderSystem = new StarRenderSystem(playfield);
        TileRenderSystem tileRenderSystem = new TileRenderSystem(playfield);
        List<EntitySystem> renderSystems = module.providesRenderSystems(textRenderSystem,
                spriteRenderSystem, beamRenderSystem, audioSystem, starRenderSystem, tileRenderSystem);
        GameScreen gameScreen = new GameScreen(world,
                module.getSystemManager(world, simpleTweenSystem, textRenderSystem, enemySystem, movementSystem,
                        collisionSystem, spriteRenderSystem, beamRenderSystem, audioSystem, starRenderSystem,
                        tileRenderSystem), playfield,
                screens, updateSystems,
                renderSystems,
                templateManager, eventBus, enemies);
        PlayGameState playState = new PlayGameState(gameScreen);
        return new OmegaPointGame(module.providesStateMachine(new OmegaStateMachineBuilder(screens, loadState,
                introGameState, playState, new PauseGameState(new PauseMenuScreen(eventBus)))), screens, gameScreen,
                eventBus);
    }
}
