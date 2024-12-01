package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;

public class ShieldAbility extends Ability {
    public ShieldAbility(Player player) {
        setName("방패");
        setDescription("왼손에 들면 무적, 오른손에 들면 공격 불가 상태가 됩니다.");
        addRequiredItems(new ItemStack(Material.SHIELD));
    }

    public static String getAbilityName() {
        return "shield";
    }
}
