package com.chronorift.game.time;

import com.chronorift.game.event.GameEvent;
import com.chronorift.game.event.GameEventBus;
import com.chronorift.game.event.GameEventType;
import com.chronorift.game.tool.ChronoToolbelt;

import java.util.List;

public class TimeContext {
    private final List<TimeState> states;
    private final GameEventBus eventBus;
    private int index;
    private TimeState zoneLockedState;

    public TimeContext(GameEventBus eventBus) {
        this.eventBus = eventBus;
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
        eventBus.post(new GameEvent(
            GameEventType.TIME_STATE_CHANGED,
            "Zone active: " + current().getName() + " - " + current().getDescription(),
            current()));
    }

    public void clearZoneLock() {
        zoneLockedState = null;
    }


    public float playerSpeedMultiplier(ChronoToolbelt toolbelt) {
        TimeState state = current();
        float multiplier = state.playerSpeedMultiplier();
        if (toolbelt.chronoElixirActive()) {
            if (state.effectType() == TimeStateType.FROZEN) {
                multiplier = softenPenalty(multiplier, 0.82f);
            } else if (state.effectType() == TimeStateType.SLOW_MOTION
                || state.effectType() == TimeStateType.REVERSED
                || state.effectType() == TimeStateType.BOSS) {
                multiplier = softenPenalty(multiplier, 0.72f);
            } else {
                multiplier = softenPenalty(multiplier, 0.62f);
            }
        }
        if (toolbelt.antiFreezeActive() && state.effectType() == TimeStateType.FROZEN) {
            multiplier = softenPenalty(multiplier, 0.82f);
        }
        return multiplier;
    }

    public float playerProjectileMultiplier(ChronoToolbelt toolbelt) {
        TimeState state = current();
        float multiplier = state.projectileSpeedMultiplier();
        if (toolbelt.chronoElixirActive()) {
            if (state.effectType() == TimeStateType.FROZEN) {
                multiplier = softenPenalty(multiplier, 0.78f);
            } else if (state.effectType() == TimeStateType.SLOW_MOTION
                || state.effectType() == TimeStateType.REVERSED
                || state.effectType() == TimeStateType.BOSS) {
                multiplier = softenPenalty(multiplier, 0.65f);
            } else {
                multiplier = softenPenalty(multiplier, 0.55f);
            }
        }
        if (toolbelt.antiFreezeActive() && state.effectType() == TimeStateType.FROZEN) {
            multiplier = softenPenalty(multiplier, 0.78f);
        }
        return multiplier;
    }

    public boolean reverseControls(ChronoToolbelt toolbelt) {
        if (!current().reverseControls()) {
            return false;
        }
        return !toolbelt.chronoElixirActive();
    }

    private float softenPenalty(float multiplier, float strength) {
        if (multiplier >= 1f) {
            return multiplier;
        }
        return multiplier + (1f - multiplier) * strength;
    }
}
