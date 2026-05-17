package com.chronorift.game.event;

import java.util.ArrayList;
import java.util.List;

public class GameEventBus {
    private final List<GameEventListener> listeners = new ArrayList<>();

    public void register(GameEventListener listener) {
        listeners.add(listener);
    }

    public void post(GameEvent event) {
        for (GameEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}