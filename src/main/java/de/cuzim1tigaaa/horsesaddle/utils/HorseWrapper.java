package de.cuzim1tigaaa.horsesaddle.utils;

import com.google.gson.*;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public class HorseWrapper {

	@Getter
	private static class GeneralHorse {

		private final int age;
		private final double jumpStrength;
		private final double speed;
		private final double maxHealth;

		public GeneralHorse(int age, double jumpStrength, double speed, double maxHealth) {
			this.age = age;
			this.jumpStrength = jumpStrength;
			this.speed = speed;
			this.maxHealth = maxHealth;
		}
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
		h.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(jsonObject.get("speed").getAsDouble());
		h.getAttribute(Attribute.MAX_HEALTH).setBaseValue(jsonObject.get("maxHealth").getAsDouble());
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
		jsonObject.addProperty("speed", src.getAttribute(Attribute.MOVEMENT_SPEED).getValue());
		jsonObject.addProperty("maxHealth", src.getAttribute(Attribute.MAX_HEALTH).getValue());
		jsonObject.addProperty("health", src.getHealth());

		ItemStack[] inventory = src.getInventory().getContents();
		jsonObject.addProperty("inventory", NBT.itemStackArrayToNBT(inventory).toString());

		if(src instanceof Horse horse) {
			jsonObject.addProperty("style", horse.getStyle().name());
			jsonObject.addProperty("color", horse.getColor().name());
		}

		if(src instanceof ChestedHorse chestedHorse)
			jsonObject.addProperty("isCarryingChest", chestedHorse.isCarryingChest());

		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		Bukkit.getLogger().info("Serialized horse: " + gson.toJson(jsonObject));
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