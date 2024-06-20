package net.purplegoose.ardania.commands;

import lombok.extern.slf4j.Slf4j;
import net.purplegoose.ardania.util.RegionManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Slf4j
public class RClaimCommand implements CommandExecutor {
    public static final String COMMAND = "rc";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!isCommandSenderPlayerInstance(commandSender) || !commandSender.hasPermission("RegionClaimer.claim")) {
            return false;
        }

        Player player = (Player) commandSender;
        if (RegionManager.doesPlayerAlreadyHaveRegion(player)) {
            player.sendMessage("You already got a region!");
            return false;
        }

        Location location = player.getLocation();
        if (RegionManager.areRegionsNearPlayer(location)) {
            player.sendMessage("You cannot do that here, there is already a region nearby!");
            return false;
        }

        return RegionManager.createRegion(player);
    }

    private boolean isCommandSenderPlayerInstance(CommandSender commandSender) {
        return commandSender instanceof Player;
    }
}
