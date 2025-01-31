package de.cuzim1tigaaa.smartsaddle;

import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.AbstractHorse;

public interface HorseData {

	AttributeInstance getMovementSpeed(AbstractHorse horse);

	AttributeInstance getMaxHealth(AbstractHorse horse);

}