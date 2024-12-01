package org.kw906plugin.battlePlugin.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.prepared_ability.StickAbility;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class StickListener implements Listener {
    private static double playerDamageTemp = 0; // 누적 데미지 초기화
    private static final double DEATH_DAMAGE_REDUCTION = config.stickAbilityConfig.deathDamageReduction;
    private static final double DAMAGE_INCREMENT_THRESHOLD = config.stickAbilityConfig.damageIncrementThreshold;
    private static final double MAX_ACCUMULATED_DAMAGE = config.stickAbilityConfig.maxAccumulatedDamage; // 최대 누적 데미지 제한

    public StickListener(BattlePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (hasAbility(player, StickAbility.class)) {
                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                if (mainHandItem.getType() == Material.STICK) {
                    double currentDamage = event.getDamage();
                    playerDamageTemp += currentDamage;
                    if (playerDamageTemp > MAX_ACCUMULATED_DAMAGE) {
                        playerDamageTemp = MAX_ACCUMULATED_DAMAGE;
                    }

                    double bonusDamage = Math.floor(playerDamageTemp / DAMAGE_INCREMENT_THRESHOLD);
                    event.setDamage(currentDamage + bonusDamage);

                    SendMessage.sendActionBar(player,
                                              Component.text("현재 누적 데미지: " + String.format("%.0f", playerDamageTemp)).color(NamedTextColor.RED)
                                                       .append(Component.text(" | ").color(NamedTextColor.WHITE))
                                                       .append(Component.text("추가 데미지: " + bonusDamage).color(NamedTextColor.GREEN)));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (hasAbility(player, StickAbility.class)) {
            playerDamageTemp *= (1 - DEATH_DAMAGE_REDUCTION);
        }
    }
}
