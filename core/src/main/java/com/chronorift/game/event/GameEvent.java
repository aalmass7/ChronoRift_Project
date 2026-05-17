package com.chronorift.game.event;

public class GameEvent{
    private final GameEventType type;
    private final String message;
    private final Object payload;

    public GameEvent(GameEventType type, String message, Object payload) {
        this.type = type;
        this.message = message;
        this.payload = payload;
    }

    public GameEventType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Object getPayload() {
        return payload;
    }
}