package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.kw906plugin.battlePlugin.Ability;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class CrossbowAbility extends Ability implements Listener {
    private static final double SPEED_MULTIPLIER = config.crossbowAbilityConfig.speedMultiplier;
    private static final double MAX_SPEED_INCREASE = config.crossbowAbilityConfig.maxSpeedIncrease;
    private static final float BASE_WALK_SPEED = 0.1f;

    public CrossbowAbility() {
        setName("쇠뇌");
        setDescription("잃은 체력에 비례해 이동 속도가 증가합니다.");
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasAbility(player, CrossbowAbility.class)) {
                // 잃은 체력 계산
                double maxHealth = player.getMaxHealth();
                double lostHealth = maxHealth - player.getHealth();

                // 이동 속도 계산 (잃은 체력에 비례)
                double speedIncrease = lostHealth * SPEED_MULTIPLIER;

                // 최대 증가치 제한
                if (speedIncrease > MAX_SPEED_INCREASE) {
                    speedIncrease = MAX_SPEED_INCREASE;
                }

                // 이동 속도 적용
                player.setWalkSpeed(BASE_WALK_SPEED + (float) speedIncrease);
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
