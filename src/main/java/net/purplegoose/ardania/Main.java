package net.purplegoose.ardania;

import net.purplegoose.ardania.commands.RClaimCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    public static void main(String[] args) {

    }

    @Override
    public void onEnable() {
        super.onEnable();
        Objects.requireNonNull(getCommand(RClaimCommand.COMMAND)).setExecutor(new RClaimCommand());
    }
}