package com.chronorift.game.time;

import com.badlogic.gdx.graphics.Color;

public class FrozenTimeState implements TimeState{
    @Override
    public String getName() {
        return "Frozen Timeline";
    }

    @Override
    public String getDescription() {
        return "Temporal frost strongly restricts the outsider, while native enemies remain dangerous.";
    }

    @Override
    public float playerSpeedMultiplier() {
        return 0.50f;
    }

    @Override
    public float enemySpeedMultiplier() {
        return 1.05f;
    }

    @Override
    public float projectileSpeedMultiplier() {
        return 0.62f;
    }

    @Override
    public boolean reverseControls() {
        return false;
    }

    @Override
    public Color accentColor() {
        return new Color(0.65f, 0.8f, 1f, 1f);
    }

    @Override
    public TimeStateType effectType() {
        return TimeStateType.FROZEN;
    }
}
