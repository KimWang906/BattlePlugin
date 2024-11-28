package org.kw906plugin.battlePlugin.events;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.kw906plugin.battlePlugin.prepared_ability.FistAbility;

import static org.bukkit.Bukkit.getServer;
import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class FistListener implements Listener {
    private static final double BASE_HEALTH = config.fistAbilityConfig.baseHealth; // 기본 체력 절반
    private static final double BASE_ATTACK_DAMAGE = config.fistAbilityConfig.baseAttackDamage; // 기본 공격력
    private static final double NETHERITE_INCREASE_HEALTH = config.fistAbilityConfig.increaseHealth; // 네더라이트 주괴 사용 시 증가하는 체력
    private static final double MAX_ALLOWED_HEALTH = config.fistAbilityConfig.maximumHealth; // 최대 체력 제한

    public FistListener(Plugin plugin) {
        getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if ((event.getDamager() instanceof Player player) && hasAbility(player, FistAbility.class)) {
            AttributeInstance attr = player.getAttribute(Attribute.MAX_HEALTH);
            if (attr != null) {
                player.setHealth(attr.getDefaultValue() * BASE_HEALTH);
                event.setDamage(BASE_ATTACK_DAMAGE);
            }
        }
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (hasAbility(player, FistAbility.class)) {
            ItemStack item = event.getItem();
            if (item != null && item.getType() == Material.NETHERITE_INGOT) {
                double newHealth = Math.min(player.getHealth() + NETHERITE_INCREASE_HEALTH, MAX_ALLOWED_HEALTH);
                AttributeInstance attr = player.getAttribute(Attribute.MAX_HEALTH);
                if (attr != null) {
                    attr.setBaseValue(newHealth);
                }
                item.setAmount(item.getAmount() - 1);
            }
        }
    }
}
