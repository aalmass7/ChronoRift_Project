package com.chronorift.game.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSprite {
    private final TextureRegion[][] cells;
    private final int columns;
    private final int rows;
    private final boolean alternateForm;
    private final AnimationProfile profile;
    private final float frameDuration;
    private final int framesPerForm;

    public AnimatedSprite(Texture texture, int columns, int rows, boolean alternateForm,
                          AnimationProfile profile, float frameDuration) {
        this.columns = columns;
        this.rows = rows;
        this.alternateForm = alternateForm;
        this.profile = profile;
        this.frameDuration = frameDuration;
        this.framesPerForm = alternateForm ? Math.max(1, (columns * rows) / 2) : columns * rows;
        this.cells = buildCells(texture, columns, rows);
    }

    private TextureRegion[][] buildCells(Texture texture, int columns, int rows) {
        TextureRegion[][] result = new TextureRegion[rows][columns];
        float cellWidth = texture.getWidth() / (float) columns;
        float cellHeight = texture.getHeight() / (float) rows;
        for (int row = 0; row < rows; row++) {
            int y = Math.round(row * cellHeight);
            int nextY = Math.round((row + 1) * cellHeight);
            for (int column = 0; column < columns; column++) {
                int x = Math.round(column * cellWidth);
                int nextX = Math.round((column + 1) * cellWidth);
                result[row][column] = new TextureRegion(texture, x, y, nextX - x, nextY - y);
            }
        }
        return result;
    }

    public TextureRegion frame(AnimationState state, boolean useAlternateForm, float stateTime) {
        int index = frameIndex(state, stateTime);
        if (useAlternateForm && alternateForm) {
            index += framesPerForm;
        }
        int maxIndex = rows * columns - 1;
        index = Math.max(0, Math.min(index, maxIndex));
        return cells[index / columns][index % columns];
    }

    private int frameIndex(AnimationState state, float stateTime) {
        return switch (profile) {
            case PLAYER -> playerFrame(state, stateTime);
            case MOLE -> moleFrame(state, stateTime);
            case MINION -> minionFrame(state, stateTime);
            case BOSS -> bossFrame(state, stateTime);
        };
    }

    private int oscillate(float stateTime, int first, int second) {
        return ((int) (stateTime / frameDuration)) % 2 == 0 ? first : second;
    }

    private int playerFrame(AnimationState state, float stateTime) {
        return switch (state) {
            case IDLE -> oscillate(stateTime, 0, 1);
            case WALK -> oscillate(stateTime, 2, 3);
            case RUN -> oscillate(stateTime, 4, 5);
            case ATTACK, ULTIMATE -> 6;
            case HURT -> 7;
        };
    }

    private int minionFrame(AnimationState state, float stateTime) {
        return switch (state) {
            case IDLE -> oscillate(stateTime, 0, 1);
            case WALK, RUN -> oscillate(stateTime, 2, 3);
            case ATTACK -> oscillate(stateTime, 4, 5);
            case ULTIMATE -> 5;
            case HURT -> 6;
        };
    }

    private int moleFrame(AnimationState state, float stateTime) {
        return switch (state) {
            case IDLE -> oscillate(stateTime, 1, 2);
            case WALK, RUN -> oscillate(stateTime, 3, 4);
            case ATTACK -> oscillate(stateTime, 5, 6);
            case ULTIMATE -> 1;
            case HURT -> 6;
        };
    }

    private int bossFrame(AnimationState state, float stateTime) {
        return switch (state) {
            case IDLE -> oscillate(stateTime, 0, 1);
            case WALK, RUN -> oscillate(stateTime, 2, 3);
            case ATTACK -> oscillate(stateTime, 4, 5);
            case ULTIMATE -> 6;
            case HURT -> 7;
        };
    }

    public boolean hasAlternateForm() {
        return alternateForm;
    }
}
