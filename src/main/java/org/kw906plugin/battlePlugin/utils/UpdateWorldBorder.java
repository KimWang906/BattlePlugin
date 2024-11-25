package org.kw906plugin.battlePlugin.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.kw906plugin.battlePlugin.SendMessage;

public class UpdateWorldBorder {
    public static void setWorldBorder(World world, int size) {
        if (world != null) {
            WorldBorder worldBorder = world.getWorldBorder();
            worldBorder.setSize(size);
            SendMessage.sendMessageOP(Component.text(world.getName() + " 월드보더 크기가 " + size + "으로 설정되었습니다.")
                                               .color(NamedTextColor.GREEN));
        } else {
            SendMessage.sendMessageOP(Component.text("월드를 찾을 수 없습니다.")
                                               .color(NamedTextColor.RED));
            throw new NullPointerException("world is null");
        }
    }
}