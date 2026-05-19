package com.chronorift.game.factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.ai.ChargeStrategy;
import com.chronorift.game.ai.StrafeShooterStrategy;
import com.chronorift.game.core.GameConfig;
import com.chronorift.game.entity.BossAbilityType;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.level.LevelDefinition;
import com.chronorift.game.level.TimelineType;
import com.chronorift.game.time.MixedBossTimeState;

public class DesertTimelineFactory implements TimelineFactory {
    private final EnemyPrototype grunt = new EnemyPrototype(GameConfig.ENEMY_RADIUS, 95f, 4f, "Orc Chrono Soldier",
            "game/orc_soldier.png", new StrafeShooterStrategy(), false, 116f, 12f, 14f)
            .withNativeTimeline(TimelineType.DESERT);
    private final EnemyPrototype boss = new EnemyPrototype(GameConfig.BOSS_RADIUS, 540f, 10f, "Sunscar Time Warlord",
            "game/boss_orc.png", "game/boss_orc_berserk.png", new ChargeStrategy(), true,
            124f, 31f, 12f, BossAbilityType.SANDS_OF_RAGE)
            .withNativeTimeline(TimelineType.DESERT);

    @Override
    public LevelDefinition createLevelDefinition() {
        Color accent = new Color(1f, 0.82f, 0.35f, 1f);
        return new LevelDefinition(TimelineType.DESERT, "Final Level: Boss Timeline",
                "The final war hall combines the harshest distortions with the strongest enemies and final boss.",
                "game/bg_orc_floor_hd.png", "Sunscar Time Warlord", 4, 7, 1.28f, 1.22f,
                accent, true, new MixedBossTimeState());
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
