package org.kw906plugin.battlePlugin;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
import org.kw906plugin.battlePlugin.prepared_ability.AbilityManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Ability {
    private String name;
    private String description;
    private static final ArrayList<ItemStack> requiredItems = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ArrayList<ItemStack> getRequiredItems() {
        return requiredItems;
    }

    public void addRequiredItems(ItemStack... items) {
        requiredItems.addAll(Arrays.stream(items).toList());
    }
}
