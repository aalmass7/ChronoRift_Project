package com.chronorift.game.combat;

import com.chronorift.game.entity.Player;

public class PlayerResistanceHandler extends DamageHandler{
    @Override
    protected void apply(DamageContext context) {
        if (context.getTarget() instanceof Player player) {
            float factor = 1f - player.currentStats().resistance();
            context.setDamage(context.getDamage() * factor);
        }
    }
}
