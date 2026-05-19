package com.chronorift.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.core.GameAssets;

public class Projectile extends Entity {
    private final Vector2 velocity;
    private final float damage;
    private final ProjectileOwner owner;
    private final Color color;
    private float life;

    public Projectile(Vector2 position, Vector2 velocity, float radius, float damage, ProjectileOwner owner, Color color) {
        super(position, radius);
        this.velocity = velocity;
        this.damage = damage;
        this.owner = owner;
        this.color = new Color(color);
        this.life = 3f;
    }

    public void update(float delta) {
        position.mulAdd(velocity, delta);
        life -= delta;
    }

    public boolean isAlive() {
        return life > 0f;
    }

    public float getDamage() {
        return damage;
    }

    public ProjectileOwner getOwner() {
        return owner;
    }

    @Override
    public void render(SpriteBatch batch, GameAssets assets) {
        Color old = batch.getColor().cpy();
        batch.setColor(color);
        batch.draw(assets.pixel(), position.x - radius, position.y - radius, radius * 2f, radius * 2f);
        batch.setColor(old);
    }
}
