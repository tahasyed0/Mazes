package me.name.mazes.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandPosition implements CommandExecutor {

    public static HashMap<Player, Location> corner1 = new HashMap<>();
    public static HashMap<Player, Location> corner2 = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("position")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length != 1) {
                    p.sendMessage(ChatColor.RED + "[Mazes] Error: Command Usage: /" + label + " (start | end)");
                } else if (args[0].equalsIgnoreCase("start")) {
                    Block b = p.getTargetBlockExact(10);
                    if (b == null) {
                        p.sendMessage(ChatColor.RED + "[Mazes] Error: Please look at the block you would like to select!");
                    } else {
                        CommandPosition.corner1.put(p, b.getLocation());
                        p.sendMessage(ChatColor.GREEN + "[Mazes] Successfully set the start to your target block!");
                    }
                } else if (args[0].equalsIgnoreCase("end")) {
                    Block b = p.getTargetBlockExact(10);
                    if (b == null) {
                        p.sendMessage(ChatColor.RED + "[Mazes] Error: Please look at the block you would like to select!");
                    } else {
                        CommandPosition.corner2.put(p, b.getLocation());
                        p.sendMessage(ChatColor.GREEN + "[Mazes] Successfully set the end to your target block!");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "[Mazes] Error: Command Usage: /" + label + " (start | end)");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[Mazes] Error: Only players can perform this command!");
            }
            return true;
        }
        return false;
    }
}
