package com.chronorift.game.builder;

import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.entity.Player;

public class PlayerBuilder {
    private Vector2 position = new Vector2(640f, 360f);
    private String spritePath = "game/player.png";
    private String ultimateSpritePath = "game/player_ultimate.png";
    private float health = 180f;
    private float armor = 6f;
    private float speed = 250f;
    private float damage = 24f;

    public PlayerBuilder position(Vector2 position) {
        this.position = position;
        return this;
    }

    public PlayerBuilder health(float health) {
        this.health = health;
        return this;
    }

    public PlayerBuilder armor(float armor) {
        this.armor = armor;
        return this;
    }

    public PlayerBuilder speed(float speed) {
        this.speed = speed;
        return this;
    }

    public PlayerBuilder damage(float damage) {
        this.damage = damage;
        return this;
    }

    public Player build() {
        return new Player(position, spritePath, ultimateSpritePath, health, armor, speed, damage);
    }
}
