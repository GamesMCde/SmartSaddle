package de.cuzim1tigaaa.smartsaddle;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

public class HorseData1_21_4 implements HorseData {

	@Override
	public AttributeInstance getMovementSpeed(org.bukkit.entity.AbstractHorse horse) {
		return horse.getAttribute(Attribute.MOVEMENT_SPEED);
	}

	@Override
	public AttributeInstance getMaxHealth(org.bukkit.entity.AbstractHorse horse) {
		return horse.getAttribute(Attribute.MAX_HEALTH);
	}
}