package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.entity.Player;
import org.kw906plugin.battlePlugin.Ability;

public class StickAbility extends Ability {
    public StickAbility(Player player) {
        setName("부러지지 않는 막대");
        setDescription("나무 막대로 공격 시 누적 피해량에 비례해 데미지가 증가합니다.");
    }

    public static String getAbilityName() {
        return "stick";
    }
}
