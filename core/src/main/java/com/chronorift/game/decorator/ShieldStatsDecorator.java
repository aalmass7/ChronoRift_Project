package com.chronorift.game.decorator;

public class ShieldStatsDecorator extends PlayerStatsDecorator {
    public ShieldStatsDecorator(PlayerStats wrapped) {
        super(wrapped);
    }

    @Override
    public float resistance() {
        return wrapped.resistance() + 0.35f;
    }
}
