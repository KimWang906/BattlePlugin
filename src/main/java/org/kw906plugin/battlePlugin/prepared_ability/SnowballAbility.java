package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;

public class SnowballAbility extends Ability {
    public SnowballAbility(Player player) {
        setName("눈덩이");
        setDescription("상대에게 눈덩이 적중 시 동상 효과와 슬로우 효과를 부여합니다. 동일한 상대에게 적중 시 동상 상태가 해제되면서 추가 데미지를 입힙니다.");
        addRequiredItems(new ItemStack(Material.SNOWBALL));
        AbilityManager.limitItems(player);
    }
}
