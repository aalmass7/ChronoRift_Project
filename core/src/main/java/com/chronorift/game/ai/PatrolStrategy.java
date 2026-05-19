package com.chronorift.game.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.core.GameConfig;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.entity.Player;
import com.chronorift.game.world.GameWorld;

public class PatrolStrategy implements EnemyStrategy{
    private final Vector2 patrolDirection = new Vector2(1f, 0f);
    private float retargetTimer = 1.2f;

    @Override
    public void update(Enemy enemy, Player player, GameWorld world, float delta) {
        float detectionRange = 300f;
        Vector2 toPlayer = new Vector2(player.getPosition()).sub(enemy.getPosition());
        if (toPlayer.len2() <= detectionRange * detectionRange) {
            if (!toPlayer.isZero()) {
                enemy.getPosition().mulAdd(toPlayer.nor(), enemy.getMoveSpeed() * world.getEffectiveEnemyMultiplier(enemy) * delta);
            }
        } else {
            retargetTimer -= delta;
            if (retargetTimer <= 0f) {
                float angle = MathUtils.random(0f, 360f);
                patrolDirection.set(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle));
                retargetTimer = 1.1f + MathUtils.random(0f, 0.8f);
            }
            enemy.getPosition().mulAdd(patrolDirection, enemy.getMoveSpeed() * 0.55f * world.getEffectiveEnemyMultiplier(enemy) * delta);
            if (enemy.getPosition().x < enemy.getRadius() || enemy.getPosition().x > GameConfig.WORLD_WIDTH - enemy.getRadius()) {
                patrolDirection.x *= -1f;
            }
            if (enemy.getPosition().y < enemy.getRadius() || enemy.getPosition().y > GameConfig.WORLD_HEIGHT - enemy.getRadius()) {
                patrolDirection.y *= -1f;
            }
        }
        enemy.tryContactAttack(player, world, delta);
    }
}
