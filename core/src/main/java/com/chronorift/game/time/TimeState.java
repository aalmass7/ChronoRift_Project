package com.chronorift.game.time;

import com.badlogic.gdx.graphics.Color;

public interface TimeState {
    String getName();

    String getDescription();

    float playerSpeedMultiplier();

    float enemySpeedMultiplier();

    float projectileSpeedMultiplier();

    boolean reverseControls();

    Color accentColor();

    default TimeStateType effectType() {
        return TimeStateType.NORMAL;
    }

    default void update(float delta) {
        // Static time states do not need per-frame updates.
    }
}
