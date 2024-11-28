package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;

public class AxeAbility extends Ability {

    public AxeAbility(Player player) {
        setName("도끼");
        setDescription("대상을 공격 시 데미지 일부를 회복합니다.");
        addRequiredItems(
                new ItemStack(Material.WOODEN_AXE),
                new ItemStack(Material.STONE_AXE),
                new ItemStack(Material.IRON_AXE),
                new ItemStack(Material.GOLDEN_AXE),
                new ItemStack(Material.DIAMOND_AXE),
                new ItemStack(Material.NETHERITE_AXE)
        );
        AbilityManager.limitItems(player);
    }
}
