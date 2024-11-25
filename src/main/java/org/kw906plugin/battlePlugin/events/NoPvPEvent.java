package org.kw906plugin.battlePlugin.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;

public class NoPvPEvent implements Listener {
    private static BukkitTask pvpTimerTask;
    private static boolean pvpDisabled = true;
    private static final long countDown = config.noPVPCount * 60 * 20;

    public NoPvPEvent() {}

    public static void startPvPTimer() {
        SendMessage.broadcastMessage(Component.text(config.noPVPCount + "분 후 PVP가 가능합니다.")
                                              .color(NamedTextColor.BLUE));

        if (pvpTimerTask != null && !pvpTimerTask.isCancelled()) {
            pvpTimerTask.cancel();
        }

        pvpTimerTask = new BukkitRunnable() {
            @Override
            public void run() {
                pvpDisabled = false;
                SendMessage.broadcastMessage(Component.text(config.noPVPCount + "분이 지나 PVP가 활성화 되었습니다!")
                                                      .color(NamedTextColor.BLUE));
            }
        }.runTaskLater(BattlePlugin.getPlugin(BattlePlugin.class), countDown);
    }

    public static void stopPvPTimer() {
        if (pvpTimerTask != null && !pvpTimerTask.isCancelled()) {
            pvpTimerTask.cancel();
            SendMessage.broadcastMessage(Component.text("PVP 제한 타이머가 중지되었습니다.")
                                                  .color(NamedTextColor.RED));
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (pvpDisabled) {
                event.setCancelled(true);
                ((Player) event.getDamager()).sendMessage("PVP는 " + config.noPVPCount + "분 후에 가능합니다!");
            }
        }
    }
}

