package com.chronorift.game.level;

import com.badlogic.gdx.graphics.Color;
import com.chronorift.game.level.objective.ObjectiveGroup;
import com.chronorift.game.time.NormalTimeState;
import com.chronorift.game.time.TimeState;

public class LevelDefinition {
    private final TimelineType type;
    private final String title;
    private final String description;
    private final String backgroundPath;
    private final String bossName;
    private final int waves;
    private final int enemiesPerWave;
    private final float enemyHpMultiplier;
    private final float enemyDamageMultiplier;
    private final Color accentColor;
    private final boolean bossLevel;
    private final TimeState zoneEffect;
    private ObjectiveGroup objectives;

    public LevelDefinition(TimelineType type, String title, String description, String backgroundPath, String bossName,
                           int waves, int enemiesPerWave, float enemyHpMultiplier, float enemyDamageMultiplier,
                           Color accentColor) {
        this(type, title, description, backgroundPath, bossName, waves, enemiesPerWave, enemyHpMultiplier,
                enemyDamageMultiplier, accentColor, true, new NormalTimeState());
    }

    public LevelDefinition(TimelineType type, String title, String description, String backgroundPath, String bossName,
                           int waves, int enemiesPerWave, float enemyHpMultiplier, float enemyDamageMultiplier,
                           Color accentColor, boolean bossLevel) {
        this(type, title, description, backgroundPath, bossName, waves, enemiesPerWave, enemyHpMultiplier,
                enemyDamageMultiplier, accentColor, bossLevel, new NormalTimeState());
    }

    public LevelDefinition(TimelineType type, String title, String description, String backgroundPath, String bossName,
                           int waves, int enemiesPerWave, float enemyHpMultiplier, float enemyDamageMultiplier,
                           Color accentColor, boolean bossLevel, TimeState zoneEffect) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.backgroundPath = backgroundPath;
        this.bossName = bossName;
        this.waves = waves;
        this.enemiesPerWave = enemiesPerWave;
        this.enemyHpMultiplier = enemyHpMultiplier;
        this.enemyDamageMultiplier = enemyDamageMultiplier;
        this.accentColor = accentColor;
        this.bossLevel = bossLevel;
        this.zoneEffect = zoneEffect == null ? new NormalTimeState() : zoneEffect;
    }

    public TimelineType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public String getBossName() {
        return bossName;
    }

    public int getWaves() {
        return waves;
    }

    public int getEnemiesPerWave() {
        return enemiesPerWave;
    }

    public float getEnemyHpMultiplier() {
        return enemyHpMultiplier;
    }

    public float getEnemyDamageMultiplier() {
        return enemyDamageMultiplier;
    }

    public Color getAccentColor() {
        return accentColor;
    }

    public boolean hasBoss() {
        return bossLevel;
    }

    public TimeState getZoneEffect() {
        return zoneEffect;
    }

    public ObjectiveGroup getObjectives() {
        return objectives;
    }

    public void setObjectives(ObjectiveGroup objectives) {
        this.objectives = objectives;
    }
}
