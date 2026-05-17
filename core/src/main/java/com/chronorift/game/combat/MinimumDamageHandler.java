package com.chronorift.game.combat;

public class MinimumDamageHandler extends DamageHandler{
    @Override
    protected void apply(DamageContext context) {
        context.setDamage(Math.max(1f, context.getDamage()));
    }
}
