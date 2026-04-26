package com.chronorift.game.time;

public abstract class AbstractTimeState implements TimeState {

    private final String name;
    private final float playerSpeedMultiplier;
    private final float enemySpeedMultiplier;
    private final float projectileSpeedMultiplier;
    private final float cooldownMultiplier;

    protected AbstractTimeState(
        String name,
        float playerSpeedMultiplier,
        float enemySpeedMultiplier,
        float projectileSpeedMultiplier,
        float cooldownMultiplier
    ) {
        this.name = name;
        this.playerSpeedMultiplier = playerSpeedMultiplier;
        this.enemySpeedMultiplier = enemySpeedMultiplier;
        this.projectileSpeedMultiplier = projectileSpeedMultiplier;
        this.cooldownMultiplier = cooldownMultiplier;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getPlayerSpeedMultiplier() {
        return playerSpeedMultiplier;
    }

    @Override
    public float getEnemySpeedMultiplier() {
        return enemySpeedMultiplier;
    }

    @Override
    public float getProjectileSpeedMultiplier() {
        return projectileSpeedMultiplier;
    }

    @Override
    public float getCooldownMultiplier() {
        return cooldownMultiplier;
    }

    @Override
    public boolean isMovementReversed() {
        return false;
    }

    @Override
    public boolean isInputDelayed() {
        return false;
    }
}
