package com.chronorift.game.ai;

import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.entity.Player;
import com.chronorift.game.world.GameWorld;

public class ChaseStrategy implements EnemyStrategy{
    @Override
    public void update(Enemy enemy, Player player, GameWorld world, float delta) {
        Vector2 dir = new Vector2(player.getPosition()).sub(enemy.getPosition());
        if (!dir.isZero()) {
            dir.nor().scl(enemy.getMoveSpeed() * world.getEffectiveEnemyMultiplier(enemy) * delta);
            enemy.getPosition().add(dir);
        }
        enemy.tryContactAttack(player, world, delta);
    }
}
