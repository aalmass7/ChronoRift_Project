package com.chronorift.game.time;

import com.badlogic.gdx.graphics.Color;

public class ReversedTimeState implements TimeState{
    @Override
    public String getName() {
        return "Reversed Timeline";
    }

    @Override
    public String getDescription() {
        return "The zone reverses outsider controls; native enemies are adapted to the warped logic.";
    }

    @Override
    public float playerSpeedMultiplier() {
        return 0.95f;
    }

    @Override
    public float enemySpeedMultiplier() {
        return 1.15f;
    }

    @Override
    public float projectileSpeedMultiplier() {
        return 1.05f;
    }

    @Override
    public boolean reverseControls() {
        return true;
    }

    @Override
    public Color accentColor() {
        return new Color(1f, 0.74f, 0.4f, 1f);
    }

    @Override
    public TimeStateType effectType() {
        return TimeStateType.REVERSED;
    }
}
