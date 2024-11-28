package org.kw906plugin.battlePlugin.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.kw906plugin.battlePlugin.prepared_ability.AxeAbility;
import org.kw906plugin.battlePlugin.utils.PlayerImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class AxeListener implements Listener {
    private final double healPercentage = config.axeAbilityConfig.healPercentage;
    private final long cooldownTime = config.axeAbilityConfig.cooldown; // 쿨타임을 밀리초 단위로 설정
    private final Map<UUID, Long> lastUsedTimes = new HashMap<>(); // 플레이어의 마지막 사용 시간을 저장하는 맵

    public AxeListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public double getHealPercentage() {
        return healPercentage;
    }

    // 마지막 사용 시간 체크
    private boolean isCooldown(Player player) {
        long currentTime = System.currentTimeMillis();
        UUID playerId = player.getUniqueId();
        long lastUsedTime = lastUsedTimes.getOrDefault(playerId, 0L); // 마지막 사용 시간을 가져옴
        return currentTime - lastUsedTime < cooldownTime; // 쿨타임이 남아 있으면 true 반환
    }

    // 마지막 사용 시간 기록
    private void updateCooldown(Player player) {
        lastUsedTimes.put(player.getUniqueId(), System.currentTimeMillis()); // 플레이어 ID로 마지막 사용 시간 갱신
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            // 플레이어가 AxeAbility 능력을 가지고 있는지 확인
            if (hasAbility(player, AxeAbility.class)) {
                ItemStack mainHandItem = player.getInventory().getItemInMainHand();

                // 도끼로 공격했는지 확인
                if (mainHandItem.getType() == Material.WOODEN_AXE ||
                        mainHandItem.getType() == Material.STONE_AXE ||
                        mainHandItem.getType() == Material.IRON_AXE ||
                        mainHandItem.getType() == Material.GOLDEN_AXE ||
                        mainHandItem.getType() == Material.DIAMOND_AXE ||
                        mainHandItem.getType() == Material.NETHERITE_AXE) {

                    // 쿨타임 체크
                    if (isCooldown(player)) {
                        long lastUsedTime = lastUsedTimes.get(player.getUniqueId());
                        long remainingTime = (cooldownTime - (System.currentTimeMillis() - lastUsedTime)) / 1000; // 남은 쿨타임 계산 (초 단위)
                        player.sendMessage("도끼: 쿨타임 " + remainingTime + "초 남음.");
                        return; // 쿨타임이 끝나지 않으면 능력 사용을 중단
                    }

                    // 쿨타임을 갱신
                    updateCooldown(player);
                    double damageDealt = event.getFinalDamage();
                    double healAmount = damageDealt * getHealPercentage();
                    double maxHealth = PlayerImpl.INITIAL_MAX_HEALTH;
                    double newHealth = Math.min(player.getHealth() + healAmount, maxHealth);
                    player.setHealth(newHealth);
                }
            }
        }
    }
}
