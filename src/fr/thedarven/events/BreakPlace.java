package fr.thedarven.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.main.LGUHC;

public class BreakPlace implements Listener {

	public BreakPlace(LGUHC pl) {
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if((LGUHC.etat.equals(EnumGame.WAIT) || LGUHC.etat.equals(EnumGame.TELEPORTATION)) && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		if((LGUHC.etat.equals(EnumGame.WAIT) || LGUHC.etat.equals(EnumGame.TELEPORTATION)) && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
			e.setCancelled(true);
		}
	}
}
