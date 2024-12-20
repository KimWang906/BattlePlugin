package org.kw906plugin.battlePlugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.kw906plugin.battlePlugin.SendMessage;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class PlayerImpl {
    private static final Map<Attribute, Double> modifyAttributeMap = new HashMap<>();

    // Version: Minecraft 1.21.3
    public static final double INITIAL_MAX_HEALTH = 20.0;
    public static final double INITIAL_KNOCKBACK_RESISTANCE = 0.0;
    public static final double INITIAL_MOVEMENT_SPEED = 0.1;
    public static final double INITIAL_ATTACK_DAMAGE = 1.0;
    public static final double INITIAL_ARMOR = 0.0;
    public static final double INITIAL_ARMOR_TOUGHNESS = 0.0;
    public static final double INITIAL_ATTACK_KNOCKBACK = 0.0;
    public static final double INITIAL_ATTACK_SPEED = 4.0;
    public static final double INITIAL_LUCK = 0.0;
    public static final double INITIAL_JUMP_STRENGTH = 0.42;

    static {
        setAttributeValue();
    }

    public static void setAttributeValue() {
        modifyAttributeMap.put(Attribute.MAX_HEALTH, INITIAL_MAX_HEALTH);
        modifyAttributeMap.put(Attribute.KNOCKBACK_RESISTANCE, INITIAL_KNOCKBACK_RESISTANCE);
        modifyAttributeMap.put(Attribute.MOVEMENT_SPEED, INITIAL_MOVEMENT_SPEED);
        modifyAttributeMap.put(Attribute.ATTACK_DAMAGE, INITIAL_ATTACK_DAMAGE);
        modifyAttributeMap.put(Attribute.ARMOR, INITIAL_ARMOR);
        modifyAttributeMap.put(Attribute.ARMOR_TOUGHNESS, INITIAL_ARMOR_TOUGHNESS);
        modifyAttributeMap.put(Attribute.ATTACK_KNOCKBACK, INITIAL_ATTACK_KNOCKBACK);
        modifyAttributeMap.put(Attribute.ATTACK_SPEED, INITIAL_ATTACK_SPEED);
        modifyAttributeMap.put(Attribute.LUCK, INITIAL_LUCK);
        modifyAttributeMap.put(Attribute.JUMP_STRENGTH, INITIAL_JUMP_STRENGTH);
    }

    public static Player getPlayerByName(String name) {
        for (Player onlinePlayer : getOnlinePlayers()) {
            if (onlinePlayer.getName().equalsIgnoreCase(name)) {
                return onlinePlayer;
            }
        }
        return null;
    }

    public static void removeAllItemsAndArmor(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public static void setFullCondition() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeAllItemsAndArmor(player);
            // MAX_HEALTH 속성 값 얻기
            AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
            if (attribute != null) {
                attribute.setBaseValue(INITIAL_MAX_HEALTH); // 최대 체력 설정
                player.setHealth(attribute.getBaseValue()); // 체력 회복
            }
            player.setFoodLevel(20); // 음식 레벨 최대화
            SendMessage.logConsole("Player " + player.getName() + " is now full condition");
        }
    }

    public static void resetPlayerAbility() {
        for (Player player : getOnlinePlayers()) {
            player.clearActivePotionEffects(); // 모든 포션 효과 제거
            for (Map.Entry<Attribute, Double> entry : modifyAttributeMap.entrySet()) {
                Attribute attribute = entry.getKey();
                double value = entry.getValue();
                AttributeInstance attributeInstance = player.getAttribute(attribute);
                if (attributeInstance != null) {
                    attributeInstance.setBaseValue(value); // 속성 값 초기화
                }
            }
            SendMessage.logConsole("Player " + player.getName() + " is now reset ability");
        }
    }
}
