package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;

public class ArrowAbility extends Ability {
    public ArrowAbility(Player player) {
        setName("활");
        setDescription("이동 속도가 증가합니다.");
        addRequiredItems(new ItemStack(Material.BOW));
        AttributeInstance speedAttribute = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (speedAttribute != null) {
            double speed = config.arrowAbilityConfig.speed;
            speedAttribute.setBaseValue(speed);
        }
    }
}
