package com.chronorift.game.tool;

public class ChronoToolbelt {
    private float shieldTimer;
    private float shieldCooldown;
    private float chronoElixirTimer;
    private float chronoElixirCooldown;
    private float antiFreezeTimer;
    private float antiFreezeCooldown;
    private float overdriveTimer;
    private float overdriveCooldown;
    private float rewindCooldown;

    public void update(float delta) {
        shieldTimer = Math.max(0f, shieldTimer - delta);
        shieldCooldown = Math.max(0f, shieldCooldown - delta);
        chronoElixirTimer = Math.max(0f, chronoElixirTimer - delta);
        chronoElixirCooldown = Math.max(0f, chronoElixirCooldown - delta);
        antiFreezeTimer = Math.max(0f, antiFreezeTimer - delta);
        antiFreezeCooldown = Math.max(0f, antiFreezeCooldown - delta);
        overdriveTimer = Math.max(0f, overdriveTimer - delta);
        overdriveCooldown = Math.max(0f, overdriveCooldown - delta);
        rewindCooldown = Math.max(0f, rewindCooldown - delta);
    }

    public boolean activateShield() {
        if (shieldCooldown > 0f) {
            return false;
        }
        shieldTimer = 5f;
        shieldCooldown = 8f;
        return true;
    }

    public boolean useChronoElixir() {
        if (chronoElixirCooldown > 0f) {
            return false;
        }
        chronoElixirTimer = 6.5f;
        antiFreezeTimer = 6.5f;
        chronoElixirCooldown = 13f;
        antiFreezeCooldown = 13f;
        return true;
    }


    public boolean activateOverdrive() {
        if (overdriveCooldown > 0f) {
            return false;
        }
        overdriveTimer = 6f;
        overdriveCooldown = 15f;
        return true;
    }

    public boolean useRewind() {
        if (rewindCooldown > 0f) {
            return false;
        }
        rewindCooldown = 16f;
        return true;
    }

    public boolean shieldActive() {
        return shieldTimer > 0f;
    }

    public boolean chronoElixirActive() {
        return chronoElixirTimer > 0f;
    }

    public boolean antiFreezeActive() {
        return antiFreezeTimer > 0f;
    }

    public boolean overdriveActive() {
        return overdriveTimer > 0f;
    }

    public float getShieldCooldown() {
        return shieldCooldown;
    }

    public float getChronoElixirCooldown() {
        return chronoElixirCooldown;
    }

    public float getAntiFreezeCooldown() {
        return antiFreezeCooldown;
    }

    public float getOverdriveCooldown() {
        return overdriveCooldown;
    }

    public float getRewindCooldown() {
        return rewindCooldown;
    }

    public String activeItemsLabel() {
        StringBuilder builder = new StringBuilder();
        if (shieldActive()) builder.append("Shield ");
        if (chronoElixirActive() || antiFreezeActive()) builder.append("Chrono Elixir ");
        if (overdriveActive()) builder.append("Ultra ");
        return builder.length() == 0 ? "none" : builder.toString().trim();
    }
}
