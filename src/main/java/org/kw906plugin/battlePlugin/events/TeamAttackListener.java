package org.kw906plugin.battlePlugin.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
        if (Status.getStatus().equals(Status.INITIALIZED) ||
                Status.getStatus().equals(Status.FINISHED) ||
                Status.getStatus().equals(Status.STANDBY) ||
                Status.getStatus().equals(Status.STOPPED)) {
            return;
        }

        Entity damager = event.getDamager();
        Entity victim = event.getEntity();

        if (damager instanceof Player attacker && victim instanceof Player target) {
            BattlePlayer attackerBattlePlayer = AbilityManager.findPlayer(attacker);
            BattlePlayer targetBattlePlayer = AbilityManager.findPlayer(target);

            if (attackerBattlePlayer != null && targetBattlePlayer != null) {
                if (attackerBattlePlayer.getTeamIndex() == targetBattlePlayer.getTeamIndex()) {
                    event.setCancelled(true);
                    SendMessage.sendActionBar(attacker, Component.text("같은 팀은 공격할 수 없습니다!")
                                                                 .color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
                }
            }
        }
    }
}
