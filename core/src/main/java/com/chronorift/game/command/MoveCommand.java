package com.chronorift.game.command;

import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.world.GameWorld;

public class MoveCommand implements GameCommand {
    private final Vector2 direction;

    public MoveCommand(Vector2 direction) {
        this.direction = direction;
    }

    @Override
    public void execute(GameWorld world, float delta) {
        world.movePlayer(direction);
    }
}
