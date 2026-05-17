package com.chronorift.game.combat;

public abstract class DamageHandler {
    private DamageHandler next;

    public DamageHandler linkWith(DamageHandler next) {
        this.next = next;
        return next;
    }

    public float handle(DamageContext context) {
        apply(context);
        if (next != null) {
            return next.handle(context);
        }
        return context.getDamage();
    }

    protected abstract void apply(DamageContext context);
}
