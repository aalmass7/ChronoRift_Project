package com.chronorift.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.core.GameAssets;
import com.chronorift.game.level.TimelineType;
import com.chronorift.game.world.GameWorld;

public class Enemy extends LivingEntity {
    private final String name;
    private final boolean boss;
    private final TimelineType nativeTimeline;

    private float moveSpeed;
    private float contactDamage;
    private float rangedDamage;

    public Enemy(Vector2 position,
                 float radius,
                 float maxHealth,
                 float armor,
                 String name,
                 boolean boss,
                 float moveSpeed,
                 float contactDamage,
                 float rangedDamage,
                 TimelineType nativeTimeline) {
        super(position, radius, maxHealth, armor);
        this.name = name;
        this.boss = boss;
        this.moveSpeed = moveSpeed;
        this.contactDamage = contactDamage;
        this.rangedDamage = rangedDamage;
        this.nativeTimeline = nativeTimeline;
    }

    public void update(float delta, Player player, GameWorld world) {
    }

    public void tryContactAttack(Player player, GameWorld world, float delta) {
    }

    public void tryShoot(Vector2 target, GameWorld world, float delta) {
    }

    @Override
    public void render(SpriteBatch batch, GameAssets assets) {
    }

    public String getName() {
        return name;
    }

    public boolean isBoss() {
        return boss;
    }

    public boolean isBerserk() {
        return false;
    }

    public TimelineType getNativeTimeline() {
        return nativeTimeline;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void boostMoveSpeed(float multiplier) {
        moveSpeed *= multiplier;
    }

    public float getContactDamage() {
        return contactDamage;
    }

    public float getRangedDamage() {
        return rangedDamage;
    }
}
