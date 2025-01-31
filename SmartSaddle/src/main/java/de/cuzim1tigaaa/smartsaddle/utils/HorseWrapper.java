package de.cuzim1tigaaa.smartsaddle.utils;

import com.google.gson.*;
import de.cuzim1tigaaa.smartsaddle.SmartSaddle;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public class HorseWrapper {

	private final SmartSaddle plugin;

	public HorseWrapper(SmartSaddle plugin) {
		this.plugin = plugin;
	}

	public AbstractHorse deserialize(String json, Location location) throws JsonParseException {
		JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		EntityType type = EntityType.valueOf(jsonObject.get("type").getAsString());

		AbstractHorse h = (AbstractHorse) location.getWorld().spawnEntity(location, type);
		switch(type) {
			case HORSE -> h = deserializeHorse(h, jsonObject);
			case SKELETON_HORSE -> h = deserializeSkeletonHorse(h, jsonObject);
			case ZOMBIE_HORSE, DONKEY, MULE, CAMEL -> {}
			default -> throw new JsonParseException("Invalid horse type: " + type);
		}

		if(h instanceof ChestedHorse chestedHorse)
			chestedHorse.setCarryingChest(jsonObject.get("isCarryingChest").getAsBoolean());

		h.setCustomName(jsonObject.get("name").getAsString());
		h.setAge(jsonObject.get("age").getAsInt());
		h.setJumpStrength(jsonObject.get("jumpStrength").getAsDouble());
		plugin.getHorseData().getMovementSpeed(h).setBaseValue(jsonObject.get("speed").getAsDouble());
		plugin.getHorseData().getMaxHealth(h).setBaseValue(jsonObject.get("maxHealth").getAsDouble());
		h.setHealth(jsonObject.get("health").getAsDouble());
		h.setTamed(true);

		ReadWriteNBT nbt = NBT.parseNBT(jsonObject.get("inventory").getAsString());
		ItemStack[] contents = NBT.itemStackArrayFromNBT(nbt);
		h.getInventory().setContents(contents);
		return h;
	}

	public JsonElement serialize(AbstractHorse src) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", src.getType().name());
		jsonObject.addProperty("name", src.getCustomName() == null ? "" : src.getCustomName());
		jsonObject.addProperty("age", src.getAge());
		jsonObject.addProperty("jumpStrength", src.getJumpStrength());
		jsonObject.addProperty("speed", plugin.getHorseData().getMovementSpeed(src).getValue());
		jsonObject.addProperty("maxHealth", plugin.getHorseData().getMaxHealth(src).getValue());
		jsonObject.addProperty("health", src.getHealth());

		ItemStack[] inventory = src.getInventory().getContents();
		jsonObject.addProperty("inventory", NBT.itemStackArrayToNBT(inventory).toString());

		if(src instanceof Horse horse) {
			jsonObject.addProperty("style", horse.getStyle().name());
			jsonObject.addProperty("color", horse.getColor().name());
		}

		if(src instanceof ChestedHorse chestedHorse)
			jsonObject.addProperty("isCarryingChest", chestedHorse.isCarryingChest());

		return jsonObject;
	}


	private Horse deserializeHorse(AbstractHorse abstractHorse, JsonObject jsonObject) {
		Horse horse = (Horse) abstractHorse;

		Horse.Style style = Horse.Style.valueOf(jsonObject.get("style").getAsString());
		Horse.Color color = Horse.Color.valueOf(jsonObject.get("color").getAsString());

		horse.setStyle(style);
		horse.setColor(color);
		return horse;
	}

	private SkeletonHorse deserializeSkeletonHorse(AbstractHorse abstractHorse, JsonObject jsonObject) {
		SkeletonHorse sHorse = (SkeletonHorse) abstractHorse;
		sHorse.setTrapped(false);
		return sHorse;
	}
}