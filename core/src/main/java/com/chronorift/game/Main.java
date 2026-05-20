package com.chronorift.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chronorift.game.core.GameAssets;
import com.chronorift.game.screen.EndScreen;
import com.chronorift.game.screen.GameScreen;
import com.chronorift.game.screen.MenuScreen;

public class Main extends Game {
    private SpriteBatch batch;
    private BitmapFont font;
    private GameAssets assets;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.1f);
        assets = new GameAssets();
        assets.load();
        setScreen(new MenuScreen(this));
    }

    public void startGame() {
        setScreen(new GameScreen(this));
    }

    public void showEndScreen(boolean victory, int score) {
        setScreen(new EndScreen(this, victory, score));
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public GameAssets getAssets() {
        return assets;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (screen != null) {
            screen.dispose();
        }
        assets.dispose();
        font.dispose();
        batch.dispose();
    }
}
