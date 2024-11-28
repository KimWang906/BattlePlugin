package org.kw906plugin.battlePlugin.events;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.Status;
import org.kw906plugin.battlePlugin.player.TeamManager;
import org.kw906plugin.battlePlugin.prepared_ability.AbilityManager;
import org.kw906plugin.battlePlugin.player.BattlePlayer;

public class PlayerDeathListener implements Listener {
    public PlayerDeathListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!Status.isRunning())
            return;

        Player deadPlayer = event.getEntity();
        Player killer = deadPlayer.getKiller();

        if (killer != null) {
            BattlePlayer battlePlayer = AbilityManager.findPlayer(killer);
            if (battlePlayer != null) {
                int teamIndex = battlePlayer.getTeamIndex();
                TeamManager.addTeamScore(teamIndex, 1);
                SendMessage.broadcastMessage(
                        Component.text(teamIndex + "팀이 " + deadPlayer.getName() +
                                               "을 죽이고 " + 1 + "점을 획득하였습니다!"));
            }
        }
    }
}
