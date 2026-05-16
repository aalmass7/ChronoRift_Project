package com.chronorift.game.time;

import java.util.List;

public class TimeContext {
    private final List<TimeState> states;
    private int index;
    private TimeState zoneLockedState;

    public TimeContext() {
        this.states = TimeStateFactory.createStates();
        this.index = 0;
    }

    public TimeState current() {
        return zoneLockedState != null ? zoneLockedState : states.get(index);
    }

    public boolean isZoneLocked() {
        return zoneLockedState != null;
    }

    public void update(float delta) {
        current().update(delta);
    }

    public void lockToZone(TimeState state) {
        zoneLockedState = state;
    }

    public void clearZoneLock() {
        zoneLockedState = null;
    }

    public float playerSpeedMultiplier() {
        return current().playerSpeedMultiplier();
    }

    public float playerProjectileMultiplier() {
        return current().projectileSpeedMultiplier();
    }

    public boolean reverseControls() {
        return current().reverseControls();
    }
}
