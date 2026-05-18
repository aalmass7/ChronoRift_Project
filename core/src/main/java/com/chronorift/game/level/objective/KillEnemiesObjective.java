package com.chronorift.game.level.objective;

import com.chronorift.game.world.GameWorld;

public class KillEnemiesObjective implements ObjectiveComponent {
    private final int required;

    public KillEnemiesObjective(int required) {
        this.required = required;
    }

    @Override
    public boolean isComplete(GameWorld world) {
        return world.getKillsThisLevel() >= required;
    }

    @Override
    public String describe(GameWorld world) {
        return "Clear anomalies: " + Math.min(world.getKillsThisLevel(), required) + "/" + required;
    }
}
