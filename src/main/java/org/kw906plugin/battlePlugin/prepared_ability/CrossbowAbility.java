package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;

public class CrossbowAbility extends Ability {
    public CrossbowAbility(Player player) {
        setName("쇠뇌");
        setDescription("잃은 체력에 비례해 이동 속도가 증가합니다.");
        addRequiredItems(new ItemStack(Material.CROSSBOW));
        AbilityManager.limitItems(player);
    }
}
