package com.chronorift.game.level;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private final List<TimelineType> order = new ArrayList<>();
    private int index;

    public LevelManager() {
        order.add(TimelineType.STREET);
        order.add(TimelineType.MEDIEVAL);
        order.add(TimelineType.FROZEN);
        order.add(TimelineType.CYBER);
        order.add(TimelineType.DESERT);
    }

    public TimelineType current() {
        return order.get(index);
    }

    public boolean hasNext() {
        return index < order.size() - 1;
    }

    public void next() {
        if (hasNext()) {
            index++;
        }
    }

    public int number() {
        return index + 1;
    }

    public int total() {
        return order.size();
    }
}