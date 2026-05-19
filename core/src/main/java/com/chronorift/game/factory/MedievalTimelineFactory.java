package com.chronorift.game.factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.ai.ChargeStrategy;
import com.chronorift.game.ai.ChaseStrategy;
import com.chronorift.game.core.GameConfig;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.level.LevelDefinition;
import com.chronorift.game.level.TimelineType;
import com.chronorift.game.time.TimeEffectType;
import com.chronorift.game.time.ZoneTimeState;

public class MedievalTimelineFactory implements TimelineFactory {
    private final EnemyPrototype grunt = new EnemyPrototype(GameConfig.ENEMY_RADIUS, 75f, 2f, "Jungle Rift Grunt",
            "game/grunt.png", new ChaseStrategy(), false, 118f, 12f, 0f)
            .withNativeTimeline(TimelineType.MEDIEVAL);
    private final EnemyPrototype boss = new EnemyPrototype(GameConfig.BOSS_RADIUS, 380f, 6f, "Rootbound Iron Warden",
            "game/boss_medieval.png", new ChargeStrategy(), true, 122f, 27f, 0f)
            .withNativeTimeline(TimelineType.MEDIEVAL);

    @Override
    public LevelDefinition createLevelDefinition() {
        Color accent = new Color(0.32f, 0.95f, 0.42f, 1f);
        return new LevelDefinition(TimelineType.MEDIEVAL, "Level 2: Slow Motion Timeline",
                "A vine-choked ruin slows the outsider while native jungle enemies remain adapted.",
                "game/bg_jungle_floor_hd.png", "Rootbound Iron Warden", 3, 5, 1.04f, 1.02f,
                accent, true,
                new ZoneTimeState("Slow Motion Timeline",
                        "Roots and unstable time drag the player; enemies react better inside their native zone.",
                        0.72f, 1.10f, 0.78f, false, accent, TimeEffectType.SLOW));
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
