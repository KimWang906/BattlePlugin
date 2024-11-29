package org.kw906plugin.battlePlugin.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.prepared_ability.LighterAbility;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class LighterListener implements Listener {
    private final int diamondAmount = config.lighterAbilityConfig.diamondAmount;

    public LighterListener(BattlePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // 가스트의 눈물 사용 시 다이아몬드로 변환
    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.GHAST_TEAR &&
                hasAbility(player, LighterAbility.class) && event.getAction().isRightClick()) {
            grantDiamondAbility(player);
            item.setAmount(item.getAmount() - 1);
        }
    }

    private void grantDiamondAbility(Player player) {
        ItemStack diamond = new ItemStack(Material.DIAMOND, diamondAmount);
        player.getInventory().addItem(diamond);
    }
}
