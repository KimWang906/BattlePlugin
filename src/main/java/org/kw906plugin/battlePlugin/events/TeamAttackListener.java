package org.kw906plugin.battlePlugin.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.Status;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
import org.kw906plugin.battlePlugin.prepared_ability.AbilityManager;

public class TeamAttackListener implements Listener {
    public TeamAttackListener(BattlePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        // 게임 상태 확인
        if (Status.getStatus().equals(Status.INITIALIZED) ||
                Status.getStatus().equals(Status.FINISHED) ||
                Status.getStatus().equals(Status.STANDBY) ||
                Status.getStatus().equals(Status.STOPPED)) {
            return;
        }

        Entity damager = event.getDamager(); // 공격자
        Entity victim = event.getEntity();  // 피해자

        // 피해자가 플레이어인지 확인
        if (!(victim instanceof Player target)) return;

        BattlePlayer targetBattlePlayer = AbilityManager.findPlayer(target);

        // 화살(Arrow) 또는 폭발(TNTPrimed)을 통한 간접 공격 확인
        if (damager instanceof Arrow arrow && arrow.getShooter() instanceof Player attacker) {
            handleTeamDamage(attacker, targetBattlePlayer, event);
        } else if (damager instanceof TNTPrimed tnt && tnt.getSource() instanceof Player attacker) {
            handleTeamDamage(attacker, targetBattlePlayer, event);
        }
        // 직접 공격 (플레이어가 공격자인 경우)
        else if (damager instanceof Player attacker) {
            handleTeamDamage(attacker, targetBattlePlayer, event);
        }
    }

    private void handleTeamDamage(Player attacker, BattlePlayer targetBattlePlayer, EntityDamageByEntityEvent event) {
        // 공격자와 피해자의 팀 정보를 가져옴
        BattlePlayer attackerBattlePlayer = AbilityManager.findPlayer(attacker);
        if (attackerBattlePlayer != null && targetBattlePlayer != null) {
            // 같은 팀인지 확인
            if (attackerBattlePlayer.getTeamIndex() == targetBattlePlayer.getTeamIndex()) {
                event.setCancelled(true);
                SendMessage.sendActionBar(attacker, Component.text("같은 팀은 공격할 수 없습니다!")
                                                             .color(NamedTextColor.RED)
                                                             .decorate(TextDecoration.BOLD));
            }
        }
    }
}
