package net.iegmann.mcbs.mcbs;

import org.bukkit.plugin.java.JavaPlugin;

public final class MCBS extends JavaPlugin {
    public static MCBS plugin;
    public static MCBS getInstance(){
        return plugin;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
