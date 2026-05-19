package com.chronorift.game.factory;

import com.chronorift.game.level.TimelineType;

public final class TimelineFactoryProvider {
    private TimelineFactoryProvider() {
    }

    public static TimelineFactory create(TimelineType type) {
        return switch (type) {
            case STREET -> new StreetTimelineFactory();
            case MEDIEVAL -> new MedievalTimelineFactory();
            case CYBER -> new CyberTimelineFactory();
            case FROZEN -> new FrozenTimelineFactory();
            case DESERT -> new DesertTimelineFactory();
        };
    }
}
