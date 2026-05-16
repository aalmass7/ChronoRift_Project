package com.chronorift.game.decorator;

public abstract class PlayerStatsDecorator implements PlayerStats {
    protected final PlayerStats wrapped;

    protected PlayerStatsDecorator(PlayerStats wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public float moveSpeed() {
        return wrapped.moveSpeed();
    }

    @Override
    public float damageMultiplier() {
        return wrapped.damageMultiplier();
    }

    @Override
    public float resistance() {
        return wrapped.resistance();
    }
}
