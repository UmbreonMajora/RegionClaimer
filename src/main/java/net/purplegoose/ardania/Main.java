package net.purplegoose.ardania;

import net.purplegoose.ardania.commands.RClaimCommand;
import net.purplegoose.ardania.util.ConfigReader;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    public static final String PREFIX = ChatColor.WHITE + "[" + ChatColor.RED + "RegionClaimer" + ChatColor.WHITE + "] " + ChatColor.RESET;

    public static void main(String[] args) {

    }

    @Override
    public void onEnable() {
        super.onEnable();
        Objects.requireNonNull(getCommand(RClaimCommand.COMMAND)).setExecutor(new RClaimCommand(new ConfigReader(this)));
    }
}