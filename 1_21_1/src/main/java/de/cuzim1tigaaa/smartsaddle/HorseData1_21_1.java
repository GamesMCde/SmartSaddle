package de.cuzim1tigaaa.smartsaddle;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.AbstractHorse;

public class HorseData1_21_1 implements HorseData {

	@Override
	public AttributeInstance getMovementSpeed(AbstractHorse horse) {
		return horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
	}

	@Override
	public AttributeInstance getMaxHealth(AbstractHorse horse) {
		return horse.getAttribute(Attribute.GENERIC_MAX_HEALTH);
	}
}