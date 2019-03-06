package net.iegmann.mcbs.mcbs;

import net.iegmann.mcbs.mcbs.commands.RunManager;
import net.iegmann.mcbs.mcbs.managers.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * クラスMCBSは本プラグインのメインクラスです.
 * ここではプラグインそのものの管理に関する処理（ex.コマンド登録）をします.
 */
public final class MCBS extends JavaPlugin {
    public static MCBS plugin;
    public static MCBS getInstance(){
        return plugin;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        GameManager.getInstance();
        this.getCommand("run").setExecutor(new RunManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
