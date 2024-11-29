package org.kw906plugin.battlePlugin.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.Status;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;

public class NoPvPEvent implements Listener {
    private static BukkitTask pvpTimerTask;
    private static boolean pvpDisabled;
    private static final long countDown = config.noPVPCount * 60 * 20;

    public NoPvPEvent(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static void startPvPTimer() {
        pvpDisabled = true;
        Status.setStatus(Status.COUNT_DOWN);

        config.getWorldConfig().getNether().setGameRule(GameRule.KEEP_INVENTORY, true);
        config.getWorldConfig().getOverworld().setGameRule(GameRule.KEEP_INVENTORY, true);
        config.getWorldConfig().getTheEnd().setGameRule(GameRule.KEEP_INVENTORY, true);

        SendMessage.broadcastMessage(Component.text(config.noPVPCount + "분 후 PVP가 가능합니다.")
                                              .color(NamedTextColor.BLUE));

        if (pvpTimerTask != null && !pvpTimerTask.isCancelled()) {
            pvpTimerTask.cancel();
            SendMessage.logConsole("pvpTimerTask가 취소되었습니다.");
        }

        pvpTimerTask = new BukkitRunnable() {
            @Override
            public void run() {
                config.getWorldConfig().getNether().setGameRule(GameRule.KEEP_INVENTORY, false);
                config.getWorldConfig().getOverworld().setGameRule(GameRule.KEEP_INVENTORY, false);
                config.getWorldConfig().getTheEnd().setGameRule(GameRule.KEEP_INVENTORY, false);
                pvpDisabled = false;
                Status.setStatus(Status.RUNNING);
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
                SendMessage.sendActionBar(
                        (Player) event.getDamager(),
                        Component.text("PVP는 " + config.noPVPCount + "분 후에 가능합니다!")
                                 .color(NamedTextColor.RED).decorate(TextDecoration.BOLD)
                );
            }
        }
    }
}

