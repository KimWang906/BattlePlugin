package org.kw906plugin.battlePlugin;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kw906plugin.battlePlugin.commands.Configure;
import org.kw906plugin.battlePlugin.commands.MainController;
import org.kw906plugin.battlePlugin.events.*;
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
        new ShieldListener(this);
        new AxeListener(this);
        new TridentListener(this);
        new FistListener(this);
        new CrossbowListener(this);
        new StickListener(this);
        new MaceListener(this);
        new SnowballListener(this);
        new FishingRodListener(this);
        new LighterListener(this);
        new ExplorerListener(this);
        new TeamAttackListener(this);
        new PlayerDeathListener(this);
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
