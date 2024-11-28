package org.kw906plugin.battlePlugin.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.prepared_ability.ShieldAbility;

import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class ShieldListener implements Listener {
    public ShieldListener(BattlePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasAbility(player, ShieldAbility.class)) {
                ItemStack offHandItem = player.getInventory().getItemInOffHand();
                if (offHandItem.getType() == Material.SHIELD) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (hasAbility(player, ShieldAbility.class)) {
                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                ItemStack offHandItem = player.getInventory().getItemInOffHand();
                if ((mainHandItem.getType() == Material.SHIELD) || (offHandItem.getType() == Material.SHIELD)) {
                    event.setCancelled(true);
                    player.sendMessage(Component.text(
                            "방패를 들고 있어 공격할 수 없습니다!"
                    ).color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
                }
            }
        }
    }
}
