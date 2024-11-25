package org.kw906plugin.battlePlugin;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class Ability {
    private String name;
    private String description;
    private final ArrayList<ItemStack> requiredItems = new ArrayList<>();

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

    public ArrayList<ItemStack> getRequiredItems() {
        return requiredItems;
    }

    public void addRequiredItems(ItemStack... items) {
        requiredItems.addAll(Arrays.stream(items).toList());
    }
}
