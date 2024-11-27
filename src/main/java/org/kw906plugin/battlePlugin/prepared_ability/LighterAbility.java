package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kw906plugin.battlePlugin.player.BattlePlayer;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class LighterAbility extends Ability implements Listener {
    private final int diamondAmount = config.lighterAbilityConfig.diamondAmount;

    public LighterAbility(Player player) {
        setName("라이터");
        setDescription("아군 전체에게 화염 저항을 부여하고, 가스트의 눈물을 다이아몬드로 변환합니다.");
        if (hasAbility(player, LighterAbility.class)) {
            BattlePlayer battlePlayer = AbilityManager.findPlayer(player);
            if (battlePlayer != null) {
                int team = battlePlayer.getTeamIndex();
                for (BattlePlayer players : AbilityManager.getPlayers()) {
                    if (players.getTeamIndex() == team) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                    }
                }
            }
        }

    }
}
