package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;

import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class ShieldAbility extends Ability implements Listener {
    public ShieldAbility() {
        setName("방패");
        setDescription("오프핸드에 들면 무적, 메인핸드에 들면 공격 불가 상태가 됩니다.");
        addRequiredItems(new ItemStack(Material.SHIELD));
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasAbility(player, ShieldAbility.class)) {
                ItemStack offHandItem = player.getInventory().getItemInOffHand();
                if (offHandItem.getType() == Material.SHIELD) {
                    event.setCancelled(true);
                    player.sendMessage("무적 상태입니다!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (hasAbility(player, ShieldAbility.class)) {
                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                if (mainHandItem.getType() == Material.SHIELD) {
                    // 방패를 오른손에 들었을 때 공격 불가
                    event.setCancelled(true);
                    player.sendMessage("방패를 들고 있어 공격할 수 없습니다.");
                }
            }
        }
    }
}
