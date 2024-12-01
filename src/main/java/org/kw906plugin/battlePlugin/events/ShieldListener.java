package org.kw906plugin.battlePlugin.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.prepared_ability.ShieldAbility;

import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class ShieldListener implements Listener {
    public ShieldListener(BattlePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasAbility(player, ShieldAbility.class)) {
                ItemStack offHandItem = player.getInventory().getItemInOffHand();
                if (offHandItem.getType() == Material.SHIELD) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (hasAbility(player, ShieldAbility.class)) {
            ItemStack offHandItem = player.getInventory().getItemInOffHand();
            ItemStack handItem = player.getInventory().getItemInMainHand();
            if (offHandItem.getType() == Material.SHIELD || handItem.getType() == Material.SHIELD) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Block block = event.getClickedBlock();
                    if (block != null) {
                        Material blockType = block.getType();
                        if (isShulkerBox(blockType) || blockType.equals(Material.CHEST)) {
                            event.setCancelled(true);
                            player.sendMessage(Component.text(
                                    "방패를 들고 있어 상자를 열 수 없습니다!"
                            ).color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
                        }
                    }
                }
            }
        }
    }

    /**
     * 해당 Material이 셜커 박스인지 확인하는 유틸리티 메서드
     */
    private boolean isShulkerBox(Material material) {
        return material.name().endsWith("_SHULKER_BOX");
    }
}
