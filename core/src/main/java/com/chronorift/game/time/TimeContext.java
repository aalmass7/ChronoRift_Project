package com.chronorift.game.time;

public class TimeContext {

    private TimeState currentState;

    public TimeContext() {
        this.currentState = TimeStateFactory.create(TimeStateType.NORMAL);
    }

    public TimeState getCurrentState() {
        return currentState;
    }

    public String getCurrentStateName() {
        return currentState.getName();
    }

    public void changeState(TimeStateType type) {
        this.currentState = TimeStateFactory.create(type);
    }

    public float applyPlayerSpeed(float baseSpeed) {
        return baseSpeed * currentState.getPlayerSpeedMultiplier();
    }

    public float applyEnemySpeed(float baseSpeed) {
        return baseSpeed * currentState.getEnemySpeedMultiplier();
    }

    public float applyProjectileSpeed(float baseSpeed) {
        return baseSpeed * currentState.getProjectileSpeedMultiplier();
    }

    public float applyCooldown(float baseCooldown) {
        return baseCooldown * currentState.getCooldownMultiplier();
    }

    public boolean isMovementReversed() {
        return currentState.isMovementReversed();
    }

    public boolean isInputDelayed() {
        return currentState.isInputDelayed();
    }
}
