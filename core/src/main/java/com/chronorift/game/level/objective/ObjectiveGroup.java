package com.chronorift.game.level.objective;

import com.chronorift.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

public class ObjectiveGroup implements ObjectiveComponent {
    private final String title;
    private final List<ObjectiveComponent> children = new ArrayList<>();

    public ObjectiveGroup(String title) {
        this.title = title;
    }

    public ObjectiveGroup add(ObjectiveComponent component) {
        children.add(component);
        return this;
    }

    @Override
    public boolean isComplete(GameWorld world) {
        for (ObjectiveComponent child : children) {
            if (!child.isComplete(world)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String describe(GameWorld world) {
        StringBuilder builder = new StringBuilder(title);
        for (ObjectiveComponent child : children) {
            builder.append("\n - ").append(child.describe(world));
        }
        return builder.toString();
    }
}
