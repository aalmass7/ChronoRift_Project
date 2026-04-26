package com.chronorift.game.time;

public interface TimeState {
    String getName();

    float getPlayerSpeedMultiplier();

    float getEnemySpeedMultiplier();

    float getProjectileSpeedMultiplier();

    float getCooldownMultiplier();

    default boolean isMovementReversed() {
        return false;
    }

    default boolean isInputDelayed() {
        return false;
    }
}
