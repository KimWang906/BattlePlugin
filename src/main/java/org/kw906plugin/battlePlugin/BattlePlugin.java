package org.kw906plugin.battlePlugin;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.kw906plugin.battlePlugin.commands.Configure;
import org.kw906plugin.battlePlugin.commands.MainController;
import org.kw906plugin.battlePlugin.events.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final class BattlePlugin extends JavaPlugin {
    public final static String version = "1.0.0";
    public final static String name = "AbilityBattle";
    public static Configure config;
    public static final ArrayList<NamespacedKey> recipeKeys = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = new Configure();
        getServer().recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                recipeKeys.add(shapelessRecipe.getKey());
            } else if (recipe instanceof ShapedRecipe shapedRecipe) {
                recipeKeys.add(shapedRecipe.getKey());
            }
        });

        new ShieldListener(this);
        new AxeListener(this);
        new TridentListener(this);
        new StickListener(this);
        new MaceListener(this);
        new FishingRodListener(this);
        new LighterListener(this);
        new TeamAttackListener(this);
        new InGameListener(this);
        new NoPvPEvent(this);
        Objects.requireNonNull(getCommand("ability-battle")).setExecutor(new MainController());
        getLogger().info("Battle 플러그인이 활성화 되었습니다.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Battle 플러그인이 비활성화 되었습니다.");
        saveConfig();
    }
}
