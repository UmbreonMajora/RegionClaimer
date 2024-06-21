package net.purplegoose.ardania.commands;

import lombok.extern.slf4j.Slf4j;
import net.purplegoose.ardania.Main;
import net.purplegoose.ardania.util.ConfigReader;
import net.purplegoose.ardania.util.RegionManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Slf4j
public class RClaimCommand implements CommandExecutor {
    public static final String COMMAND = "rc";

    private final ConfigReader configReader;

    public RClaimCommand(ConfigReader configReader) {
        this.configReader = configReader;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!isCommandSenderPlayerInstance(commandSender) || !commandSender.hasPermission("RegionClaimer.claim")) {
            return false;
        }

        Player player = (Player) commandSender;
        if (RegionManager.doesPlayerAlreadyHaveRegion(player)) {
            player.sendMessage(Main.PREFIX + configReader.getRegionExistsFail());
            return false;
        }

        Location location = player.getLocation();
        if (RegionManager.areRegionsNearPlayer(location, configReader.getRegionSizeLimit())) {
            player.sendMessage(Main.PREFIX + configReader.getRegionCloseFail());
            return false;
        }

        if (RegionManager.createRegion(player)) {
            player.sendMessage(Main.PREFIX + configReader.getSuccess());
            RegionManager.setFence(location, configReader.getMarkerBlock());
        }
        return true;
    }

    private boolean isCommandSenderPlayerInstance(CommandSender commandSender) {
        return commandSender instanceof Player;
    }
}
