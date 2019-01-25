package fr.thedarven.events;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.main.LGUHC;

public class Hunger implements Listener {

	public Hunger(LGUHC pl) {
	}
	
	@EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
		if(LGUHC.etat.equals(EnumGame.WAIT) || LGUHC.etat.equals(EnumGame.TELEPORTATION)){
			event.setFoodLevel(20);
		}
    }
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e){
		Player player = e.getPlayer();
		if(e.getItem().getType().equals(Material.GOLDEN_APPLE) && e.getItem().getData().getData() == 1){
			e.setCancelled(true);
			player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT , 2.0f, 1.0f);
			ItemStack item = new ItemStack(Material.GOLDEN_APPLE, e.getItem().getAmount());
			e.getPlayer().setItemInHand(item);
		}		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		Player player = (Player) e.getWhoClicked();
		if(e.getCurrentItem() != null){
			if(e.getCurrentItem().getType().equals(Material.GOLDEN_APPLE) && e.getCurrentItem().getData().getData() == 1){
				player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT , 2.0f, 1.0f);
				ItemStack item = new ItemStack(Material.GOLDEN_APPLE, e.getCurrentItem().getAmount());
				e.setCurrentItem(item);
			}	
		}
	}
}
