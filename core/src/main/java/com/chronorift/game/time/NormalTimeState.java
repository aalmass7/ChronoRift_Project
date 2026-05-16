package com.chronorift.game.time;

import com.badlogic.gdx.graphics.Color;

public class NormalTimeState implements TimeState{

        @Override
        public String getName() {
            return "Normal Timeline";
        }

        @Override
        public String getDescription() {
            return "Stable time flow with no negative zone effect.";
        }

        @Override
        public float playerSpeedMultiplier() {
            return 1f;
        }

        @Override
        public float enemySpeedMultiplier() {
            return 1f;
        }

        @Override
        public float projectileSpeedMultiplier() {
            return 1f;
        }

        @Override
        public boolean reverseControls() {
            return false;
        }

        @Override
        public Color accentColor() {
            return Color.WHITE;
        }

        @Override
        public TimeStateType effectType() {
            return TimeStateType.NORMAL;
        }
}
