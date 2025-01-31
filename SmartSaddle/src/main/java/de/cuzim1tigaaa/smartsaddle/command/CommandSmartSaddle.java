package de.cuzim1tigaaa.smartsaddle.command;

import de.cuzim1tigaaa.smartsaddle.SmartSaddle;
import de.cuzim1tigaaa.smartsaddle.files.*;
import org.bukkit.command.*;

import java.util.List;

public class CommandSmartSaddle implements CommandExecutor, TabCompleter {

	private final SmartSaddle plugin;

	public CommandSmartSaddle(SmartSaddle plugin) {
		this.plugin = plugin;
		plugin.getCommand("smartsaddle").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission(Permissions.SADDLE_COMMAND)) {
			sender.sendMessage(Config.getConfig().getMessage(Paths.MESSAGES_PERMISSION));
			return true;
		}

		sender.sendMessage(Config.getConfig().getMessage(Paths.MESSAGES_RELOADED));
		plugin.reload();
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return List.of();
	}
}