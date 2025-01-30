package de.cuzim1tigaaa.horsesaddle.listeners;

import de.cuzim1tigaaa.horsesaddle.HorseSaddle;
import de.cuzim1tigaaa.horsesaddle.files.Config;
import de.cuzim1tigaaa.horsesaddle.files.Permissions;
import de.cuzim1tigaaa.horsesaddle.utils.SaddleUtils;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

public class SaddleEvent implements Listener {

	private final SaddleUtils saddleUtils;

	public SaddleEvent(HorseSaddle plugin) {
		this.saddleUtils = new SaddleUtils(plugin);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onSaddleRemove(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(Config.getConfig().getDisabledWorlds().contains(player.getWorld().getName()))
			return;

		if(!(event.getInventory() instanceof AbstractHorseInventory horseInv))
			return;

		if(event.getRawSlot() != 0 || horseInv.getSaddle() == null)
			return;

		if(!(horseInv.getHolder() instanceof AbstractHorse horse))
			return;

		if(!horse.getPassengers().contains(player))
			return;

		switch(horse.getType()) {
			case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, DONKEY, MULE, CAMEL -> {
				if(!player.hasPermission(Permissions.SADDLE_USE + horse.getType().name().toLowerCase()))
					return;
			}
		}

		ItemStack saddle = horseInv.getSaddle();
		player.getInventory().addItem(saddleUtils.saveHorseToSaddle(saddle, horse));

		horseInv.setSaddle(null);
		player.closeInventory();
		horse.removePassenger(player);
		horse.remove();
	}

	@EventHandler
	public void onSaddlePlace(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(Config.getConfig().getDisabledWorlds().contains(player.getWorld().getName()))
			return;

		if(event.getClickedBlock() == null || event.getItem() == null)
			return;

		ItemStack item = event.getItem();
		if(item.getType() != Material.SADDLE)
			return;

		if(saddleUtils.spawnHorseFromSaddle(item, event.getClickedBlock().getLocation().add(0, 1, 0)))
			player.getInventory().remove(item);
	}
}