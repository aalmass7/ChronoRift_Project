package com.chronorift.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.core.GameAssets;

public abstract class Entity {
    protected final Vector2 position;
    protected float radius;

    protected Entity(Vector2 position, float radius) {
        this.position = position;
        this.radius = radius;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRadius() {
        return radius;
    }

    public abstract void render(SpriteBatch batch, GameAssets assets);
}
