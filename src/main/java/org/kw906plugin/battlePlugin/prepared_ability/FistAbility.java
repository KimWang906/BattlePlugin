package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kw906plugin.battlePlugin.Ability;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;

public class FistAbility extends Ability {
    public static final double BASE_HEALTH = config.fistAbilityConfig.baseHealth; // 기본 체력 절반
    public static final double BASE_ATTACK_DAMAGE = config.fistAbilityConfig.baseAttackDamage; // 기본 공격력
    public static final double NETHERITE_INCREASE_HEALTH = config.fistAbilityConfig.increaseHealth; // 네더라이트 주괴 사용 시 증가하는 체력
    public static final double MAX_ALLOWED_HEALTH = config.fistAbilityConfig.maximumHealth; // 최대 체력 제한

    public FistAbility(Player player) {
        setName("주먹");
        setDescription("기본 체력 10, 기본 공격력 6, 포화 상태에서 능력 발동, 네더라이트 주괴 사용 시 체력 증가.");
        AttributeInstance attrHealth = player.getAttribute(Attribute.MAX_HEALTH);
        AttributeInstance attrDamage = player.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attrHealth != null && attrDamage != null) {
            attrHealth.setBaseValue(BASE_HEALTH);
            attrDamage.setBaseValue(BASE_ATTACK_DAMAGE);
        }
        applyEffect(player);
    }

    public static void applyEffect(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, PotionEffect.INFINITE_DURATION, 1));
    }

    public static String getAbilityName() {
        return "fist";
    }
}
