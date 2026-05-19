package com.chronorift.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.ai.ChargeStrategy;
import com.chronorift.game.ai.ChaseStrategy;
import com.chronorift.game.ai.EnemyStrategy;
import com.chronorift.game.ai.PatrolStrategy;
import com.chronorift.game.ai.StrafeShooterStrategy;
import com.chronorift.game.animation.AnimatedSprite;
import com.chronorift.game.animation.AnimationState;
import com.chronorift.game.core.GameAssets;
import com.chronorift.game.level.TimelineType;
import com.chronorift.game.world.GameWorld;

public class Enemy extends LivingEntity {
    private final String name;
    private final String spritePath;
    private final String berserkSpritePath;
    private final EnemyStrategy baseStrategy;
    private final EnemyStrategy chaseStrategy = new ChaseStrategy();
    private final EnemyStrategy chargeStrategy = new ChargeStrategy();
    private final EnemyStrategy strafeStrategy = new StrafeShooterStrategy();
    private final EnemyStrategy patrolStrategy = new PatrolStrategy();
    private final boolean boss;
    private final BossAbilityType abilityType;
    private final TimelineType nativeTimeline;
    private final Vector2 previousPosition = new Vector2();

    private EnemyStrategy activeStrategy;
    private AnimationState animationState = AnimationState.IDLE;
    private boolean berserk;
    private boolean facingRight = true;
    private float animationTime;
    private float actionLockTimer;
    private float moveSpeed;
    private float contactDamage;
    private float rangedDamage;
    private float shootCooldown;
    private float abilityCooldown = 2.4f;

    public Enemy(Vector2 position, float radius, float maxHealth, float armor, String name, String spritePath,
                 EnemyStrategy strategy, boolean boss, float moveSpeed, float contactDamage, float rangedDamage) {
        this(position, radius, maxHealth, armor, name, spritePath, null, strategy, boss, moveSpeed, contactDamage,
            rangedDamage, BossAbilityType.NONE, null);
    }

    public Enemy(Vector2 position, float radius, float maxHealth, float armor, String name, String spritePath,
                 String berserkSpritePath, EnemyStrategy strategy, boolean boss, float moveSpeed, float contactDamage,
                 float rangedDamage, BossAbilityType abilityType) {
        this(position, radius, maxHealth, armor, name, spritePath, berserkSpritePath, strategy, boss, moveSpeed,
            contactDamage, rangedDamage, abilityType, null);
    }

    public Enemy(Vector2 position, float radius, float maxHealth, float armor, String name, String spritePath,
                 String berserkSpritePath, EnemyStrategy strategy, boolean boss, float moveSpeed, float contactDamage,
                 float rangedDamage, BossAbilityType abilityType, TimelineType nativeTimeline) {
        super(position, radius, maxHealth, armor);
        this.name = name;
        this.spritePath = spritePath;
        this.berserkSpritePath = berserkSpritePath;
        this.baseStrategy = strategy;
        this.activeStrategy = strategy;
        this.boss = boss;
        this.moveSpeed = moveSpeed;
        this.contactDamage = contactDamage;
        this.rangedDamage = rangedDamage;
        this.abilityType = abilityType;
        this.nativeTimeline = nativeTimeline;
    }

    public void update(float delta, Player player, GameWorld world) {
        previousPosition.set(position);
        animationTime += delta;
        actionLockTimer = Math.max(0f, actionLockTimer - delta);
        shootCooldown = Math.max(0f, shootCooldown - delta);
        abilityCooldown = Math.max(0f, abilityCooldown - delta);
        activateBerserkIfNeeded(world);
        adaptStrategyToCondition(player);
        activeStrategy.update(this, player, world, delta);
        updateFacing(player);
        updateBerserkUltimate(player, world);

        if (actionLockTimer <= 0f) {
            boolean moved = previousPosition.dst2(position) > 1f;
            setAnimationState(moved ? AnimationState.WALK : AnimationState.IDLE);
        }
    }

    private void adaptStrategyToCondition(Player player) {
        float distance2 = position.dst2(player.getPosition());
        EnemyStrategy next = baseStrategy;

        if (health <= maxHealth * 0.28f || (boss && berserk)) {
            next = chargeStrategy;
        } else if (baseStrategy instanceof PatrolStrategy) {
            next = distance2 <= 300f * 300f ? chaseStrategy : patrolStrategy;
        } else if (rangedDamage > 0f) {
            if (distance2 < 135f * 135f) {
                next = chaseStrategy;
            } else {
                next = strafeStrategy;
            }
        } else if (distance2 > 320f * 320f) {
            next = chaseStrategy;
        }

        activeStrategy = next;
    }

    private void updateFacing(Player player) {
        float movementX = position.x - previousPosition.x;
        if (Math.abs(movementX) > 0.2f) {
            facingRight = movementX > 0f;
            return;
        }
        float toPlayerX = player.getPosition().x - position.x;
        if (Math.abs(toPlayerX) > 4f) {
            facingRight = toPlayerX > 0f;
        }
    }

    private void activateBerserkIfNeeded(GameWorld world) {
        if (!boss || berserk || health > maxHealth * 0.3f) {
            return;
        }
        berserk = true;
        radius *= 1.18f;
        moveSpeed *= 1.38f;
        contactDamage *= 1.45f;
        rangedDamage *= 1.35f;
        abilityCooldown = 0.4f;
        lockAnimation(AnimationState.ULTIMATE, 0.65f);
        world.postLog(name + " entered BERSERK mode! Ultimate unlocked.");
    }

    private void updateBerserkUltimate(Player player, GameWorld world) {
        if (!boss || !berserk || abilityType == BossAbilityType.NONE || abilityCooldown > 0f) {
            return;
        }
        lockAnimation(AnimationState.ULTIMATE, 0.55f);
        switch (abilityType) {
            case REACTOR_BARRAGE -> {
                fireRadial(world, 12, 270f, 19f, 8f, new Color(0.95f, 0.1f, 0.2f, 1f));
                fireTargeted(player, world, 420f, 26f, 11f, new Color(0.25f, 0.95f, 1f, 1f));
                world.postLog("Reactor Barrage: dodge the red-cyan projectile ring.");
                abilityCooldown = 2.8f;
            }
            case FROST_NOVA -> {
                fireRadial(world, 14, 230f, 17f, 9f, new Color(0.45f, 0.85f, 1f, 1f));
                world.postLog("Frost Nova: ice shards explode from the boss.");
                abilityCooldown = 3.15f;
            }
            case SANDS_OF_RAGE -> {
                fireRadial(world, 10, 250f, 20f, 10f, new Color(1f, 0.66f, 0.12f, 1f));
                if (position.dst2(player.getPosition()) < 230f * 230f) {
                    world.applyDamage(this, player, 18f * world.getEffectiveEnemyDamageMultiplier(this));
                }
                world.postLog("Sands of Rage: the hourglass releases a time quake.");
                abilityCooldown = 3.0f;
            }
            case NONE -> {
                // No special ability.
            }
        }
    }

    private void fireRadial(GameWorld world, int count, float speed, float damage, float radius, Color color) {
        float offset = MathUtils.random(0f, 360f / count);
        float effectiveDamage = damage * world.getEffectiveEnemyDamageMultiplier(this);
        for (int i = 0; i < count; i++) {
            float angle = offset + i * (360f / count);
            Vector2 velocity = new Vector2(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle)).scl(speed);
            world.spawnProjectile(new Projectile(new Vector2(position), velocity, radius, effectiveDamage, ProjectileOwner.ENEMY,
                color));
        }
    }

    private void fireTargeted(Player player, GameWorld world, float speed, float damage, float radius, Color color) {
        Vector2 velocity = new Vector2(player.getPosition()).sub(position);
        if (velocity.isZero()) {
            return;
        }
        velocity.nor().scl(speed);
        world.spawnProjectile(new Projectile(new Vector2(position), velocity, radius,
            damage * world.getEffectiveEnemyDamageMultiplier(this), ProjectileOwner.ENEMY, color));
    }

    public void tryShoot(Vector2 target, GameWorld world, float delta) {
        if (rangedDamage <= 0f || shootCooldown > 0f) {
            return;
        }
        Vector2 direction = new Vector2(target).sub(position);
        if (direction.isZero()) {
            return;
        }
        Vector2 velocity = direction.nor().scl(320f * world.getTimeContext().current().projectileSpeedMultiplier());
        world.spawnProjectile(new Projectile(new Vector2(position), velocity, 7f,
            rangedDamage * world.getEffectiveEnemyDamageMultiplier(this), ProjectileOwner.ENEMY,
            boss ? new Color(1f, 0.75f, 0.2f, 1f) : new Color(0.35f, 0.95f, 0.45f, 1f)));
        shootCooldown = boss ? (berserk ? 0.62f : 0.85f) : 1.2f;
        lockAnimation(AnimationState.ATTACK, boss ? 0.32f : 0.24f);
    }

    public void tryContactAttack(Player player, GameWorld world, float delta) {
        if (position.dst2(player.getPosition()) <= (radius + player.getRadius()) * (radius + player.getRadius())) {
            world.applyDamage(this, player, contactDamage * world.getEffectiveEnemyDamageMultiplier(this) * delta * 2.5f);
            lockAnimation(AnimationState.ATTACK, 0.18f);
        }
    }

    @Override
    public void damage(float amount) {
        float before = health;
        super.damage(amount);
        if (health < before && isAlive()) {
            lockAnimation(AnimationState.HURT, boss ? 0.28f : 0.2f);
        }
    }

    public String getName() {
        return name;
    }

    public boolean isBoss() {
        return boss;
    }

    public boolean isBerserk() {
        return berserk;
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

    private void lockAnimation(AnimationState state, float duration) {
        setAnimationState(state);
        actionLockTimer = Math.max(actionLockTimer, duration);
    }

    private void setAnimationState(AnimationState state) {
        if (animationState != state) {
            animationState = state;
            animationTime = 0f;
        }
    }

    @Override
    public void render(SpriteBatch batch, GameAssets assets) {
        float size = boss ? (berserk ? 240f : 200f) : 112f;
        String currentSprite = berserk && berserkSpritePath != null ? berserkSpritePath : spritePath;
        AnimatedSprite animatedSprite = assets.animation(spritePath);
        if (animatedSprite != null) {
            TextureRegion frame = animatedSprite.frame(animationState, berserk, animationTime);
            drawAnimatedFrame(batch, frame, size);
        } else {
            batch.draw(assets.texture(currentSprite), position.x - size / 2f, position.y - size / 2f, size, size);
        }
        if (berserk) {
            Color old = batch.getColor().cpy();
            batch.setColor(new Color(1f, 0.1f, 0.05f, 0.20f));
            batch.draw(assets.pixel(), position.x - size * 0.42f, position.y - size * 0.42f, size * 0.84f, size * 0.84f);
            batch.setColor(old);
        }
    }

    private void drawAnimatedFrame(SpriteBatch batch, TextureRegion frame, float size) {
        float stepWave = MathUtils.sin(animationTime * (boss ? 7f : 10f));
        float bob = 0f;
        float scaleY = 1f;
        float rotation = 0f;
        if (animationState == AnimationState.WALK || animationState == AnimationState.RUN) {
            bob = Math.abs(stepWave) * (boss ? 3.5f : 4.5f);
            scaleY = 1f + Math.abs(stepWave) * 0.035f;
            rotation = stepWave * (boss ? 1.4f : 2.4f);
        } else if (animationState == AnimationState.ATTACK || animationState == AnimationState.ULTIMATE) {
            rotation = facingRight ? -2.6f : 2.6f;
            scaleY = 1.025f;
        } else if (animationState == AnimationState.HURT) {
            rotation = facingRight ? 4f : -4f;
        }
        float scaleX = facingRight ? 1f : -1f;
        batch.draw(frame, position.x - size / 2f, position.y - size / 2f + bob,
            size / 2f, size / 2f, size, size, scaleX, scaleY, rotation);
    }
}
