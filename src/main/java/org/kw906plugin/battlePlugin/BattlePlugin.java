package org.kw906plugin.battlePlugin;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kw906plugin.battlePlugin.commands.Configure;
import org.kw906plugin.battlePlugin.commands.MainController;
import org.kw906plugin.battlePlugin.events.PlayerDeathListener;
import org.kw906plugin.battlePlugin.prepared_ability.*;

import java.util.Objects;

public final class BattlePlugin extends JavaPlugin {
    public final static String version = "1.0.0";
    public final static String name = "CatchTail";
    public static Configure config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = new Configure();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ShieldAbility(), this);
        pm.registerEvents(new AxeAbility(), this);
        pm.registerEvents(new TridentAbility(), this);
        pm.registerEvents(new ArrowAbility(), this);
        pm.registerEvents(new FistAbility(), this);
        pm.registerEvents(new CrossbowAbility(), this);
        pm.registerEvents(new StickAbility(), this);
        pm.registerEvents(new MaceAbility(), this);
        pm.registerEvents(new SnowballAbility(), this);
        pm.registerEvents(new FishingRodAbility(), this);
        pm.registerEvents(new LighterAbility(), this);
        pm.registerEvents(new ExplorerAbility(), this);

        pm.registerEvents(new PlayerDeathListener(), this);
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
