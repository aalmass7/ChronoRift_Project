package com.chronorift.game.command;

import com.chronorift.game.world.GameWorld;

public class DashCommand implements GameCommand {
    @Override
    public void execute(GameWorld world, float delta) {
        world.dashPlayer();
    }
}
