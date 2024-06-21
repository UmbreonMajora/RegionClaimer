package net.purplegoose.ardania.util;

import lombok.Getter;
import net.purplegoose.ardania.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

@Getter
public class ConfigReader {

    private FileConfiguration config;

    private int regionSizeLimit = 50;
    private String regionCloseFail = "Deine Region konnte nicht erstellt werden, es ist bereits eine andere Region in der Nähe.";
    private String regionExistsFail = "Deine Region konnte nicht erstellt werden, du besitzt bereits eine.";
    private String success = "Deine Region wurde erfolgreich erstellt!";
    private Material markerBlock = Material.RED_WOOL;

    public ConfigReader(Main main) {
        if (main.getDataFolder().exists()) {
            File file = new File(main.getDataFolder(), "config.yml");
            if (file.exists()) {
                config = main.getConfig();
                regionSizeLimit = config.getInt("region-size");
                regionCloseFail = config.getString("region-close-fail");
                regionExistsFail = config.getString("region-exists-fail");
                success = config.getString("success");
                markerBlock = Material.valueOf(config.getString("marker-block"));
            }
        }
    }
}
