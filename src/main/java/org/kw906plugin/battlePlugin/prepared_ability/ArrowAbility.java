package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;

import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class ArrowAbility extends Ability implements Listener {

    private final double speedMultiplier = 1.2; // 이동 속도 배율

    public ArrowAbility() {
        setName("활");
        setDescription("활을 들고 있을 시 이동 속도가 증가합니다.");
        addRequiredItems(new ItemStack(Material.BOW));
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());

        if (hasAbility(player, ArrowAbility.class)) {
            AttributeInstance speedAttribute = player.getAttribute(Attribute.MOVEMENT_SPEED);

            if (newItem != null && newItem.getType() == Material.BOW) {
                if (speedAttribute != null) {
                    speedAttribute.setBaseValue(speedAttribute.getDefaultValue() * speedMultiplier);
                }
            } else {
                resetPlayerSpeed(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        resetPlayerSpeed(event.getPlayer());
    }

    private void resetPlayerSpeed(Player player) {
        AttributeInstance speedAttribute = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (speedAttribute != null) {
            speedAttribute.setBaseValue(speedAttribute.getDefaultValue());
        }
    }
}
