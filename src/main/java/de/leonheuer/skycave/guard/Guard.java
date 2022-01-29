package de.leonheuer.skycave.guard;

import de.leonheuer.skycave.guard.commands.*;
import de.leonheuer.skycave.guard.listener.PlayerChangedWorldListener;
import de.leonheuer.skycave.guard.listener.PlayerJoinLeaveListener;
import de.leonheuer.skycave.guard.listener.PlayerLoginListener;
import de.leonheuer.skycave.guard.storage.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Guard extends JavaPlugin {

    private DataManager dataManager;
    public final List<Player> spectators = new ArrayList<>();

    @Override
    public void onEnable() {
        dataManager = new DataManager();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerLoginListener(this), this);
        pm.registerEvents(new PlayerJoinLeaveListener(this), this);
        pm.registerEvents(new PlayerChangedWorldListener(this), this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        registerCommand("aacn", NotifyCommand.class);
        registerCommand("kickall", KickallCommand.class);
        registerCommand("lookup", LookupCommand.class);
        registerCommand("kontrolle", KontrolleCommand.class);
        registerCommand("countentity", CountEntityCommand.class);
        registerCommand("gc", GCCommand.class);
    }

    @Override
    public void onDisable() {
        dataManager.saveTimeProfile();
    }

    private void registerCommand(String command, Class<? extends CommandExecutor> clazz) {
        PluginCommand cmd = this.getCommand(command);
        if (cmd == null) {
            getLogger().severe("The command /" + command + " was not found in the plugin.yml.");
            return;
        }
        try {
            cmd.setExecutor(clazz.getDeclaredConstructor().newInstance());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }

}
