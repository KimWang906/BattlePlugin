package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;

import java.util.HashMap;
import java.util.UUID;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class StickAbility extends Ability implements Listener {
    private static final HashMap<UUID, Double> playerDamageMap = new HashMap<>();
    private static final double DAMAGE_MULTIPLIER = config.stickAbilityConfig.damageMultiplier;
    private static final double DEATH_DAMAGE_REDUCTION = config.stickAbilityConfig.deathDamageReduction;
    private static final double MAX_DAMAGE = config.stickAbilityConfig.maxDamage; // 최대 성장 데미지

    public StickAbility() {
        setName("부러지지 않는 막대");
        setDescription("나무 막대로 공격 시 누적 피해량에 비례해 데미지가 증가합니다.");
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (hasAbility(player, StickAbility.class)) {
                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                if (mainHandItem.getType() == Material.STICK) {
                    double currentDamage = event.getDamage();
                    double accumulatedDamage = playerDamageMap.getOrDefault(player.getUniqueId(), 0.0);
                    double bonusDamage = Math.min(accumulatedDamage * DAMAGE_MULTIPLIER, MAX_DAMAGE - currentDamage); // 최대 데미지 제한

                    event.setDamage(currentDamage + bonusDamage);
                    playerDamageMap.put(player.getUniqueId(), accumulatedDamage + currentDamage);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerId = player.getUniqueId();

        if (hasAbility(player, StickAbility.class) && playerDamageMap.containsKey(playerId)) {
            double accumulatedDamage = playerDamageMap.get(playerId);
            accumulatedDamage *= (1 - DEATH_DAMAGE_REDUCTION);
            playerDamageMap.put(playerId, accumulatedDamage);
        }
    }

    /**
     * 플레이어의 전투가 끝나거나 로그아웃 시 누적 피해량을 초기화하는 메서드
     */
    public static void resetAccumulatedDamage(Player player) {
        playerDamageMap.remove(player.getUniqueId());
    }
}
