package com.chronorift.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.chronorift.game.Main;
import com.chronorift.game.core.GameConfig;

public class EndScreen extends ScreenAdapter {
    private final Main game;
    private final boolean victory;
    private final int score;
    private final OrthographicCamera camera;
    private final FitViewport viewport;

    public EndScreen(Main game, boolean victory, int score) {
        this.game = game;
        this.victory = victory;
        this.score = score;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        camera.position.set(GameConfig.WORLD_WIDTH / 2f, GameConfig.WORLD_HEIGHT / 2f, 0f);
        camera.update();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.startGame();
            return;
        }
        Gdx.gl.glClearColor(0.04f, 0.04f, 0.07f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getBatch().draw(game.getAssets().texture(victory ? "game/bg_orc_floor.png" : "game/bg_street.png"), 0f, 0f,
                GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        game.getFont().setColor(victory ? Color.GOLD : Color.SCARLET);
        game.getFont().draw(game.getBatch(), victory ? "VICTORY" : "GAME OVER", 590f, 520f);
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "Score: " + score, 590f, 480f);
        game.getFont().draw(game.getBatch(), victory ? "All timeline zones were restored: Normal, Slow, Frozen, Reversed, and Boss Timeline." : "The tower consumed the timeline.", 420f, 430f);
        game.getFont().draw(game.getBatch(), "Press ENTER to play again", 500f, 360f);
        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
