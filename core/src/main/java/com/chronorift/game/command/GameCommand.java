package com.chronorift.game.command;

import com.chronorift.game.world.GameWorld;

public interface GameCommand {
    void execute(GameWorld world, float delta);
}
