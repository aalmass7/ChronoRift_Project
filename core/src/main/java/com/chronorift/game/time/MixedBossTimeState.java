package com.chronorift.game.time;

import com.badlogic.gdx.graphics.Color;

public class MixedBossTimeState implements TimeState{
    private enum Phase {
        SLOW("Slow Pulse", "movement drag", 0.72f, 1.18f, 0.80f, false),
        FROZEN("Frozen Pulse", "movement restriction", 0.50f, 1.10f, 0.62f, false),
        REVERSED("Reverse Pulse", "reversed controls", 0.92f, 1.22f, 1.05f, true),
        OVERLOAD("Overload Pulse", "fast enemy pressure", 0.82f, 1.32f, 1.18f, true);

        private final String label;
        private final String description;
        private final float playerSpeed;
        private final float enemySpeed;
        private final float projectileSpeed;
        private final boolean reverse;

        Phase(String label, String description, float playerSpeed, float enemySpeed, float projectileSpeed, boolean reverse) {
            this.label = label;
            this.description = description;
            this.playerSpeed = playerSpeed;
            this.enemySpeed = enemySpeed;
            this.projectileSpeed = projectileSpeed;
            this.reverse = reverse;
        }
    }

    private static final float PHASE_DURATION = 3.2f;
    private static final Color ACCENT = new Color(1f, 0.78f, 0.28f, 1f);
    private int phaseIndex;
    private float phaseTimer;

    @Override
    public void update(float delta) {
        phaseTimer += delta;
        if (phaseTimer >= PHASE_DURATION) {
            phaseTimer -= PHASE_DURATION;
            phaseIndex = (phaseIndex + 1) % Phase.values().length;
        }
    }

    private Phase phase() {
        return Phase.values()[phaseIndex];
    }

    @Override
    public String getName() {
        return "Boss Timeline - " + phase().label;
    }

    @Override
    public String getDescription() {
        return "Mixed time effects cycle in the final arena: " + phase().description + ".";
    }

    @Override
    public float playerSpeedMultiplier() {
        return phase().playerSpeed;
    }

    @Override
    public float enemySpeedMultiplier() {
        return phase().enemySpeed;
    }

    @Override
    public float projectileSpeedMultiplier() {
        return phase().projectileSpeed;
    }

    @Override
    public boolean reverseControls() {
        return phase().reverse;
    }

    @Override
    public Color accentColor() {
        Phase current = phase();
        return switch (current) {
            case SLOW -> new Color(0.32f, 0.95f, 0.42f, 1f);
            case FROZEN -> new Color(0.65f, 0.82f, 1f, 1f);
            case REVERSED -> new Color(1f, 0.56f, 0.28f, 1f);
            case OVERLOAD -> new Color(1f, 0.18f, 0.12f, 1f);
        };
    }

    @Override
    public TimeStateType effectType() {
        return TimeStateType.BOSS;
    }
}
