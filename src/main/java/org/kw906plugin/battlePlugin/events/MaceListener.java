package org.kw906plugin.battlePlugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.prepared_ability.MaceAbility;

import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class MaceListener implements Listener {
    public MaceListener(BattlePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasAbility(player, MaceAbility.class) && event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                event.setCancelled(true);
            }
        }
    }
}
