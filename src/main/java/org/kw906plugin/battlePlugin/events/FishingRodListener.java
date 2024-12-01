package org.kw906plugin.battlePlugin.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.prepared_ability.FishingRodAbility;

import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;
import static org.kw906plugin.battlePlugin.prepared_ability.FishingRodAbility.diamondAmount;

public class FishingRodListener implements Listener {
    public FishingRodListener(BattlePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && item.getType().equals(Material.ENCHANTED_BOOK) &&
                hasAbility(player, FishingRodAbility.class) && event.getAction().isRightClick()) {
            grantDiamondAbility(player);
            item.setAmount(Math.max(item.getAmount() - 1, 0));
        }
    }

    private void grantDiamondAbility(Player player) {
        ItemStack diamond = new ItemStack(Material.DIAMOND, diamondAmount);
        player.getInventory().addItem(diamond);
    }
}
