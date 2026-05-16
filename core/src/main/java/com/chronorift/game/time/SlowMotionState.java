package com.chronorift.game.time;

import com.badlogic.gdx.graphics.Color;

public class SlowMotionState implements TimeState{
    @Override
    public String getName() {
        return "Slow Motion Timeline";
    }

    @Override
    public String getDescription() {
        return "The zone drags the outsider while native enemies react faster.";
    }

    @Override
    public float playerSpeedMultiplier() {
        return 0.72f;
    }

    @Override
    public float enemySpeedMultiplier() {
        return 1.10f;
    }

    @Override
    public float projectileSpeedMultiplier() {
        return 0.78f;
    }

    @Override
    public boolean reverseControls() {
        return false;
    }

    @Override
    public Color accentColor() {
        return new Color(0.5f, 0.9f, 1f, 1f);
    }

    @Override
    public TimeStateType effectType() {
        return TimeStateType.SLOW_MOTION;
    }
}
