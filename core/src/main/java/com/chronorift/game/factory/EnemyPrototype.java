package com.chronorift.game.factory;

import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.ai.ChargeStrategy;
import com.chronorift.game.ai.ChaseStrategy;
import com.chronorift.game.ai.EnemyStrategy;
import com.chronorift.game.ai.StrafeShooterStrategy;
import com.chronorift.game.ai.PatrolStrategy;
import com.chronorift.game.entity.BossAbilityType;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.level.TimelineType;

public class EnemyPrototype {
    private final float radius;
    private final float maxHealth;
    private final float armor;
    private final String name;
    private final String spritePath;
    private final String berserkSpritePath;
    private final EnemyStrategy strategy;
    private final boolean boss;
    private final float moveSpeed;
    private final float contactDamage;
    private final float rangedDamage;
    private final BossAbilityType abilityType;
    private TimelineType nativeTimeline;

    public EnemyPrototype(float radius, float maxHealth, float armor, String name, String spritePath,
                          EnemyStrategy strategy, boolean boss, float moveSpeed, float contactDamage, float rangedDamage) {
        this(radius, maxHealth, armor, name, spritePath, null, strategy, boss, moveSpeed, contactDamage, rangedDamage,
            BossAbilityType.NONE);
    }

    public EnemyPrototype(float radius, float maxHealth, float armor, String name, String spritePath,
                          String berserkSpritePath, EnemyStrategy strategy, boolean boss, float moveSpeed,
                          float contactDamage, float rangedDamage, BossAbilityType abilityType) {
        this.radius = radius;
        this.maxHealth = maxHealth;
        this.armor = armor;
        this.name = name;
        this.spritePath = spritePath;
        this.berserkSpritePath = berserkSpritePath;
        this.strategy = strategy;
        this.boss = boss;
        this.moveSpeed = moveSpeed;
        this.contactDamage = contactDamage;
        this.rangedDamage = rangedDamage;
        this.abilityType = abilityType;
    }

    public EnemyPrototype withNativeTimeline(TimelineType nativeTimeline) {
        this.nativeTimeline = nativeTimeline;
        return this;
    }

    public Enemy spawn(Vector2 position, float healthMultiplier, float damageMultiplier) {
        return new Enemy(new Vector2(position), radius, maxHealth * healthMultiplier, armor, name, spritePath,
            berserkSpritePath, copyStrategy(), boss, moveSpeed, contactDamage * damageMultiplier,
            rangedDamage * damageMultiplier, abilityType, nativeTimeline);
    }

    private EnemyStrategy copyStrategy() {
        if (strategy instanceof ChaseStrategy) {
            return new ChaseStrategy();
        }
        if (strategy instanceof ChargeStrategy) {
            return new ChargeStrategy();
        }
        if (strategy instanceof StrafeShooterStrategy) {
            return new StrafeShooterStrategy();
        }
        if (strategy instanceof PatrolStrategy) {
            return new PatrolStrategy();
        }
        return strategy;
    }
}
