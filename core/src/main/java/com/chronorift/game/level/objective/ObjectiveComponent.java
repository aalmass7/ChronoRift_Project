package com.chronorift.game.level.objective;

import com.chronorift.game.world.GameWorld;

public interface ObjectiveComponent {
    boolean isComplete(GameWorld world);
    String describe(GameWorld world);
}
