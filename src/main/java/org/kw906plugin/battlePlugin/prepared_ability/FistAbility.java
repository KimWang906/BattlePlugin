package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kw906plugin.battlePlugin.Ability;

public class FistAbility extends Ability {
    public FistAbility(Player player) {
        setName("주먹");
        setDescription("기본 체력 10, 기본 공격력 6, 포화 상태에서 능력 발동, 네더라이트 주괴 사용 시 체력 증가.");
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 1));
    }
}
