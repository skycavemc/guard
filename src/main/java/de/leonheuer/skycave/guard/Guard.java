package de.leonheuer.skycave.guard;

import de.leonheuer.skycave.guard.commands.*;
import de.leonheuer.skycave.guard.listener.PlayerChangedWorldListener;
import de.leonheuer.skycave.guard.listener.PlayerJoinLeaveListener;
import de.leonheuer.skycave.guard.listener.PlayerLoginListener;
import de.leonheuer.skycave.guard.storage.DataManager;
import de.leonheuer.skycave.guard.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Guard extends JavaPlugin {

    private Util util;
    private DataManager dm;

    @Override
    public void onEnable() {
        registerClasses();
        registerListener(Bukkit.getPluginManager());
        registerCommands();
    }

    @Override
    public void onDisable() {
        dm.saveTimeProfile();
    }

    private void registerClasses() {
        util = new Util();
        dm = new DataManager(this);
    }

    private void registerListener(PluginManager pm) {
        pm.registerEvents(new PlayerLoginListener(this), this);
        pm.registerEvents(new PlayerJoinLeaveListener(this), this);
        pm.registerEvents(new PlayerChangedWorldListener(this), this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void registerCommands() {
        this.getCommand("aacn").setExecutor(new NotifyCommand());
        this.getCommand("kickall").setExecutor(new KickallCommand());
        this.getCommand("lookup").setExecutor(new LookupCommand(this));
        this.getCommand("kontrolle").setExecutor(new KontrolleCommand(this));
        this.getCommand("countentity").setExecutor(new CountEntityCommand());
        this.getCommand("gc").setExecutor(new GCCommand());
    }

    public Util getUtil() {
        return util;
    }

    public DataManager getDm() {
        return dm;
    }

}
