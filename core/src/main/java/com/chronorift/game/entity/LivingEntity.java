package com.chronorift.game.entity;

import com.badlogic.gdx.math.Vector2;

public abstract class LivingEntity extends Entity {
    protected float maxHealth;
    protected float health;
    protected float armor;

    protected LivingEntity(Vector2 position, float radius, float maxHealth, float armor) {
        super(position, radius);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.armor = armor;
    }

    public void damage(float amount) {
        health -= amount;
    }

    public void heal(float amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public boolean isAlive() {
        return health > 0f;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getArmor() {
        return armor;
    }

    public void setHealth(float health) {
        this.health = Math.min(maxHealth, Math.max(0f, health));
    }
}
