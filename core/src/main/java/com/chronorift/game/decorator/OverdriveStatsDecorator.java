package com.chronorift.game.decorator;

public class OverdriveStatsDecorator extends PlayerStatsDecorator {
    public OverdriveStatsDecorator(PlayerStats wrapped) {
        super(wrapped);
    }

    @Override
    public float moveSpeed() {
        return wrapped.moveSpeed() + 45f;
    }

    @Override
    public float damageMultiplier() {
        return wrapped.damageMultiplier() + 0.75f;
    }

    @Override
    public float resistance() {
        return wrapped.resistance() + 0.18f;
    }
}
