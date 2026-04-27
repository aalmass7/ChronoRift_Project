package com.chronorift.game.time;

public class TimeStateFactory {

    private TimeStateFactory() {
    }

    public static TimeState create(TimeStateType type) {
        if (type == null) {
            throw new IllegalArgumentException("TimeStateType cannot be null");
        }

        return switch (type) {
            case NORMAL -> new NormalTimeState();
            case SLOW_MOTION -> new SlowMotionState();
            case FROZEN -> new FrozenTimeState();
            case REVERSED -> new ReversedTimeState();
        };
    }
}
