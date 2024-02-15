package me.name.mazes.util;

import com.google.common.reflect.TypeToken;
import me.name.mazes.MazeStuff.Maze;
import me.name.mazes.Mazes;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class Util {
    public static void sendMessage(CommandSender recipient, String message) {
        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void saveMazes() throws IOException {
        File file = new File(Mazes.getPlugin().getDataFolder().getAbsoluteFile() + "/mazes.json");
        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);
        Mazes.gson.toJson(Maze.mazeList, writer);
        writer.flush();
        writer.close();
        System.out.println("Mazes saved.");
    }

    public static void loadMazes() throws IOException {
        File file = new File(Mazes.getPlugin().getDataFolder().getAbsoluteFile() + "/mazes.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            Type type = new TypeToken<HashMap<String, Maze>>(){}.getType();
            Maze.mazeList = Mazes.gson.fromJson(reader, type);
            System.out.println("Mazes loaded.");
        }
    }
}
