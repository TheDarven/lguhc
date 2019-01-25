package fr.thedarven.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import fr.thedarven.main.LGUHC;

public class Nether implements Listener {

	public Nether(LGUHC pl) {
	}
	
	@EventHandler
	public void onPortalTeleport(PlayerPortalEvent e){
		e.setCancelled(true);
	}

}
