package com.enderdevelopment.plugin;

import com.enderdevelopment.plugin.listener.AnvilListener;
import com.enderdevelopment.plugin.listener.CraftListener;
import com.enderdevelopment.plugin.listener.DeathListener;
import com.enderdevelopment.plugin.listener.EssenceListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        ProtectionManager.init(this);
        getServer().getPluginManager().registerEvents(new EssenceListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftListener(this), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getCommand("everlast").setExecutor(new EverlastCommand(this));
        getLogger().info("Everlast enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Everlast disabled.");
    }
}
