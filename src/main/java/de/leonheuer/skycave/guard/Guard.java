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

import java.lang.reflect.Constructor;
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
        registerCommand("lookup", LookupCommand.class, this);
        registerCommand("kontrolle", KontrolleCommand.class, this);
        registerCommand("countentity", CountEntityCommand.class);
        registerCommand("gc", GCCommand.class);
    }

    @Override
    public void onDisable() {
        dataManager.saveTimeProfile();
    }

    private <T extends CommandExecutor> void registerCommand(String command, Class<T> clazz, Object... parameters) {
        PluginCommand cmd = this.getCommand(command);
        if (cmd == null) {
            getLogger().severe("The command /" + command + " was not found in the plugin.yml.");
            return;
        }
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameters.getClass());
            cmd.setExecutor(constructor.newInstance(parameters));
        } catch (NoSuchMethodException e) {
            getLogger().severe(
                    "No constructor for the executor class of /" + command +
                    " (Class name: " + clazz.getName() + ") matches the specified parameters.");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }

}
