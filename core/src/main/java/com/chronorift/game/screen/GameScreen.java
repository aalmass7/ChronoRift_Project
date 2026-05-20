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
    private final InputHandler inputHandler;
    private boolean paused;
    private boolean settingsOpen;

    public GameScreen(Main game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        this.inputHandler = new InputHandler();
        camera.position.set(GameConfig.WORLD_WIDTH / 2f, GameConfig.WORLD_HEIGHT / 2f, 0f);
        camera.update();
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


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
