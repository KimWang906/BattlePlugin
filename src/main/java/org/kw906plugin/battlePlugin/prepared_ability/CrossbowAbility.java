package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.utils.PlayerImpl;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class CrossbowAbility extends Ability implements Listener {
    private static final double SPEED_MULTIPLIER = config.crossbowAbilityConfig.speedMultiplier;
    private static final double MAX_SPEED_INCREASE = config.crossbowAbilityConfig.maxSpeedIncrease;
    private static final float BASE_WALK_SPEED = 0.1f;
    private double temp;

    public CrossbowAbility() {
        setName("쇠뇌");
        setDescription("잃은 체력에 비례해 이동 속도가 증가합니다.");
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
