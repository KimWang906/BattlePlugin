package org.kw906plugin.battlePlugin.events;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kw906plugin.battlePlugin.BattlePlugin;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;

public class TridentListener implements Listener {
    private static final long DEFAULT_PLAYER_HEALTH = 20;
    private static final double DEFAULT_SUBMERGED_MINING_SPEED = 0.2;
    private static final double DEFAULT_WATER_MOVEMENT_EFFICIENCY = 0;
    private static final double DEFAULT_OXYGEN_BONUS = 1.0;

    public TridentListener(BattlePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (hasAbility(player) && isInWater(player)) {
            applyWaterEffects(player);
        } else {
            removeWaterEffects(player);
        }
    }

    private boolean hasAbility(Player player) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        return mainHandItem.getType() == Material.TRIDENT;
    }

    private boolean isInWater(Player player) {
        Material blockType = player.getLocation().getBlock().getType();
        return blockType == Material.WATER || blockType == Material.BUBBLE_COLUMN;
    }

    private void applyWaterEffects(Player player) {
        long maxHealth = config.tridentAbilityConfig.maxHealth;
        double waterSubmergedMiningSpeed = config.tridentAbilityConfig.waterSubmergedMiningSpeed;
        double waterMovementEfficiency = config.tridentAbilityConfig.waterMovementEfficiency;
        double oxygenBonus = config.tridentAbilityConfig.oxygenBonus;
        int saturationAmplifier = config.tridentAbilityConfig.saturationAmplifier;

        AttributeInstance healthAttr = player.getAttribute(Attribute.MAX_HEALTH);
        AttributeInstance waterEffectAttr_0 = player.getAttribute(Attribute.SUBMERGED_MINING_SPEED);
        AttributeInstance waterEffectAttr_1 = player.getAttribute(Attribute.WATER_MOVEMENT_EFFICIENCY);
        AttributeInstance waterEffectAttr_2 = player.getAttribute(Attribute.OXYGEN_BONUS);

        if (healthAttr != null && healthAttr.getBaseValue() < maxHealth) {
            healthAttr.setBaseValue(maxHealth);
        }

        if (waterEffectAttr_0 != null && waterEffectAttr_0.getBaseValue() < waterSubmergedMiningSpeed) {
            waterEffectAttr_0.setBaseValue(waterSubmergedMiningSpeed);
        }

        if (waterEffectAttr_1 != null && waterEffectAttr_1.getBaseValue() < waterMovementEfficiency) {
            waterEffectAttr_1.setBaseValue(waterMovementEfficiency);
        }

        if (waterEffectAttr_2 != null && waterEffectAttr_2.getBaseValue() < oxygenBonus) {
            waterEffectAttr_2.setBaseValue(oxygenBonus);
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, saturationAmplifier));
    }

    private void removeWaterEffects(Player player) {
        AttributeInstance healthAttribute = player.getAttribute(Attribute.MAX_HEALTH);
        AttributeInstance waterEffectAttr_0 = player.getAttribute(Attribute.SUBMERGED_MINING_SPEED);
        AttributeInstance waterEffectAttr_1 = player.getAttribute(Attribute.WATER_MOVEMENT_EFFICIENCY);
        AttributeInstance waterEffectAttr_2 = player.getAttribute(Attribute.OXYGEN_BONUS);

        if (healthAttribute != null) {
            healthAttribute.setBaseValue(DEFAULT_PLAYER_HEALTH);
        }

        if (waterEffectAttr_0 != null) {
            waterEffectAttr_0.setBaseValue(DEFAULT_SUBMERGED_MINING_SPEED);
        }

        if (waterEffectAttr_1 != null) {
            waterEffectAttr_1.setBaseValue(DEFAULT_WATER_MOVEMENT_EFFICIENCY);
        }

        if (waterEffectAttr_2 != null) {
            waterEffectAttr_2.setBaseValue(DEFAULT_OXYGEN_BONUS);
        }

        player.removePotionEffect(PotionEffectType.REGENERATION);
    }
}
