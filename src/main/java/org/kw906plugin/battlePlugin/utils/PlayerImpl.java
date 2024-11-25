package org.kw906plugin.battlePlugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class PlayerImpl {
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
            AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
            assert attribute != null;
            attribute.setBaseValue(20);
            player.heal(20);
            player.setFoodLevel(20);
        }
    }

//    public static void applyArmors(PlayerInfo playerInfo, int color, ItemStack helmet, ItemStack chest, ItemStack leggings, ItemStack boots) {
//        LeatherArmorMeta helmetsMeta = setArmorSetting(playerInfo, color, helmet);
//        LeatherArmorMeta chestMeta = setArmorSetting(playerInfo, color, chest);
//        LeatherArmorMeta leggingsMeta = setArmorSetting(playerInfo, color, leggings);
//        LeatherArmorMeta bootsMeta = setArmorSetting(playerInfo, color, boots);
//
//        helmet.setItemMeta(helmetsMeta);
//        chest.setItemMeta(chestMeta);
//        leggings.setItemMeta(leggingsMeta);
//        boots.setItemMeta(bootsMeta);
//    }
//
//    public static LeatherArmorMeta setArmorSetting(PlayerInfo playerInfo, int color, ItemStack armor) {
//        LeatherArmorMeta armorMeta = (LeatherArmorMeta) armor.getItemMeta();
//        armorMeta.setColor(playerInfo.getColorCode(color));
//        armorMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
//        armorMeta.setUnbreakable(true);
//
//        return armorMeta;
//    }
}

