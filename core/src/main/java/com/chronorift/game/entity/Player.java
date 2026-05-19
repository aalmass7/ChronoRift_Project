package com.chronorift.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.animation.AnimatedSprite;
import com.chronorift.game.animation.AnimationState;
import com.chronorift.game.core.GameAssets;
import com.chronorift.game.core.GameConfig;
import com.chronorift.game.decorator.BasePlayerStats;
import com.chronorift.game.decorator.OverdriveStatsDecorator;
import com.chronorift.game.decorator.PlayerStats;
import com.chronorift.game.decorator.ShieldStatsDecorator;
import com.chronorift.game.memento.PlayerMemento;
import com.chronorift.game.time.TimeContext;
import com.chronorift.game.tool.ChronoToolbelt;
import com.chronorift.game.world.GameWorld;

public class Player extends LivingEntity {
    private final String spritePath;
    private final String ultimateSpritePath;
    private final ChronoToolbelt toolbelt;
    private final Vector2 moveIntent = new Vector2();
    private final Vector2 facing = new Vector2(1f, 0f);
    private final float baseMoveSpeed;
    private final float baseDamage;

    private AnimationState animationState = AnimationState.IDLE;
    private float animationTime;
    private float actionLockTimer;
    private float fireCooldown;
    private float dashCooldown;
    private int score;

    public Player(Vector2 position, String spritePath, String ultimateSpritePath, float maxHealth, float armor,
                  float baseMoveSpeed, float baseDamage) {
        super(position, GameConfig.PLAYER_RADIUS, maxHealth, armor);
        this.spritePath = spritePath;
        this.ultimateSpritePath = ultimateSpritePath;
        this.baseMoveSpeed = baseMoveSpeed;
        this.baseDamage = baseDamage;
        this.toolbelt = new ChronoToolbelt();
    }

    public void update(float delta, TimeContext timeContext) {
        toolbelt.update(delta);
        radius = toolbelt.overdriveActive() ? GameConfig.PLAYER_RADIUS * 1.35f : GameConfig.PLAYER_RADIUS;
        animationTime += delta;
        actionLockTimer = Math.max(0f, actionLockTimer - delta);
        fireCooldown = Math.max(0f, fireCooldown - delta);
        dashCooldown = Math.max(0f, dashCooldown - delta);

        PlayerStats stats = buildStats();
        Vector2 appliedMove = new Vector2(moveIntent);
        if (timeContext.reverseControls(toolbelt)) {
            appliedMove.scl(-1f);
        }

        boolean moving = !appliedMove.isZero();
        if (moving) {
            appliedMove.nor();
            if (Math.abs(appliedMove.x) > 0.05f) {
                facing.set(appliedMove.x, appliedMove.y);
            }
            position.mulAdd(appliedMove, stats.moveSpeed() * timeContext.playerSpeedMultiplier(toolbelt) * delta);
        }

        if (actionLockTimer <= 0f) {
            setAnimationState(moving ? (toolbelt.overdriveActive() ? AnimationState.RUN : AnimationState.WALK) : AnimationState.IDLE);
        }

        position.x = MathUtils.clamp(position.x, radius, GameConfig.WORLD_WIDTH - radius);
        position.y = MathUtils.clamp(position.y, radius, GameConfig.WORLD_HEIGHT - radius);
    }

    private PlayerStats buildStats() {
        PlayerStats stats = new BasePlayerStats(baseMoveSpeed);
        if (toolbelt.shieldActive()) {
            stats = new ShieldStatsDecorator(stats);
        }
        if (toolbelt.overdriveActive()) {
            stats = new OverdriveStatsDecorator(stats);
        }
        return stats;
    }

    public PlayerStats currentStats() {
        return buildStats();
    }

    public void setMoveIntent(Vector2 direction) {
        this.moveIntent.set(direction);
    }

    public void updateFacing(Vector2 target) {
        facing.set(target).sub(position);
        if (!facing.isZero()) {
            facing.nor();
        }
    }



    public boolean dash(TimeContext timeContext) {
        if (dashCooldown > 0f || moveIntent.isZero()) {
            return false;
        }
        Vector2 dir = new Vector2(moveIntent);
        if (timeContext.reverseControls(toolbelt)) {
            dir.scl(-1f);
        }
        dir.nor();
        position.mulAdd(dir, toolbelt.overdriveActive() ? 165f : 120f);
        dashCooldown = toolbelt.overdriveActive() ? 0.8f : 1.25f;
        lockAnimation(AnimationState.RUN, 0.18f);
        return true;
    }

    public boolean activateShield() {
        return toolbelt.activateShield();
    }


    public boolean useChronoElixir() {
        boolean used = toolbelt.useChronoElixir();
        if (used) {
            lockAnimation(AnimationState.ULTIMATE, 0.24f);
        }
        return used;
    }


    public boolean activateOverdrive() {
        boolean activated = toolbelt.activateOverdrive();
        if (activated) {
            lockAnimation(AnimationState.ULTIMATE, 0.45f);
        }
        return activated;
    }

    public ChronoToolbelt getToolbelt() {
        return toolbelt;
    }

    public PlayerMemento save() {
        return new PlayerMemento(health, maxHealth);
    }

    public boolean rewind(PlayerMemento snapshot) {
        if (snapshot == null || !toolbelt.useRewind()) {
            return false;
        }
        this.maxHealth = snapshot.getMaxHealth();
        this.health = snapshot.getHealth();
        lockAnimation(AnimationState.ULTIMATE, 0.32f);
        return true;
    }

    @Override
    public void damage(float amount) {
        float before = health;
        super.damage(amount);
        if (health < before) {
            lockAnimation(AnimationState.HURT, 0.24f);
        }
    }

    public void addScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
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
        boolean ultimate = toolbelt.overdriveActive();
        float size = ultimate ? 172f : 118f;
        AnimatedSprite animatedSprite = assets.animation(spritePath);
        if (animatedSprite != null) {
            TextureRegion frame = animatedSprite.frame(animationState, ultimate, animationTime);
            drawAnimatedFrame(batch, frame, size, ultimate);
        } else {
            batch.draw(assets.texture(ultimate ? ultimateSpritePath : spritePath), position.x - size / 2f, position.y - size / 2f, size, size);
        }
        if (toolbelt.shieldActive()) {
            Color old = batch.getColor().cpy();
            batch.setColor(new Color(0.4f, 0.85f, 1f, 0.28f));
            batch.draw(assets.pixel(), position.x - size * 0.42f, position.y - size * 0.42f, size * 0.84f, size * 0.84f);
            batch.setColor(old);
        }
        if (toolbelt.chronoElixirActive() || toolbelt.antiFreezeActive()) {
            Color old = batch.getColor().cpy();
            float auraSize = size * 0.96f;
            float auraX = position.x - auraSize / 2f;
            float auraY = position.y - auraSize / 2f;
            float border = Math.max(3f, size * 0.035f);
            batch.setColor(new Color(0.72f, 0.12f, 1f, 0.18f));
            batch.draw(assets.pixel(), auraX, auraY, auraSize, auraSize);
            batch.setColor(new Color(0.82f, 0.18f, 1f, 0.72f));
            batch.draw(assets.pixel(), auraX, auraY + auraSize - border, auraSize, border);
            batch.draw(assets.pixel(), auraX, auraY, auraSize, border);
            batch.draw(assets.pixel(), auraX, auraY, border, auraSize);
            batch.draw(assets.pixel(), auraX + auraSize - border, auraY, border, auraSize);
            batch.setColor(old);
        }
        if (ultimate) {
            Color old = batch.getColor().cpy();
            batch.setColor(new Color(1f, 0.05f, 0.05f, 0.18f));
            batch.draw(assets.pixel(), position.x - size * 0.46f, position.y - size * 0.46f, size * 0.92f, size * 0.92f);
            batch.setColor(old);
        }
    }

    private void drawAnimatedFrame(SpriteBatch batch, TextureRegion frame, float size, boolean ultimate) {
        float pace = ultimate ? 13f : 10f;
        float wave = MathUtils.sin(animationTime * pace);
        float bob = 0f;
        float scaleY = 1f;
        float rotation = 0f;

        if (animationState == AnimationState.WALK || animationState == AnimationState.RUN) {
            bob = Math.abs(wave) * (ultimate ? 5.5f : 4f);
            scaleY = 1f + Math.abs(wave) * 0.04f;
            rotation = wave * (ultimate ? 1.8f : 2.8f);
        } else if (animationState == AnimationState.ATTACK || animationState == AnimationState.ULTIMATE) {
            scaleY = 1.035f;
            rotation = facing.x >= 0f ? -3f : 3f;
        } else if (animationState == AnimationState.HURT) {
            rotation = facing.x >= 0f ? 5f : -5f;
        }

        float scaleX = facing.x >= 0f ? 1f : -1f;
        batch.draw(frame, position.x - size / 2f, position.y - size / 2f + bob,
            size / 2f, size / 2f, size, size, scaleX, scaleY, rotation);
    }
}
