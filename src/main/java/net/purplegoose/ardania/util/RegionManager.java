package net.purplegoose.ardania.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;

@Slf4j
public class RegionManager {
    private RegionManager() {/* static use only*/}

    private static final RegionContainer REGION_CONTAINER;

    static {
        REGION_CONTAINER = WorldGuard.getInstance().getPlatform().getRegionContainer();
    }

    public static boolean createRegion(Player player) {
        com.sk89q.worldedit.world.World playerWorld = BukkitAdapter.adapt(player.getWorld());
        Location location = player.getLocation();
        BlockVector3 pos1 = BlockVector3.at(location.getBlockX() - 25, -64, location.getBlockZ() - 25);
        BlockVector3 pos2 = BlockVector3.at(location.getBlockX() + 25, 320, location.getBlockZ() + 25);
        ProtectedRegion newPlayerRegion = new ProtectedCuboidRegion(player.getName(), pos1, pos2);
        DefaultDomain owners = newPlayerRegion.getOwners();
        owners.addPlayer(WorldGuardPlugin.inst().wrapPlayer(player));

        com.sk89q.worldguard.protection.managers.RegionManager regionManager = REGION_CONTAINER.get(playerWorld);

        if (isRegionManagerNull(regionManager)) {
            return false;
        }

        Objects.requireNonNull(regionManager).addRegion(newPlayerRegion);
        return true;
    }

    public static boolean areRegionsNearPlayer(Location location, int regionSize) {
        if (location == null) {
            log.error("A player tried to create a new region but the location was null!");
            return true;
        }

        com.sk89q.worldguard.protection.managers.RegionManager regionManager = REGION_CONTAINER.get(BukkitAdapter
                .adapt(Objects.requireNonNull(location.getWorld())));

        if (isRegionManagerNull(regionManager)) {
            return false;
        }

        int xLocation = location.getBlockX();
        int zLocation = location.getBlockZ();
        for (int x = xLocation - (regionSize / 2); x < xLocation + (regionSize / 2); x++) {
            for (int z = zLocation - (regionSize / 2); z < zLocation + (regionSize / 2); z++) {
                for (int y = 320; y > -64; y--) {
                    ApplicableRegionSet set = Objects.requireNonNull(regionManager)
                            .getApplicableRegions(BlockVector3.at(x, y, z));
                    if (set.getRegions().size() > 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void setFence(Location location, Material markerBlock) {
        int xLocation = location.getBlockX();
        int zLocation = location.getBlockZ();

        for (int x = xLocation - 26; x < xLocation + 26; x++) {
            for (int z = zLocation - 26; z < zLocation + 26; z++) {
                for (int y = 320; y > -64; y--) {
                    if (x == (xLocation - 25) && z == (zLocation - 25)) {
                        if (Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z).getType() != Material.AIR &&
                                location.getWorld().getBlockAt(x, y + 1, z).getType() == Material.AIR) {
                            location.getWorld().getBlockAt(x, y, z).setType(markerBlock);
                            location.getWorld().getBlockAt(x, y, z + 1).setType(markerBlock);
                            location.getWorld().getBlockAt(x + 1, y, z).setType(markerBlock);
                        }
                    }
                    if (x == (xLocation + 25) && z == (zLocation + 25)) {
                        if (Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z).getType() != Material.AIR &&
                                location.getWorld().getBlockAt(x, y + 1, z).getType() == Material.AIR) {
                            location.getWorld().getBlockAt(x, y, z).setType(markerBlock);
                            location.getWorld().getBlockAt(x, y, z - 1).setType(markerBlock);
                            location.getWorld().getBlockAt(x - 1, y, z).setType(markerBlock);
                        }
                    }

                    if (x == (xLocation + 25) && z == (zLocation - 25)) {
                        if (Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z).getType() != Material.AIR &&
                                location.getWorld().getBlockAt(x, y + 1, z).getType() == Material.AIR) {
                            location.getWorld().getBlockAt(x, y, z).setType(markerBlock);
                            location.getWorld().getBlockAt(x - 1, y, z).setType(markerBlock);
                            location.getWorld().getBlockAt(x, y, z + 1).setType(markerBlock);
                        }
                    }

                    if (x == (xLocation - 25) && z == (zLocation + 25)) {
                        if (Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z).getType() != Material.AIR &&
                                location.getWorld().getBlockAt(x, y + 1, z).getType() == Material.AIR) {
                            location.getWorld().getBlockAt(x, y, z).setType(markerBlock);
                            location.getWorld().getBlockAt(x + 1, y, z).setType(markerBlock);
                            location.getWorld().getBlockAt(x, y, z - 1).setType(markerBlock);
                        }
                    }
                }
            }
        }
    }

    public static boolean doesPlayerAlreadyHaveRegion(Player player) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        Location playerLocation = player.getLocation();
        org.bukkit.World world = playerLocation.getWorld();
        if (world == null) {
            log.error(String.format("%s tried to create a new region but the world was null!", player.getName()));
            return true;
        }

        World wgWorld = BukkitAdapter.adapt(world);
        com.sk89q.worldguard.protection.managers.RegionManager regionManager = REGION_CONTAINER.get(wgWorld);
        if (regionManager == null) {
            log.error(String.format("%s tried to create a new region but the region manager was null!", player.getName()));
            return true;
        }

        return regionManager.getRegionCountOfPlayer(localPlayer) > 0;
    }

    private static boolean isRegionManagerNull(com.sk89q.worldguard.protection.managers.RegionManager regionManager) {
        if (regionManager == null) {
            log.error("A player tried to create a new region but the region manager was null!");
            return true;
        }
        return false;
    }
}
