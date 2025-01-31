package de.cuzim1tigaaa.smartsaddle.files;

import de.cuzim1tigaaa.smartsaddle.SmartSaddle;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

	private FileConfiguration fileConfig, fileMessages;
	@Getter
	private List<String> disabledWorlds;

	private static Config config;

	public static Config getConfig() {
		if(config == null)
			config = new Config();
		return config;
	}

	private static final Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");

	public String getMessage(String path, Object... replace) {
		StringBuilder message;
		if(fileMessages.isList(path)) {
			List<String> messageList = fileMessages.getStringList(path);
			message = new StringBuilder();
			for(String line : messageList) {
				message.append(line).append("\n");
			}
		}else {
			if(fileMessages.getString(path) == null)
				return ChatColor.RED + "Error: Path " + ChatColor.GRAY + "'" + path + "' " + ChatColor.RED + "does not exist!";
			message = new StringBuilder(fileMessages.getString(path));
		}

		if(message.isEmpty())
			return null;

		for(int i = 0; i < replace.length; i++) {
			String target = replace[i] == null ? null : (String) replace[i];
			if(target == null)
				continue;
			i++;
			String replacement = replace[i] == null ? null : replace[i].toString();
			if(fileMessages != null) message = new StringBuilder(replacement == null ? message.toString() : message.toString().replace("%" + target + "%", replacement));
		}

		Matcher matcher = pattern.matcher(message.toString());
		while(matcher.find()) {
			String hex = message.substring(matcher.start(), matcher.end());
			message = new StringBuilder(message.toString().replace(hex, ChatColor.of(hex) + ""));
			matcher = pattern.matcher(message.toString());
		}

		return ChatColor.translateAlternateColorCodes('&', message.toString());
	}

	private void set(FileConfiguration fileConfig, String path, Object value, String... comments) {
		fileConfig.setComments(path, List.of(comments));
		fileConfig.set(path, fileConfig.get(path, value));
	}

	public void loadConfig(SmartSaddle plugin) {
		try {
			File file = new File(plugin.getDataFolder(), "config.yml");
			if(!file.exists()) {
				fileConfig = new YamlConfiguration();
				fileConfig.save(file);
			}
			fileConfig = YamlConfiguration.loadConfiguration(file);
			set(fileConfig, Paths.CONFIG_DISABLED_WORLDS, List.of("worldToDisable"), "List of disabled worlds");
			fileConfig.save(file);
		}catch(IOException e) {
			plugin.getLogger().severe("Failed to load config file!");
		}

		this.disabledWorlds = fileConfig.getStringList(Paths.CONFIG_DISABLED_WORLDS);
	}

	public void loadMessages(SmartSaddle plugin) {
		try {
			File file = new File(plugin.getDataFolder(), "messages.yml");
			if(!file.exists()) {
				fileMessages = new YamlConfiguration();
				fileMessages.save(file);
			}
			fileMessages = YamlConfiguration.loadConfiguration(file);
			set(fileMessages, Paths.MESSAGES_PERMISSION, "&cYou do not have permission to use this command");
			set(fileMessages, Paths.MESSAGES_RELOADED, "&7The plugin has been reloaded.");

			set(fileMessages, Paths.MESSAGES_SADDLE_NAME, "&6%TYPE%");
			set(fileMessages, Paths.MESSAGES_SADDLE_LORE, List.of(
							"",
							"&7Custom Name: &6%CUSTOM_NAME%",
							"&7Jump Strength: &6%JUMP_STRENGTH%",
							"&7Speed: &6%SPEED%",
							"&7Max Health: &6%MAX_HEALTH%",
							"&7Health: &6%HEALTH%"
					),
					"General Lore data for saddles. The following placeholders can be used:",
					"%CUSTOM_NAME% - The custom name of the horse",
					"%JUMP_STRENGTH% - The jump strength of the horse",
					"%SPEED% - The speed of the horse",
					"%MAX_HEALTH% - The max health of the horse",
					"%HEALTH% - The health of the horse");

			set(fileMessages, Paths.MESSAGES_SADDLE_LORE_HORSE, List.of(
							"",
							"&7Color: &6%COLOR%",
							"&7Style: &6%STYLE%"
					),
					"Horse specific lore data for saddles. The following placeholders can be used:",
					"%COLOR% - The color of the horse",
					"%STYLE% - The style of the horse");

			fileMessages.save(file);
		}catch(IOException e) {
			plugin.getLogger().severe("Failed to load messages file!");
		}
	}

}