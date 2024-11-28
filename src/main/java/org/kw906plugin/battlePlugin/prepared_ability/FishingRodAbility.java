package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;

public class FishingRodAbility extends Ability {
    public FishingRodAbility(Player player) {
        setName("낚싯대 능력");
        setDescription("낚싯대 사용 시 행운 최대치와 마을의 영웅 효과를 부여하고, 인챈트북을 우클릭 시 다이아 능력을 부여합니다.");
        addRequiredItems(new ItemStack(Material.FISHING_ROD));
        AbilityManager.limitItems(player);
    }
}
