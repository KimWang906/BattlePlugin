package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.events.CrossbowListener;

import java.util.HashSet;
import java.util.Set;

import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class CrossbowAbility extends Ability {
    public static Set<Player> hitPlayers = new HashSet<>();

    public CrossbowAbility(Player player) {
        setName("쇠뇌");
        setDescription("잃은 체력에 비례해 이동 속도가 증가합니다.");
        addRequiredItems(new ItemStack(Material.CROSSBOW));
        AbilityManager.limitItems(player);
    }

    public static void onDeathCrossbow(Player player) {
        if (hasAbility(player, CrossbowAbility.class)) {
            AttributeInstance attr = player.getAttribute(Attribute.MOVEMENT_SPEED);
            if (attr != null) {
                attr.setBaseValue(CrossbowListener.BASE_WALK_SPEED);
            }
        }
    }

    // 버프 적용 메서드
    public static void applyBuffToShooter(Player shooter) {
        // 예: 속도 증가 버프 (1레벨, 30초)
        shooter.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 30, 1));
        shooter.sendMessage("2명 이상을 맞혀서 속도 버프가 적용되었습니다!");
    }
}
