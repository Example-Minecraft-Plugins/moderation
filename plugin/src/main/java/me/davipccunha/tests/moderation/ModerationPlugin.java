package me.davipccunha.tests.moderation;

import lombok.Getter;
import me.davipccunha.tests.moderation.api.ModerationAPI;
import me.davipccunha.tests.moderation.api.model.Ban;
import me.davipccunha.tests.moderation.api.model.BanIP;
import me.davipccunha.tests.moderation.api.model.Mute;
import me.davipccunha.tests.moderation.command.*;
import me.davipccunha.tests.moderation.listener.AsyncPlayerPreLoginListener;
import me.davipccunha.tests.moderation.provider.ModerationProvider;
import me.davipccunha.utils.cache.RedisCache;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ModerationPlugin extends JavaPlugin {
    private RedisCache<Ban> bansCache;
    private RedisCache<Mute> mutesCache;
    private RedisCache<BanIP> ipBansCache;

    public void onEnable() {
        this.init();
        getLogger().info("Moderation plugin loaded!");
    }

    public void onDisable() {
        getLogger().info("Moderation plugin unloaded!");
    }

    private void init() {
        saveDefaultConfig();
        this.registerListeners(
                new AsyncPlayerPreLoginListener(this)
        );
        this.registerCommands();

        this.loadCaches();

        Bukkit.getServicesManager().register(ModerationAPI.class, new ModerationProvider(this), this, ServicePriority.Normal);
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        for (Listener listener : listeners) pluginManager.registerEvents(listener, this);
    }

    private void registerCommands() {
        getCommand("expulsar").setExecutor(new ExpulsarCommand());
        getCommand("silenciar").setExecutor(new SilenciarCommand(this));
        getCommand("dessilenciar").setExecutor(new DessilenciarCommand(this));
        getCommand("banir").setExecutor(new BanirCommand(this));
        getCommand("desbanir").setExecutor(new DesbanirCommand(this));
        getCommand("checarpunicao").setExecutor(new ChecarpunicaoCommand(this));
    }

    private void loadCaches() {
        this.bansCache = new RedisCache<>("moderation:bans", Ban.class);
        this.mutesCache = new RedisCache<>("moderation:mutes", Mute.class);
        this.ipBansCache = new RedisCache<>("moderation:ipbans", BanIP.class);
    }
}
