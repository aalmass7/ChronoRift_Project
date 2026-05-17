package com.chronorift.game.event;

import com.chronorift.game.core.GameConfig;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class HudLog implements GameEventListener {
    private final Deque<String> lines = new ArrayDeque<>();

    @Override
    public void onEvent(GameEvent event) {
        if (event.getMessage() == null || event.getMessage().isEmpty()) {
            return;
        }
        lines.addFirst(event.getMessage());
        while (lines.size() > GameConfig.MAX_LOG_LINES) {
            lines.removeLast();
        }
    }

    public List<String> getLines() {
        return new ArrayList<>(lines);
    }
}