package me.name.mazes.MazeStuff;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class Maze {

    private Location start;
    private Location end;
    private ArrayList<Location> solutionPath = new ArrayList<>();
    private String name;
    private int height = 2;
    private Material wall = Material.BLACK_CONCRETE;
    private Material floor = Material.WHITE_CONCRETE;

    public static HashMap<String, Maze> mazeList = new HashMap<>();

    public Maze(Location a, Location b, String name) {
        b.setY(a.getY());
        this.start = a;
        this.end = b;
        this.name = name;
        mazeList.put(name.toLowerCase(), this);
    }

    private boolean isWithin(Location l) {
        double minX = Math.min(this.start.getX(), this.end.getX());
        double maxX = Math.max(this.start.getX(), this.end.getX());
        double minZ = Math.min(this.start.getZ(), this.end.getZ());
        double maxZ = Math.max(this.start.getZ(), this.end.getZ());

        return (l.getX() >= minX && l.getX() <= maxX && l.getZ() >= minZ && l.getZ() <= maxZ);
    }

    public void generate() {
        Location current = this.start;
        ArrayList<Location> path = new ArrayList<>();
        ArrayList<Location> visited = new ArrayList<>();
        path.add(current);
        visited.add(current);
        ArrayList<Vector> dirs = new ArrayList<>();
        dirs.add(new Vector(2, 0, 0));
        dirs.add(new Vector(-2, 0, 0));
        dirs.add(new Vector(0, 0, 2));
        dirs.add(new Vector(0, 0, -2));
        ArrayList<Location> available = new ArrayList<>();
        Location option;
        Location old;
        double avgX;
        double avgZ;

        ArrayList<Block> blocks = this.reset();
        blocks.remove(this.start.getBlock());

        while (path.size() > 0) {
            available.clear();
            current = path.get(path.size()-1);
            for (Vector v : dirs) {
                option = current.clone().add(v);
                if (!visited.contains(option) && this.isWithin(option)) {
                    available.add(option);
                }
            }
            if (available.size() == 0) {
                path.remove(current);
                continue;
            }
            old = current.clone();
            current = available.get((int)(Math.random() * available.size()));
            path.add(current);
            visited.add(current);
            blocks.remove(current.getBlock());
            avgX = (old.getX() + current.getX())/2;
            avgZ = (old.getZ() + current.getZ())/2;
            blocks.remove(new Location(current.getWorld(), avgX, current.getY(), avgZ).getBlock());

            if (current.getBlock().getLocation().equals(this.end)) {
                this.solutionPath = (ArrayList<Location>) path.clone();
            }
        }

        for (Block b : blocks) {
            for (int level = 0; level <= Math.max(0, this.height); level++) {
                b.getLocation().clone().add(new Vector(0, level, 0)).getBlock().setType(this.wall);
            }
        }
    }

    public boolean fillSolution(Material m) {
        if (this.solutionPath.size() == 0) {
            return false;
        }
        for (Location l : this.solutionPath) {
            l.getBlock().setType(m);
        }
        return true;
    }

    public boolean solve() {
        return this.fillSolution(Material.LIME_CONCRETE);
    }

    public boolean unsolve() {
        return this.fillSolution(this.floor);
    }

    public ArrayList<Block> reset() {
        ArrayList<Block> blocks = new ArrayList<Block>();
        Block b = null;

        double minX = Math.min(this.start.getX(), this.end.getX());
        double maxX = Math.max(this.start.getX(), this.end.getX());
        double minZ = Math.min(this.start.getZ(), this.end.getZ());
        double maxZ = Math.max(this.start.getZ(), this.end.getZ());

        Location minCorner = new Location(this.start.getWorld(), minX, this.start.getY(), minZ);

        for (int x = 0; x <= maxX - minX; x++) {
            for (int y = 0; y <= maxZ - minZ; y++) {
                b = minCorner.clone().add(new Vector(x, 0, y)).getBlock();
                blocks.add(b);
                b.setType(this.floor);
                for (int level = 1; level <= Math.max(0, this.height); level++) {
                    b.getLocation().clone().add(new Vector(0, level, 0)).getBlock().setType(Material.AIR);
                }
            }
        }
        return blocks;
    }


    public String getName() {
        return this.name;
    }

    public int getHeight() {
        return this.height;
    }

    public Material getWallType() {
        return this.wall;
    }

    public Material getFloorType() {
        return this.floor;
    }

    public Location getStart() {
        return this.start;
    }

    public Location getEnd() {
        return this.end;
    }

    public ArrayList<Location> getSolution() {
        return this.solutionPath;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWallType(Material material) {
        this.wall = material;
    }

    public void setFloorType(Material material) {
        this.floor = material;
    }

    public void setSolution(ArrayList<Location> solution) {
        this.solutionPath = solution;
    }

}
