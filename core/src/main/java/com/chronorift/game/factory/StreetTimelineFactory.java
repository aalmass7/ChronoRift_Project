package com.chronorift.game.factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.ai.PatrolStrategy;
import com.chronorift.game.core.GameConfig;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.level.LevelDefinition;
import com.chronorift.game.level.TimelineType;
import com.chronorift.game.time.TimeStateType;
import com.chronorift.game.time.ZoneTimeState;

public class StreetTimelineFactory implements TimelineFactory {
    private final EnemyPrototype mole = new EnemyPrototype(GameConfig.ENEMY_RADIUS, 58f, 1f, "Chrono Mole Guard",
            "game/mole_guard.png", new PatrolStrategy(), false, 125f, 10f, 0f)
            .withNativeTimeline(TimelineType.STREET);

    @Override
    public LevelDefinition createLevelDefinition() {
        Color accent = new Color(0.55f, 0.95f, 1f, 1f);
        return new LevelDefinition(TimelineType.STREET, "Level 1: Normal Timeline",
                "Basic introduction level. No negative time distortion; clear guards and enter the tower door.",
                "game/bg_street_hd.png", "Tower Door", 2, 5, 1f, 1f,
                accent, false,
                new ZoneTimeState("Normal Timeline",
                        "Stable time flow: no chrono penalty, only the door objective.",
                        1f, 1f, 1f, false, accent, TimeStateType.NORMAL));
    }

    @Override
    public Enemy createMinion(Vector2 position, float hpMultiplier, float damageMultiplier) {
        return mole.spawn(position, hpMultiplier, damageMultiplier);
    }

    @Override
    public Enemy createBoss(Vector2 position, float hpMultiplier, float damageMultiplier) {
        return mole.spawn(position, hpMultiplier, damageMultiplier);
    }
}
