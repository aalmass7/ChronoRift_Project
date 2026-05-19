package com.chronorift.game.integration;

import com.chronorift.game.entity.Enemy;
import com.chronorift.game.level.LevelDefinition;
import com.chronorift.game.level.TimelineType;
import com.chronorift.game.time.TimeContext;

public class TimelineAiIntegrator {
    private static final float OUTSIDE_TIMELINE_SPEED_PENALTY = 0.72f;
    private static final float OUTSIDE_TIMELINE_DAMAGE_PENALTY = 0.74f;

    public float enemySpeedMultiplier(Enemy enemy, LevelDefinition level, TimeContext timeContext) {
        float multiplier = timeContext.current().enemySpeedMultiplier();
        if (isOutsideNativeTimeline(enemy, level)) {
            multiplier *= OUTSIDE_TIMELINE_SPEED_PENALTY;
        }
        return multiplier;
    }

    public float enemyDamageMultiplier(Enemy enemy, LevelDefinition level) {
        return isOutsideNativeTimeline(enemy, level) ? OUTSIDE_TIMELINE_DAMAGE_PENALTY : 1f;
    }

    public boolean isOutsideNativeTimeline(Enemy enemy, LevelDefinition level) {
        if (enemy == null || level == null) {
            return false;
        }
        TimelineType nativeTimeline = enemy.getNativeTimeline();
        return nativeTimeline != null && nativeTimeline != level.getType();
    }
}
