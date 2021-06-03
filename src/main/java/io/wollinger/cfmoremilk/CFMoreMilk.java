package io.wollinger.cfmoremilk;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class CFMoreMilk extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        LogManager.log("CFMoreMilk started! Have fun!", Level.INFO);
    }

    @Override
    public void onDisable() {

    }

}
