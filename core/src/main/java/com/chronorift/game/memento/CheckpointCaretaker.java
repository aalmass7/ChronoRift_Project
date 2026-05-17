package com.chronorift.game.memento;

public class CheckpointCaretaker {
    private PlayerMemento snapshot;

    public void save(PlayerMemento snapshot) {
        this.snapshot = snapshot;
    }

    public PlayerMemento getSnapshot() {
        return snapshot;
    }
}
