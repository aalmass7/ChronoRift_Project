package com.chronorift.game.combat;

import com.chronorift.game.entity.LivingEntity;

public class DamageContext {
    private final LivingEntity source;
    private final LivingEntity target;
    private float damage;

    public DamageContext(LivingEntity source, LivingEntity target, float damage) {
        this.source = source;
        this.target = target;
        this.damage = damage;
    }

    public LivingEntity getSource() {
        return source;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
