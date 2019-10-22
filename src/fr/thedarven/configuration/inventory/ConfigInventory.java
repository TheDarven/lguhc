package fr.thedarven.configuration.inventory;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.enums.EnumCompo;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.main.LGUHC;

public class ConfigInventory implements Listener {

	public ConfigInventory(LGUHC pl) {
	}
	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(!(e.getWhoClicked() instanceof Player) || e.getClickedInventory() == null){
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if(LGUHC.etat.equals(EnumGame.WAIT) && p.isOp() && !e.getCurrentItem().getType().equals(Material.AIR)){
			if(!e.getInventory().getName().equals(InventoryRegister.menu.getName()) && !e.getInventory().getName().startsWith("Menu > Cycle > Jour") && !e.getInventory().getName().equals("Menu > Composition")){
				return;
			}
			e.setCancelled(true);
			
			if(e.getClickedInventory().getName().equals(InventoryRegister.menu.getName()) && e.getClickedInventory().getSize() == InventoryRegister.menu.getLines()*9){
				if(e.getCurrentItem().getType().equals(Material.WATCH)){
					ConfigurationOpenInventory.openCycleInventory(p,1);
				}
				if(e.getCurrentItem().getType().equals(Material.PAPER)){
					ConfigurationOpenInventory.openCompoInventory(p);
				}
			}
			
			
			if(e.getClickedInventory().getName().startsWith("Menu > Cycle > Jour") && e.getClickedInventory().getSize() == 36){
				int day = Integer.parseInt(e.getClickedInventory().getName().replace("Menu > Cycle > Jour ", ""));
				
				if(e.getCurrentItem().getType().equals(Material.REDSTONE)){
					p.openInventory(InventoryRegister.menu.getInventory());
				}
				if(e.getCurrentItem().getType().equals(Material.ARROW)){
					int arrowDay = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GREEN+"Jour ", ""));
					ConfigurationOpenInventory.openCycleInventory(p, arrowDay);
				}
				
				if(e.getCurrentItem().getType().equals(Material.WOOL)){
					LGUHC.cycle.changeOnMinute(e.getRawSlot()+1+20*(day-1));
					ConfigurationOpenInventory.openCycleInventory(p, day);
				}
				
				if(e.getCurrentItem().getType().equals(Material.SIGN)){
					LGUHC.cycle.cloneDay(day);
				}
			}
			
			if(e.getClickedInventory().getName().equals("Menu > Composition") && e.getClickedInventory().getSize() == 54){
				if(e.getCurrentItem().getType().equals(Material.REDSTONE)){
					p.openInventory(InventoryRegister.menu.getInventory());
				}
				if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)){
					if(e.getClick().equals(ClickType.LEFT)){
						String name = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§b", "").toUpperCase().replaceAll(" ", "").replaceAll("'", "");
						EnumCompo.valueOf(name).addRole();
						ConfigurationOpenInventory.openCompoInventory(p);
					}
					if(e.getClick().equals(ClickType.RIGHT)){
						String name = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§b", "").toUpperCase().replaceAll(" ", "").replaceAll("'", "");
						EnumCompo.valueOf(name).removeRole();
						ConfigurationOpenInventory.openCompoInventory(p);
					}
				}
			}
		}		
	}

}
