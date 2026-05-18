package com.chronorift.game.level.objective;

import com.chronorift.game.world.GameWorld;

public class BossObjective implements ObjectiveComponent {
    @Override
    public boolean isComplete(GameWorld world) {
        return world.isBossDefeated();
    }

    @Override
    public String describe(GameWorld world) {
        return "Defeat boss: " + (world.isBossDefeated() ? "DONE" : "PENDING");
    }
}
