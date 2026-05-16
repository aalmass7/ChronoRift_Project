package com.chronorift.game.time;

import com.badlogic.gdx.graphics.Color;

public class ZoneTimeState implements TimeState{
    private final String name;
    private final String description;
    private final float playerSpeedMultiplier;
    private final float enemySpeedMultiplier;
    private final float projectileSpeedMultiplier;
    private final boolean reverseControls;
    private final Color accentColor;
    private final TimeStateType effectType;

    public ZoneTimeState(String name, String description, float playerSpeedMultiplier,
                         float enemySpeedMultiplier, float projectileSpeedMultiplier,
                         boolean reverseControls, Color accentColor) {
        this(name, description, playerSpeedMultiplier, enemySpeedMultiplier, projectileSpeedMultiplier,
            reverseControls, accentColor, TimeStateType.NORMAL);
    }

    public ZoneTimeState(String name, String description, float playerSpeedMultiplier,
                         float enemySpeedMultiplier, float projectileSpeedMultiplier,
                         boolean reverseControls, Color accentColor, TimeStateType effectType) {
        this.name = name;
        this.description = description;
        this.playerSpeedMultiplier = playerSpeedMultiplier;
        this.enemySpeedMultiplier = enemySpeedMultiplier;
        this.projectileSpeedMultiplier = projectileSpeedMultiplier;
        this.reverseControls = reverseControls;
        this.accentColor = new Color(accentColor);
        this.effectType = effectType == null ? TimeStateType.NORMAL : effectType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public float playerSpeedMultiplier() {
        return playerSpeedMultiplier;
    }

    @Override
    public float enemySpeedMultiplier() {
        return enemySpeedMultiplier;
    }

    @Override
    public float projectileSpeedMultiplier() {
        return projectileSpeedMultiplier;
    }

    @Override
    public boolean reverseControls() {
        return reverseControls;
    }

    @Override
    public Color accentColor() {
        return new Color(accentColor);
    }

    @Override
    public TimeStateType effectType() {
        return effectType;
    }
}
