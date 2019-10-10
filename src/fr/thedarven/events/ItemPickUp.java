package fr.thedarven.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import fr.thedarven.main.LGUHC;

public class ItemPickUp implements Listener {

	public ItemPickUp(LGUHC pl) {
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onItemPickUp(PlayerPickupItemEvent e) {
		ItemStack itemFloor = e.getItem().getItemStack();
		if(itemFloor.getType() == Material.POTION) {
			for(ItemStack item : e.getPlayer().getInventory()) {
				if(item != null && item.getType() == Material.POTION && item.getAmount() != 64) {
					if(item.getData().getData() == itemFloor.getData().getData()) {
						item.setAmount(item.getAmount()+1);
						e.setCancelled(true);
						e.getItem().remove();
						return;
					}
				}
			}
		}
	}

}
