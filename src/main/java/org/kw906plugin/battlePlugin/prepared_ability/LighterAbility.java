package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.Status;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
import org.kw906plugin.battlePlugin.player.TeamManager;

import java.util.ArrayList;

public class LighterAbility extends Ability {
    public static ArrayList<Player> teams = new ArrayList<>();
    public static BattlePlayer master;

    public LighterAbility(Player player) {
        setName("라이터");
        setDescription("아군 전체에게 화염 저항을 부여하고, 가스트의 눈물을 다이아몬드로 변환합니다.");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Status.isRunning()) {
                    master = AbilityManager.findPlayer(player);
                    if (master != null) {
                        // 팀 정보 가져오기
                        teams = TeamManager.findMyTeams(master.getTeamIndex());

                        // 디버그 로그 추가 (팀원 확인)
                        for (Player teammate : teams) {
                            SendMessage.logConsole("Applying effect to teammate: " + teammate.getName());
                        }

                        // 모든 팀원에게 효과 적용
                        applyEffect();
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(BattlePlugin.getPlugin(BattlePlugin.class), 0L, 20L);
    }

    public static void applyEffect() {
        // 팀원 전체에게 효과 부여
        for (Player teammate : teams) {
            teammate.addPotionEffect(new PotionEffect(
                    PotionEffectType.FIRE_RESISTANCE,
                    Integer.MAX_VALUE, 0,
                    false, false
            ));
        }
    }


    public static String getAbilityName() {
        return "lighter";
    }
}
