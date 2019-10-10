package fr.thedarven.events;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class Walk implements Listener {

	public Walk(LGUHC pl) {
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void walk(PlayerMoveEvent e){
		if(LGUHC.etat.equals(EnumGame.WAIT)){
			Location loc = new Location(Bukkit.getWorld("lguhc"), e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY()-1, e.getPlayer().getLocation().getBlockZ());
			if(loc.getBlock().getType().equals(Material.STAINED_GLASS) && loc.getBlock().getData() == (byte) 0 && loc.getBlockY() > 200 && !e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
				Random r = new Random();
				int color = 1 + r.nextInt(15);
				if(color == 7){
					color = 8;
				}
				Bukkit.getWorld("lguhc").getBlockAt(loc).setData((byte) (color));
				LGUHC.jump.addBlock(loc);
			}
			
			if(loc.getBlockY() < 190 && !e.getPlayer().getGameMode().equals(GameMode.SPECTATOR) && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
				e.getPlayer().teleport(new Location(Bukkit.getWorld("lguhc"), 0, 202, 0));
			}
		}else if(LGUHC.etat.equals(EnumGame.TELEPORTATION) && !e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
			Location origin = e.getFrom();
			Location destination = e.getTo();
			if(!(destination.getBlockX() == origin.getBlockX() && destination.getBlockZ() == origin.getBlockZ())) {
				e.setTo(e.getFrom());
			}
		}else if((LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.etat.equals(EnumGame.MIDDLEGAME)) && e.getPlayer().getGameMode().equals(GameMode.ADVENTURE) && PlayerLG.getPlayerManager(e.getPlayer().getUniqueId()).isAlive() && (Integer) PlayerLG.getPlayerManager(e.getPlayer().getUniqueId()).getPreDeath().get(0) != 0 && e.getPlayer().getLocation().getBlockY() > 400){
			if(!(e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ() && e.getTo().getBlockY() == e.getFrom().getBlockY())) {
				e.setTo(e.getFrom());
			}
		}
	}
	
	@EventHandler
	public void teleportEvent(PlayerTeleportEvent e){ 
		if((LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.etat.equals(EnumGame.MIDDLEGAME)) && e.getPlayer().getGameMode().equals(GameMode.SPECTATOR) && PlayerLG.getPlayerManager(e.getPlayer().getUniqueId()).isAlive() && (Integer) PlayerLG.getPlayerManager(e.getPlayer().getUniqueId()).getPreDeath().get(0) != 0 && e.getPlayer().getLocation().getBlockY() > 400){
			e.setCancelled(true);
		}
	} 
	
	@EventHandler
    public void onKick(PlayerKickEvent e){
		if(e.getReason().equals("Flying is not enabled on this server")){
			e.setCancelled(true);
		}
	}
}
