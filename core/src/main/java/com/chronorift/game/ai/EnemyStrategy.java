package com.chronorift.game.ai;

import com.chronorift.game.entity.Enemy;
import com.chronorift.game.entity.Player;
import com.chronorift.game.world.GameWorld;

public interface EnemyStrategy {
    void update(Enemy enemy, Player player, GameWorld world, float delta);
}
