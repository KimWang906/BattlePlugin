package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kw906plugin.battlePlugin.Ability;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class FishingRodAbility extends Ability implements Listener {
    private final int diamondAmount = config.fishingRodAbilityConfig.diamondAmount;

    public FishingRodAbility() {
        setName("낚싯대 능력");
        setDescription("낚싯대 사용 시 행운 최대치와 마을의 영웅 효과를 부여하고, 인챈트북을 우클릭 시 다이아 능력을 부여합니다.");
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.FISHING_ROD && hasAbility(player, FishingRodAbility.class)) {
            AttributeInstance playerAttr = player.getAttribute(Attribute.LUCK);
            if (playerAttr != null) {
                playerAttr.setBaseValue(1024);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, Integer.MAX_VALUE, 1));
        }

        if (item != null && item.getType() == Material.ENCHANTED_BOOK && hasAbility(player, FishingRodAbility.class) && event.getAction().isRightClick()) {
            grantDiamondAbility(player);
            item.setAmount(item.getAmount() - 1);
        }
    }

    private void grantDiamondAbility(Player player) {
        ItemStack diamond = new ItemStack(Material.DIAMOND, diamondAmount);
        player.getInventory().addItem(diamond);
    }
}
