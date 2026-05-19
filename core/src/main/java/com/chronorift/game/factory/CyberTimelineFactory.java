package com.chronorift.game.factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.ai.StrafeShooterStrategy;
import com.chronorift.game.core.GameConfig;
import com.chronorift.game.entity.BossAbilityType;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.level.LevelDefinition;
import com.chronorift.game.level.TimelineType;
import com.chronorift.game.time.TimeEffectType;
import com.chronorift.game.time.ZoneTimeState;

public class CyberTimelineFactory implements TimelineFactory {
    private final EnemyPrototype grunt = new EnemyPrototype(GameConfig.ENEMY_RADIUS, 82f, 3f, "Mech Rift Soldier",
            "game/mech_soldier.png", new StrafeShooterStrategy(), false, 108f, 10f, 14f)
            .withNativeTimeline(TimelineType.CYBER);
    private final EnemyPrototype boss = new EnemyPrototype(GameConfig.BOSS_RADIUS, 430f, 7f, "Chrono Enforcer",
            "game/boss_mech.png", "game/boss_mech_berserk.png", new StrafeShooterStrategy(), true,
            104f, 20f, 18f, BossAbilityType.REACTOR_BARRAGE)
            .withNativeTimeline(TimelineType.CYBER);

    @Override
    public LevelDefinition createLevelDefinition() {
        Color accent = new Color(0.35f, 0.9f, 1f, 1f);
        return new LevelDefinition(TimelineType.CYBER, "Level 4: Reversed Timeline",
                "The reactor corrupts movement logic. The player is confused by reversed controls, but cyber enemies are adapted.",
                "game/bg_mech_floor_hd.png", "Chrono Enforcer", 4, 5, 1.10f, 1.10f,
                accent, true,
                new ZoneTimeState("Reversed Timeline",
                        "Reactor glitch reverses player controls; native cyber enemies keep normal battle logic.",
                        0.95f, 1.15f, 1.05f, true, accent, TimeEffectType.REVERSED));
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
