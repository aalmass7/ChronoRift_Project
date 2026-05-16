package com.chronorift.game.decorator;

public class BasePlayerStats implements PlayerStats {
    private final float moveSpeed;

    public BasePlayerStats(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public float moveSpeed() {
        return moveSpeed;
    }

    @Override
    public float damageMultiplier() {
        return 1f;
    }

    @Override
    public float resistance() {
        return 0f;
    }
}
