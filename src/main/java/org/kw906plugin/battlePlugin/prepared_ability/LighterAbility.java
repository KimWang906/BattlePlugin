package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.Status;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
public class LighterAbility extends Ability {
    public LighterAbility(Player player) {
        setName("라이터");
        setDescription("아군 전체에게 화염 저항을 부여하고, 가스트의 눈물을 다이아몬드로 변환합니다.");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Status.isRunning()) {
                    applyEffect(player);
                    this.cancel();
                }
            }
        }.runTaskTimer(BattlePlugin.getPlugin(BattlePlugin.class), 0L, 20L);
    }

    private void applyEffect(Player player) {
        BattlePlayer battlePlayer = AbilityManager.findPlayer(player);
        if (battlePlayer != null) {
            int team = battlePlayer.getTeamIndex();
            for (BattlePlayer players : AbilityManager.getPlayers()) {
                if (players.getTeamIndex() == team) {
                    Player teamPlayer = players.getPlayer();
                    if (teamPlayer != null) {
                        teamPlayer.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                    }
                }
            }
        }
    }
}
