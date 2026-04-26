package com.chronorift.game.time;

public class NormalTimeState implements TimeState{
    @Override
    public String getName() {
        return "Normal Time";
    }

    @Override
    public float getPlayerSpeedMultiplier() {
        return 1.0f;
    }

    @Override
    public float getEnemySpeedMultiplier() {
        return 1.0f;
    }

    @Override
    public float getProjectileSpeedMultiplier() {
        return 1.0f;
    }

    @Override
    public float getCooldownMultiplier() {
        return 1.0f;
    }
}
