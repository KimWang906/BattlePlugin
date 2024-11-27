package org.kw906plugin.battlePlugin;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kw906plugin.battlePlugin.commands.Configure;
import org.kw906plugin.battlePlugin.commands.MainController;
import org.kw906plugin.battlePlugin.events.FistListener;
import org.kw906plugin.battlePlugin.events.LighterListener;
import org.kw906plugin.battlePlugin.events.PlayerDeathListener;
import org.kw906plugin.battlePlugin.events.StickListener;
import org.kw906plugin.battlePlugin.prepared_ability.*;
import org.kw906plugin.battlePlugin.utils.PlayerImpl;

import java.util.Objects;

public final class BattlePlugin extends JavaPlugin {
    public final static String version = "1.0.0";
    public final static String name = "CatchTail";
    public static Configure config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = new Configure();
        PlayerImpl.setAttributeValue();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ShieldAbility(), this);
        pm.registerEvents(new AxeAbility(), this);
        pm.registerEvents(new TridentAbility(), this);
        new FistListener(this);
        pm.registerEvents(new CrossbowAbility(), this);
        new StickListener(this);
        pm.registerEvents(new MaceAbility(), this);
        pm.registerEvents(new SnowballAbility(), this);
        pm.registerEvents(new FishingRodAbility(), this);
        new LighterListener(this);
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
