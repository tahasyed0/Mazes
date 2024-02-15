package me.name.mazes.commands;

import me.name.mazes.MazeStuff.Maze;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("maze")) {
            switch (args.length) {
                case 1:
                    return Arrays.asList("create", "generate", "delete", "reset", "solve", "unsolve", "list", "options");
                case 2:
                    if (Arrays.asList("generate", "delete", "reset", "solve", "unsolve", "options").contains(args[0].toLowerCase())) {
                        return new ArrayList<>(Maze.mazeList.keySet());
                    }
                    break;
                case 3:
                    if (args[0].equalsIgnoreCase("options")) {
                        return Arrays.asList("setfloor", "setwalls", "setheight");
                    }
                    break;
                case 4:
                    if (args[0].equalsIgnoreCase("options") && Arrays.asList("setfloor", "setwalls").contains(args[2].toLowerCase())) {
                        return Stream.of(Material.values())
                                .filter(Material::isBlock)
                                .map(Material::toString)
                                .map(String::toLowerCase)
                                .filter(s -> s.startsWith(args[3].toLowerCase()))
                                .collect(Collectors.toList());
                    }
                    break;
                default:
                    break;
            }
        } else if (command.getName().equalsIgnoreCase("position")) {
            if (args.length == 1) {
                return Arrays.asList("start", "end");
            }
        }
        return null;
    }

}
