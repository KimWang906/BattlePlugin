package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;

public class TridentAbility extends Ability implements Listener {
    public TridentAbility(Player player) {
        setName("삼지창");
        setDescription("물 속에 있을 때 체력 증가, 재생, 이동 속도 디버프 없음, 호흡 가능");
        addRequiredItems(new ItemStack(Material.TRIDENT));
        player.getInventory().addItem(new ItemStack(Material.TRIDENT));
    }
}
