package com.chronorift.game.factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.ai.ChargeStrategy;
import com.chronorift.game.ai.ChaseStrategy;
import com.chronorift.game.core.GameConfig;
import com.chronorift.game.entity.BossAbilityType;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.level.LevelDefinition;
import com.chronorift.game.level.TimelineType;
import com.chronorift.game.time.FrozenTimeState;


public class FrozenTimelineFactory implements TimelineFactory {
    private final EnemyPrototype grunt = new EnemyPrototype(GameConfig.ENEMY_RADIUS, 88f, 4f, "Frostbound Soldier",
            "game/frost_soldier.png", new ChaseStrategy(), false, 104f, 13f, 0f)
            .withNativeTimeline(TimelineType.FROZEN);
    private final EnemyPrototype boss = new EnemyPrototype(GameConfig.BOSS_RADIUS, 475f, 8f, "Frost Fang",
            "game/boss_ice.png", "game/boss_ice_berserk.png", new ChargeStrategy(), true,
            116f, 28f, 0f, BossAbilityType.FROST_NOVA)
            .withNativeTimeline(TimelineType.FROZEN);

    @Override
    public LevelDefinition createLevelDefinition() {
        Color accent = new Color(0.7f, 0.85f, 1f, 1f);
        return new LevelDefinition(TimelineType.FROZEN, "Level 3: Frozen Timeline",
                "A glacier arena periodically restricts the outsider. Frost enemies are adapted to this timeline.",
                "game/bg_ice_floor_hd.png", "Frost Fang", 4, 6, 1.16f, 1.12f,
                accent, true, new FrozenTimeState());
    }

    @Override
    public Enemy createMinion(Vector2 position, float hpMultiplier, float damageMultiplier) {
        return grunt.spawn(position, hpMultiplier, damageMultiplier);
    }

    @Override
    public Enemy createBoss(Vector2 position, float hpMultiplier, float damageMultiplier) {
        return boss.spawn(position, hpMultiplier, damageMultiplier);
    }
}
