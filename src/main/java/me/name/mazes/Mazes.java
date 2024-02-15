package me.name.mazes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.name.mazes.commands.CommandMaze;
import me.name.mazes.commands.CommandPosition;
import me.name.mazes.commands.TabCompletion;
import me.name.mazes.util.LocationTypeAdapter;
import me.name.mazes.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Mazes extends JavaPlugin {

    private static Mazes plugin;

    public static Gson gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationTypeAdapter().nullSafe()).create();

    @Override
    public void onEnable() {
        this.getCommand("position").setExecutor(new CommandPosition());
        this.getCommand("maze").setExecutor(new CommandMaze());

        TabCompletion tabCompletion = new TabCompletion();

        this.getCommand("position").setTabCompleter(tabCompletion);
        this.getCommand("maze").setTabCompleter(tabCompletion);

        plugin = this;

        try {
            Util.loadMazes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Mazes] Plugin has loaded!");
    }

    @Override
    public void onDisable() {
        try {
            Util.saveMazes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Mazes getPlugin() {
        return plugin;
    }
}