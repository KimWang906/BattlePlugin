package org.kw906plugin.battlePlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import static org.bukkit.Bukkit.getServer;

public enum Status {
    STANDBY(NamedTextColor.YELLOW),
    INITIALIZED(NamedTextColor.GREEN),
    GAME_SETUP(NamedTextColor.GOLD),
    RUNNING(NamedTextColor.GREEN),
    COUNT_DOWN(NamedTextColor.BLUE),
    FINISHED(NamedTextColor.AQUA),
    STOPPED(NamedTextColor.RED),
    BREAK_PRD(NamedTextColor.LIGHT_PURPLE);

    private NamedTextColor color;

    Status(NamedTextColor color) {
        this.color = color;
    }

    public NamedTextColor getColor() {
        return color;
    }

    private static Status currentStatus = Status.STANDBY;

    public static void setStatus(Status newStatus) {
        SendMessage.sendMessageOP(Component.text("게임상태가 변경되었습니다. %old% → %new%"
                                                         .replace("%old%", currentStatus.toString())
                                                         .replace("%new%", newStatus.toString()))
                                           .color(NamedTextColor.GRAY));
        currentStatus = newStatus;
    }

    public static Status getStatus() {
        return currentStatus;
    }

    public static void broadcastStatus() {
        getServer().broadcast(getMessage());
    }

    public static Component getMessage() {
        return Component.text("게임상태 : " + currentStatus.color + currentStatus.toString())
                        .color(NamedTextColor.GOLD);
    }

    public static boolean isRunning() {
        return currentStatus == Status.RUNNING || currentStatus == Status.COUNT_DOWN;
    }
}
