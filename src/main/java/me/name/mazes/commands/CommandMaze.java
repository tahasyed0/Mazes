package me.name.mazes.commands;

import me.name.mazes.MazeStuff.Maze;
import me.name.mazes.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandMaze implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("maze")) {

            if (args.length == 0) {
                Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze (create | generate | delete | reset | solve | unsolve | list | options)");
            }

            else if (Arrays.asList("create", "generate", "delete", "reset", "solve", "unsolve", "options").contains(args[0].toLowerCase()) && args.length == 1) {
                if (!args[0].equalsIgnoreCase("options")) {
                    Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze " + args[0].toLowerCase() + " [name]");
                } else {
                    Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze options [name] (setheight | setfloor | setwalls)");
                }
            }

            else if (args.length == 1) {
                if (!args[0].equalsIgnoreCase("list")) {
                    Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze (create | generate | delete | reset | solve | unsolve | list | options)");
                }
                else if (Maze.mazeList.size() == 0) {
                    Util.sendMessage(sender, "&a[Mazes] &7You currently do not have any mazes.");
                } else {
                    Util.sendMessage(sender, "&a[Mazes] &7Your mazes: &f" + String.join(", ", Maze.mazeList.keySet()));
                }
            }

            else if (!args[0].equalsIgnoreCase("create") && !Maze.mazeList.containsKey(args[1].toLowerCase())) {
                Util.sendMessage(sender, "&a[Mazes] &4Error: &7Maze not found.");
            }

            else if (args[0].equalsIgnoreCase("create")) {
                if (!(sender instanceof Player)) {
                    Util.sendMessage(sender, "&4[Mazes] Error: Only players can perform this command!");
                    return true;
                }
                Player p = (Player) sender;
                if (!Maze.mazeList.containsKey(args[1].toLowerCase())) {
                    if (CommandPosition.corner1.get(p) == null || CommandPosition.corner2.get(p) == null) {
                        Util.sendMessage(sender, "&a[Mazes] &4Error: &7Please select the start and end using /position:");
                        Util.sendMessage(sender, "&a[Mazes]          &7Start: " + (CommandPosition.corner1.get(p) == null ? "&4Not Set" : "&aSet"));
                        Util.sendMessage(sender, "&a[Mazes]          &7End: " + (CommandPosition.corner2.get(p) == null ? "&4Not Set" : "&aSet"));

                        return true;
                    }
                    Location start = CommandPosition.corner1.get(p);
                    Location end = CommandPosition.corner2.get(p);
                    end.setY(start.getY());
                    new Maze(start, end, args[1]);
                    Util.sendMessage(sender, "&a[Mazes] Maze created successfully!");
                } else {
                    Util.sendMessage(sender, "&a[Mazes] &4Error: &7A maze with that name already exists.");
                }
            }

            else if (args[0].equalsIgnoreCase("generate")) {
                Maze.mazeList.get(args[1].toLowerCase()).generate();
                Util.sendMessage(sender, "&a[Mazes] Maze generated successfully!");
            }

            else if (args[0].equalsIgnoreCase("delete")) {
                Maze.mazeList.remove(args[1].toLowerCase());
                Util.sendMessage(sender, "&a[Mazes] Maze deleted successfully!");
            }

            else if (args[0].equalsIgnoreCase("reset")) {
                Maze.mazeList.get(args[1].toLowerCase()).reset();
                Util.sendMessage(sender, "&a[Mazes] Maze reset successfully!");
            }

            else if (args[0].equalsIgnoreCase("solve")) {
                if (Maze.mazeList.get(args[1].toLowerCase()).solve()) {
                    Util.sendMessage(sender, "&a[Mazes] Maze solved successfully!");
                } else {
                    Util.sendMessage(sender, "&a[Mazes] &4Error: &7Maze does not have a solution.");
                }
            }

            else if (args[0].equalsIgnoreCase("unsolve")) {
                if (Maze.mazeList.get(args[1].toLowerCase()).unsolve()) {
                    Util.sendMessage(sender, "&a[Mazes] Maze unsolved successfully!");
                } else {
                    Util.sendMessage(sender, "&a[Mazes] &4Error: &7Maze does not have a solution.");
                }
            }

            else if (args[0].equalsIgnoreCase("options")) {
                switch (args.length) {
                    case 2:
                        Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze options " + args[1].toLowerCase() + " (setheight | setfloor | setwalls)");
                        break;
                    case 3:
                        if (args[2].equalsIgnoreCase("setwalls") || args[2].equalsIgnoreCase("setfloor")) {
                            Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze " + args[0].toLowerCase() + " " + args[1].toLowerCase() + " " + args[2].toLowerCase() + " <block>");
                        } else if (args[2].equalsIgnoreCase("setheight")) {
                            Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze " + args[0].toLowerCase() + " " + args[1].toLowerCase() + " " + args[2].toLowerCase() + " <integer>");
                        } else {
                            Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze " + args[0].toLowerCase() + " " + args[1].toLowerCase() + " (setheight | setfloor | setwalls)");
                        }
                        break;
                    case 4:
                        if (args[2].equalsIgnoreCase("setwalls")) {
                            Material m = Material.getMaterial(args[3].toUpperCase());
                            if (m == null) {
                                Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze " + args[0].toLowerCase() + " " + args[1].toLowerCase() + " " + args[2].toLowerCase() + " <block>");
                            } else if (!m.isBlock()) {
                                Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze " + args[0].toLowerCase() + " " + args[1].toLowerCase() + " " + args[2].toLowerCase() + " <block>");
                            } else {
                                Maze.mazeList.get(args[1].toLowerCase()).setWallType(m);
                                Util.sendMessage(sender, "&a[Mazes] Maze walls set successfully!");
                            }
                        } else if (args[2].equalsIgnoreCase("setfloor")) {
                            Material m = Material.getMaterial(args[3].toUpperCase());
                            if (m == null) {
                                Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze " + args[0].toLowerCase() + " " + args[1].toLowerCase() + " " + args[2].toLowerCase() + " <block>");
                            } else if (!m.isBlock()) {
                                Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze " + args[0].toLowerCase() + " " + args[1].toLowerCase() + " " + args[2].toLowerCase() + " <block>");
                            } else {
                                Maze.mazeList.get(args[1].toLowerCase()).setFloorType(m);
                                Util.sendMessage(sender, "&a[Mazes] Maze floor set successfully!");
                            }
                        } else if (args[2].equalsIgnoreCase("setheight")) {
                            try {
                                int h = Integer.parseInt(args[3]);
                                Maze.mazeList.get(args[1].toLowerCase()).setHeight(h);
                                Util.sendMessage(sender, "&a[Mazes] Maze height set successfully!");
                            } catch (NumberFormatException e) {
                                Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze " + args[0].toLowerCase() + " " + args[1].toLowerCase() + " " + args[2].toLowerCase() + " <integer>");
                            }
                        } else {
                            Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze " + args[0].toLowerCase() + " " + args[1].toLowerCase() + " (setheight | setfloor | setwalls)");
                        }
                        break;
                    default:
                        Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze options [name] (setheight | setfloor | setwalls)");
                        break;
                }
            }

            else {
                Util.sendMessage(sender, "&a[Mazes] &4Usage: &7/maze (create | generate | delete | reset | solve | unsolve | list | options)");
            }
            return true;
        }
        return false;
    }
}
