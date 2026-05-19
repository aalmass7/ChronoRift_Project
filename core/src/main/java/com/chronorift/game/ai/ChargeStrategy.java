package com.chronorift.game.ai;

import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.entity.Player;
import com.chronorift.game.world.GameWorld;

public class ChargeStrategy implements EnemyStrategy{
    private float burstTimer = 1.5f;

    @Override
    public void update(Enemy enemy, Player player, GameWorld world, float delta) {
        burstTimer -= delta;
        Vector2 dir = new Vector2(player.getPosition()).sub(enemy.getPosition());
        if (!dir.isZero()) {
            float multiplier = burstTimer <= 0f ? 2.2f : 0.75f;
            enemy.getPosition().mulAdd(dir.nor(), enemy.getMoveSpeed() * multiplier * world.getEffectiveEnemyMultiplier(enemy) * delta);
            if (burstTimer <= -0.35f) {
                burstTimer = 2.2f;
            }
        }
        enemy.tryContactAttack(player, world, delta);
    }
}
