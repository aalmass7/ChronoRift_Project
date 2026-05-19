package com.chronorift.game.ai;

import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.entity.Player;
import com.chronorift.game.world.GameWorld;

public class StrafeShooterStrategy implements EnemyStrategy{
    @Override
    public void update(Enemy enemy, Player player, GameWorld world, float delta) {
        Vector2 toPlayer = new Vector2(player.getPosition()).sub(enemy.getPosition());
        float distance = toPlayer.len();
        if (distance > 240f) {
            enemy.getPosition().mulAdd(toPlayer.nor(), enemy.getMoveSpeed() * world.getEffectiveEnemyMultiplier(enemy) * delta);
        } else if (distance < 160f) {
            enemy.getPosition().mulAdd(toPlayer.nor().scl(-1f), enemy.getMoveSpeed() * 0.85f * world.getEffectiveEnemyMultiplier(enemy) * delta);
        }
        enemy.tryShoot(player.getPosition(), world, delta);
        enemy.tryContactAttack(player, world, delta);
    }
}
