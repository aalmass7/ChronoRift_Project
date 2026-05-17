package com.chronorift.game.memento;

public class PlayerMemento {
    private final float health;
    private final float maxHealth;

    public PlayerMemento(float health, float maxHealth) {
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }
}
