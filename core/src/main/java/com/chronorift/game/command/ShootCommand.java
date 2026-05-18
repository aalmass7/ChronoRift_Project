package com.chronorift.game.command;

import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.world.GameWorld;

public class ShootCommand implements GameCommand {
    private final Vector2 target;

    public ShootCommand(Vector2 target) {
        this.target = target;
    }

    @Override
    public void execute(GameWorld world, float delta) {
        world.playerShoot(target);
    }
}
