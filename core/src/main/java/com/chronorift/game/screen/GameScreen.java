package com.chronorift.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.chronorift.game.Main;
import com.chronorift.game.command.InputHandler;
import com.chronorift.game.core.GameConfig;
import com.chronorift.game.world.GameWorld;

public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final GameWorld world;
    private final InputHandler inputHandler;
    private boolean paused;
    private boolean settingsOpen;

    public GameScreen(Main game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        this.world = new GameWorld(game.getAssets(), game.getFont());
        this.inputHandler = new InputHandler();
        camera.position.set(GameConfig.WORLD_WIDTH / 2f, GameConfig.WORLD_HEIGHT / 2f, 0f);
        camera.update();
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1f / 30f);
        handlePauseInput();

        if (!paused) {
            world.execute(inputHandler.poll(viewport), delta);
            world.update(delta);
        }

        if (world.isVictory()) {
            game.showEndScreen(true, world.getScore());
            return;
        }
        if (world.isGameOver()) {
            game.showEndScreen(false, world.getScore());
            return;
        }

        Gdx.gl.glClearColor(0.02f, 0.02f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        world.render(game.getBatch());
        if (paused) {
            drawPauseOverlay();
        }
        game.getBatch().end();
    }

    private void handlePauseInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (!paused) {
                paused = true;
                settingsOpen = false;
            } else if (settingsOpen) {
                settingsOpen = false;
            } else {
                paused = false;
            }
        }

        if (paused && Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            settingsOpen = !settingsOpen;
        }

        if (paused && !settingsOpen && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            paused = false;
        }
    }

    private void drawPauseOverlay() {
        game.getBatch().setColor(0f, 0f, 0f, 0.58f);
        game.getBatch().draw(game.getAssets().pixel(), 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        game.getBatch().setColor(0.04f, 0.07f, 0.12f, 0.88f);
        game.getBatch().draw(game.getAssets().pixel(), 330f, 116f, 620f, 500f);
        game.getBatch().setColor(Color.WHITE);

        if (settingsOpen) {
            drawInRoundSettings();
        } else {
            drawPauseMenu();
        }
    }

    private void drawPauseMenu() {
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "PAUSED", 592f, 570f);
        game.getFont().draw(game.getBatch(), "The round is stopped while this menu is open.", 438f, 512f);

        game.getFont().setColor(new Color(0.55f, 0.95f, 1f, 1f));
        game.getFont().draw(game.getBatch(), "Press S - Settings / Controls", 500f, 444f);
        game.getFont().draw(game.getBatch(), "Press ENTER - Continue", 522f, 402f);
        game.getFont().draw(game.getBatch(), "Press ESC - Continue", 532f, 360f);
        game.getFont().setColor(Color.WHITE);
    }

    private void drawInRoundSettings() {
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "SETTINGS / CONTROLS", 520f, 570f);
        game.getFont().draw(game.getBatch(), "Movement: WASD", 392f, 514f);
        game.getFont().draw(game.getBatch(), "Aim / Shoot: Left mouse button", 392f, 482f);
        game.getFont().draw(game.getBatch(), "Dash: SPACE", 392f, 450f);
        game.getFont().draw(game.getBatch(), "Blue Shield: E (5s full immunity)", 392f, 418f);
        game.getFont().draw(game.getBatch(), "Chrono Elixir: Q (reduces slow/frozen/reverse penalties)", 392f, 386f);
        game.getFont().draw(game.getBatch(), "Ultra hero form: R", 392f, 354f);
        game.getFont().draw(game.getBatch(), "Checkpoint Rewind: C", 392f, 322f);
        game.getFont().draw(game.getBatch(), "Pause: ESC", 392f, 290f);

        game.getFont().setColor(new Color(0.55f, 0.95f, 1f, 1f));
        game.getFont().draw(game.getBatch(), "Press S or ESC to return to pause menu", 434f, 196f);
        game.getFont().setColor(Color.WHITE);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
