package org.kw906plugin.battlePlugin.events;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.kw906plugin.battlePlugin.BattlePlugin;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class CrossbowListener implements Listener {
    private static final double SPEED_MULTIPLIER = config.crossbowAbilityConfig.speedMultiplier;
    private static final double MAX_SPEED_INCREASE = config.crossbowAbilityConfig.maxSpeedIncrease;
    public static final float BASE_WALK_SPEED = 0.1f;

    public CrossbowListener(BattlePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

//    @EventHandler
//    public void onPlayerDamage(EntityRegainHealthEvent event) {
//        setSpeed(event.getEntity());
//    }
//
//    @EventHandler
//    public void onPlayerHeal(EntityRegainHealthEvent event) {
//        setSpeed(event.getEntity());
//    }

//    public void setSpeed(Entity entity) {
//        if (entity instanceof Player player) {
//            double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
//            double currentHealth = player.getHealth();
//            double healthPercentage = currentHealth / maxHealth;
//            double newMovementSpeed = calculateMovementSpeed(healthPercentage);
//            player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(newMovementSpeed);
//        }
//    }
//
//    private double calculateMovementSpeed(double healthPercentage) {
//        double minSpeed = 0.1;
//        double maxSpeed = 0.29;
//        return minSpeed + (maxSpeed - minSpeed) * (1 - healthPercentage);
//    }
}
