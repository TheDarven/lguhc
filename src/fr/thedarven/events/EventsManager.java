package fr.thedarven.events;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.thedarven.configuration.inventory.ConfigInventory;
import fr.thedarven.main.LGUHC;

public class EventsManager {

	public static void registerEvents(LGUHC pl) {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Login(pl), pl);
		pm.registerEvents(new Nether(pl), pl);
		pm.registerEvents(new Tchat(pl), pl);
		pm.registerEvents(new Walk(pl), pl);
		pm.registerEvents(new Death(pl), pl);
		pm.registerEvents(new BreakPlace(pl), pl);
		pm.registerEvents(new Hunger(pl), pl);
		pm.registerEvents(new ClickPlayer(pl), pl);
		
		pm.registerEvents(new ConfigInventory(pl), pl);	
		pm.registerEvents(new WorldGeneration(pl), pl);
	}
}
