package com.chronorift.game.combat;

public class ArmorDamageHandler extends DamageHandler{
    @Override
    protected void apply(DamageContext context) {
        context.setDamage(Math.max(0.5f, context.getDamage() - context.getTarget().getArmor() * 0.45f));
    }
}
