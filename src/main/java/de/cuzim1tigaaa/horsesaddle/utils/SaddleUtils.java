package de.cuzim1tigaaa.horsesaddle.utils;

import de.cuzim1tigaaa.horsesaddle.HorseSaddle;
import de.cuzim1tigaaa.horsesaddle.files.Config;
import de.cuzim1tigaaa.horsesaddle.files.Paths;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SaddleUtils {

	private final NamespacedKey horseDataKey;

	public SaddleUtils(HorseSaddle plugin) {
		this.horseDataKey = new NamespacedKey(plugin, "data");
	}

	public ItemStack saveHorseToSaddle(ItemStack saddle, AbstractHorse abstractHorse) {
		ItemMeta meta = saddle.getItemMeta();
		meta.getPersistentDataContainer()
				.set(horseDataKey, PersistentDataType.STRING, new HorseWrapper().serialize(abstractHorse).toString());

		meta.setDisplayName(Config.getConfig().getMessage(Paths.MESSAGES_SADDLE_NAME, "TYPE", abstractHorse.getType().name().replace("_", " ")));
		meta.addEnchant(Enchantment.UNBREAKING, 3, false);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		String message = Config.getConfig().getMessage(Paths.MESSAGES_SADDLE_LORE,
				"CUSTOM_NAME", abstractHorse.getCustomName() == null ? "" : abstractHorse.getCustomName(),
				"JUMP_STRENGTH", String.format("%.2f", abstractHorse.getJumpStrength()),
				"SPEED", String.format("%.2f", abstractHorse.getAttribute(Attribute.MOVEMENT_SPEED).getValue()),
				"MAX_HEALTH", String.format("%.2f", abstractHorse.getAttribute(Attribute.MAX_HEALTH).getValue()),
				"HEALTH", String.format("%.2f", abstractHorse.getHealth()));

		if(abstractHorse instanceof Horse horse) {
			message += "\n" + Config.getConfig().getMessage(Paths.MESSAGES_SADDLE_LORE_HORSE,
					"COLOR", horse.getColor().name().replace("_", " "),
					"STYLE", horse.getStyle().name().replace("_", " "));
		}

		meta.setLore(new ArrayList<>(List.of(message.split("\n"))));
		saddle.setItemMeta(meta);
		return saddle;
	}

	public boolean spawnHorseFromSaddle(ItemStack saddle, Location location) {
		ItemMeta meta = saddle.getItemMeta();
		String data = meta.getPersistentDataContainer().get(horseDataKey, PersistentDataType.STRING);
		if(data == null)
			return false;

		AbstractHorse horse = new HorseWrapper().deserialize(data, location);
		return true;
	}
}