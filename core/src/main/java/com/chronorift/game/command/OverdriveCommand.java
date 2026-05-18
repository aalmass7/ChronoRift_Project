package com.chronorift.game.command;

import com.chronorift.game.world.GameWorld;

public class OverdriveCommand implements GameCommand {
    @Override
    public void execute(GameWorld world, float delta) {
        world.activateOverdrive();
    }
}
