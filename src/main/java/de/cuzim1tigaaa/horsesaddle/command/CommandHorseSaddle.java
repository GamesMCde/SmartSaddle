package de.cuzim1tigaaa.horsesaddle.command;

import de.cuzim1tigaaa.horsesaddle.HorseSaddle;
import de.cuzim1tigaaa.horsesaddle.files.*;
import org.bukkit.command.*;

import java.util.List;

public class CommandHorseSaddle implements CommandExecutor, TabCompleter {

	private final HorseSaddle plugin;

	public CommandHorseSaddle(HorseSaddle plugin) {
		this.plugin = plugin;
		plugin.getCommand("horsesaddle").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission(Permissions.SADDLE_COMMAND)) {
			sender.sendMessage(Config.getConfig().getMessage(Paths.MESSAGES_PERMISSION));
			return true;
		}

		plugin.reload();
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return List.of();
	}
}