package org.kw906plugin.battlePlugin.events;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.prepared_ability.CrossbowAbility;
import org.kw906plugin.battlePlugin.utils.PlayerImpl;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class CrossbowListener implements Listener {
    private static final double SPEED_MULTIPLIER = config.crossbowAbilityConfig.speedMultiplier;
    private static final double MAX_SPEED_INCREASE = config.crossbowAbilityConfig.maxSpeedIncrease;
    private static final float BASE_WALK_SPEED = 0.1f;

    public CrossbowListener(BattlePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasAbility(player, CrossbowAbility.class)) {
                double maxHealth = PlayerImpl.INITIAL_MAX_HEALTH;
                double lostHealth = maxHealth - player.getHealth();
                double speedIncrease = lostHealth * SPEED_MULTIPLIER;
                if (speedIncrease > MAX_SPEED_INCREASE) {
                    speedIncrease = MAX_SPEED_INCREASE;
                }

                // 이동 속도 적용
                AttributeInstance attr = player.getAttribute(Attribute.MOVEMENT_SPEED);
                if (attr != null) {
                    attr.setBaseValue(speedIncrease);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasAbility(player, CrossbowAbility.class)) {
                player.setWalkSpeed(BASE_WALK_SPEED);
            }
        }
    }
}
