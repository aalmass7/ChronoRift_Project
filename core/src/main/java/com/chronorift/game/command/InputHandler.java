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
        commands.add(new MoveCommand(direction));

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector2 target = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            commands.add(new ShootCommand(target));
        }

        boolean q = Gdx.input.isKeyPressed(Input.Keys.Q);
        if (q && !chronoElixirPressed) commands.add(new ChronoElixirCommand());
        chronoElixirPressed = q;

        boolean e = Gdx.input.isKeyPressed(Input.Keys.E);
        if (e && !shieldPressed) commands.add(new ShieldCommand());
        shieldPressed = e;

        boolean r = Gdx.input.isKeyPressed(Input.Keys.R);
        if (r && !overdrivePressed) commands.add(new OverdriveCommand());
        overdrivePressed = r;

        boolean c = Gdx.input.isKeyPressed(Input.Keys.C);
        if (c && !rewindPressed) commands.add(new RewindCommand());
        rewindPressed = c;

        boolean space = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        if (space && !dashPressed) commands.add(new DashCommand());
        dashPressed = space;

        return commands;
    }
}
