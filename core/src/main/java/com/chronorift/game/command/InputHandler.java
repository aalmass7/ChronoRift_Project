package com.chronorift.game.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {
    private boolean chronoElixirPressed;
    private boolean shieldPressed;
    private boolean overdrivePressed;
    private boolean rewindPressed;
    private boolean dashPressed;

    public List<GameCommand> poll(Viewport viewport) {
        List<GameCommand> commands = new ArrayList<>();
        Vector2 direction = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) direction.y += 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) direction.y -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) direction.x -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) direction.x += 1f;

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector2 target = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        }


        return commands;
    }
}
