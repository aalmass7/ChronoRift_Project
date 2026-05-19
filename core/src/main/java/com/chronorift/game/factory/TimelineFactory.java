package com.chronorift.game.factory;

import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.level.LevelDefinition;

public interface TimelineFactory {
    LevelDefinition createLevelDefinition();
    Enemy createMinion(Vector2 position, float hpMultiplier, float damageMultiplier);
    Enemy createBoss(Vector2 position, float hpMultiplier, float damageMultiplier);
}
