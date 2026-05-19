package com.chronorift.game.world;

import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.entity.LivingEntity;
import com.chronorift.game.entity.enemy.Enemy;
import com.chronorift.game.entity.projectile.Projectile;
import com.chronorift.game.time.TimeContext;

public class GameWorld {

    private TimeContext timeContext;

    public void movePlayer(Vector2 direction) {}

    public void playerShoot(Vector2 target) {}

    public void dashPlayer() {}

    public void activateShield() {}

    public void activateOverdrive() {}

    public void rewindPlayer() {}

    public void useChronoElixir() {}

    public int getKillsThisLevel() {
        return 0;
    }

    public boolean isBossDefeated() {
        return false;
    }

    public float getEffectiveEnemyMultiplier(Enemy enemy) {
        return 1f;
    }

    public float getEffectiveEnemyDamageMultiplier(Enemy enemy) {
        return 1f;
    }

    public void spawnProjectile(Projectile projectile) {
    }

    public void applyDamage(LivingEntity source, LivingEntity target, float damage) {
        target.damage(damage);
    }

    public void postLog(String message) {
    }

    public TimeContext getTimeContext() {
        return timeContext;
    }
}