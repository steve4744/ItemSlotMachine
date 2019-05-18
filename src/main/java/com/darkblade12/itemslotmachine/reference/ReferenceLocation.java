package com.darkblade12.itemslotmachine.reference;

import com.darkblade12.itemslotmachine.safe.SafeLocation;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class ReferenceLocation {

    private static final String FORMAT = "-?\\d+(@-?\\d+){2}";
    int l, f, u;

    ReferenceLocation(int l, int f, int u) {
        this.l = l;
        this.f = f;
        this.u = u;
    }

    public static ReferenceLocation fromString(String s) throws IllegalArgumentException {
        if (!s.matches(FORMAT)) {
            throw new IllegalArgumentException("Invalid format");
        }
        String[] p = s.split("@");
        return new ReferenceLocation(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]));
    }

    static ReferenceLocation fromBukkitLocation(Location c, Direction d, Location l) {
        int cX = c.getBlockX();
        int cY = c.getBlockY();
        int cZ = c.getBlockZ();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        switch (d) {
            case NORTH:
                return new ReferenceLocation(x - cX, cZ - z, y - cY);
            case EAST:
                return new ReferenceLocation(cZ - z, x - cX, y - cY);
            case SOUTH:
                return new ReferenceLocation(x - cX, z - cZ, y - cY);
            case WEST:
                return new ReferenceLocation(z - cZ, cX - x, y - cY);
        }
        return null;
    }

    public static ReferenceLocation fromBukkitLocation(Player p, Location l) {
        return fromBukkitLocation(p.getLocation(), Direction.get(p), l);
    }

    public ReferenceLocation add(int l, int f, int u) {
        this.l += l;
        this.f += f;
        this.u += u;
        return this;
    }

    public int getL() {
        return l;
    }

    public int getF() {
        return f;
    }

    Location getBukkitLocation(Location c, Direction d) {
        int x = c.getBlockX();
        int y = c.getBlockY();
        int z = c.getBlockZ();
        switch (d) {
            case WEST:
                return new Location(c.getWorld(), x - f, y + u, z + l);
            case NORTH:
                return new Location(c.getWorld(), x - l, y + u, z - f);
            case EAST:
                return new Location(c.getWorld(), x + f, y + u, z - l);
            case SOUTH:
                return new Location(c.getWorld(), x + l, y + u, z + f);
        }
        return null;
    }

    public SafeLocation getSafeLocation(Location c, Direction d) {
        return SafeLocation.fromBukkitLocation(getBukkitLocation(c, d));
    }

    public Block getBukkitBlock(Location c, Direction d) {
        return getBukkitLocation(c, d).getBlock();
    }

    @Override
    public String toString() {
        return l + "@" + f + "@" + u;
    }

    ReferenceBlock toReferenceBlock(BlockData blockData, Direction initialDirection) {
        return new ReferenceBlock(l, f, u, blockData, initialDirection);
    }

    ReferenceItemFrame toReferenceItemFrame(Direction initialFacing, Direction initialDirection) {
        return new ReferenceItemFrame(l, f, u, initialFacing, initialDirection);
    }

    @Override
    public ReferenceLocation clone() {
        ReferenceLocation referenceLocation = (ReferenceLocation) super.clone();
        return new ReferenceLocation(l, f, u);
    }
}
