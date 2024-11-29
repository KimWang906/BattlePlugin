package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;

public class WizardAbility extends Ability {
    public static final int poisonDuration = config.wizardAbilityConfig.poisonDuration;
    public static final int poisonAmplifier = config.wizardAbilityConfig.poisonAmplifier;
    public static final int instantDamageAmplifier = config.wizardAbilityConfig.instantDamageAmplifier;

    public WizardAbility(Player player) {
        setName("마법사");
        setDescription("발효된 거미의 눈을 상호작용 시 다음과 같이 동작합니다.\n좌클릭 시: 독 포션으로 변합니다.\n우클릭 시: 즉시 고통의 물약으로 변합니다.");
        addRequiredItems(new ItemStack(Material.FERMENTED_SPIDER_EYE));
    }
}
