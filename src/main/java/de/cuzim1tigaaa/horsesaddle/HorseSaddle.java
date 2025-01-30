package de.cuzim1tigaaa.horsesaddle;

import de.cuzim1tigaaa.horsesaddle.command.CommandHorseSaddle;
import de.cuzim1tigaaa.horsesaddle.files.Config;
import de.cuzim1tigaaa.horsesaddle.listeners.SaddleEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class HorseSaddle extends JavaPlugin {

	@Override
	public void onEnable() {
		if(!Bukkit.getServer().getPluginManager().isPluginEnabled("NBTAPI")) {
			getLogger().severe("NBTAPI is not installed but required! Disabling HorseSaddle...");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		reload();
		// Plugin startup logic
		new SaddleEvent(this);
		new CommandHorseSaddle(this);
	}

	public void reload() {
		getLogger().log(Level.INFO, "Loading plugin files...");
		Config.getConfig().loadConfig(this);
		Config.getConfig().loadMessages(this);
	}
}