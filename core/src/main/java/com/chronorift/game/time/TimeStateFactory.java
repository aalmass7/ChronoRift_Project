package com.chronorift.game.time;

import java.util.ArrayList;
import java.util.List;

public class TimeStateFactory {

    private TimeStateFactory() {
    }

    public static List<TimeState> createStates() {
        List<TimeState> states = new ArrayList<>();
        states.add(new NormalTimeState());
        states.add(new SlowMotionState());
        states.add(new FrozenTimeState());
        states.add(new ReversedTimeState());
        return states;
    }
}
