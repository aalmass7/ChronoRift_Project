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

public class MenuScreen extends ScreenAdapter {
    private enum MenuTab {
        HOME,
        SETTINGS
    }

    private final Main game;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private MenuTab activeTab = MenuTab.HOME;

    public MenuScreen(Main game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        camera.position.set(GameConfig.WORLD_WIDTH / 2f, GameConfig.WORLD_HEIGHT / 2f, 0f);
        camera.update();
    }

    @Override
    public void render(float delta) {
        handleInput();
        Gdx.gl.glClearColor(0.06f, 0.07f, 0.10f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getBatch().draw(game.getAssets().texture("game/bg_street.png"), 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        drawPanelBackdrop();
        if (activeTab == MenuTab.HOME) {
            drawHomeTab();
        } else {
            drawSettingsTab();
        }
        game.getBatch().end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && activeTab == MenuTab.HOME) {
            game.startGame();
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            activeTab = activeTab == MenuTab.HOME ? MenuTab.SETTINGS : MenuTab.HOME;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && activeTab == MenuTab.SETTINGS) {
            activeTab = MenuTab.HOME;
        }
    }

    private void drawPanelBackdrop() {
        game.getBatch().setColor(0f, 0f, 0f, 0.46f);
        game.getBatch().draw(game.getAssets().pixel(), 42f, 82f, GameConfig.WORLD_WIDTH - 84f, GameConfig.WORLD_HEIGHT - 150f);
        game.getBatch().setColor(Color.WHITE);
    }

    private void drawHomeTab() {
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "CHRONO RIFT", 520f, 650f);
        game.getFont().draw(game.getBatch(), "Home tab", 60f, 610f);
        game.getFont().draw(game.getBatch(), "Time is controlled by zones, not by the hero.", 60f, 568f);
        game.getFont().draw(game.getBatch(), "GDD-aligned flow:", 60f, 518f);
        game.getFont().draw(game.getBatch(), "1. Normal Timeline - no negative effects, clear guards, enter tower.", 84f, 486f);
        game.getFont().draw(game.getBatch(), "2. Slow Motion Timeline - movement drag and adapted native enemies.", 84f, 456f);
        game.getFont().draw(game.getBatch(), "3. Frozen Timeline - real movement restriction and stronger frost zone pressure.", 84f, 426f);
        game.getFont().draw(game.getBatch(), "4. Reversed Timeline - reversed controls and cyber enemies.", 84f, 396f);
        game.getFont().draw(game.getBatch(), "5. Final Boss Timeline - mixed slow, frozen, reverse, and overload pulses.", 84f, 366f);

        game.getFont().setColor(new Color(0.55f, 0.95f, 1f, 1f));
        game.getFont().draw(game.getBatch(), "Press ENTER to start", 520f, 176f);
        game.getFont().draw(game.getBatch(), "Press S for Settings / Controls", 492f, 136f);
        game.getFont().setColor(Color.WHITE);
    }

    private void drawSettingsTab() {
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "SETTINGS / CONTROLS", 486f, 650f);
        game.getFont().draw(game.getBatch(), "Controls are kept here, not on the in-game HUD.", 60f, 604f);
        game.getFont().draw(game.getBatch(), "Movement: WASD", 84f, 548f);
        game.getFont().draw(game.getBatch(), "Aim / Shoot: Left mouse button", 84f, 516f);
        game.getFont().draw(game.getBatch(), "Dash tool: SPACE", 84f, 484f);
        game.getFont().draw(game.getBatch(), "Blue Shield: E (5s full immunity)", 84f, 452f);
        game.getFont().draw(game.getBatch(), "Chrono Elixir: Q (reduces slow/frozen/reverse penalties)", 84f, 420f);
        game.getFont().draw(game.getBatch(), "Ultra hero form: R", 84f, 388f);
        game.getFont().draw(game.getBatch(), "Checkpoint Rewind: C", 84f, 356f);
        game.getFont().draw(game.getBatch(), "Pause / Continue: ESC", 84f, 324f);
        game.getFont().draw(game.getBatch(), "In-round settings: ESC, then press S", 84f, 292f);
        game.getFont().draw(game.getBatch(), "No key switches timeline states; levels lock their own effect automatically.", 84f, 238f);

        game.getFont().setColor(new Color(0.55f, 0.95f, 1f, 1f));
        game.getFont().draw(game.getBatch(), "Press S or ESC to return", 500f, 136f);
        game.getFont().setColor(Color.WHITE);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
