package fr.thedarven.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;
import fr.thedarven.roles.Chaman;
import fr.thedarven.roles.Spectateur;

@SuppressWarnings("deprecation")
public class Tchat implements Listener {

	public Tchat(LGUHC pl) {
	}
	
	@EventHandler
	public void writeTchat(PlayerChatEvent e){
		if(LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.etat.equals(EnumGame.MIDDLEGAME)){
			e.setCancelled(true);
			
			if(PlayerLG.getPlayerManager(e.getPlayer().getUniqueId()).getRole() instanceof Spectateur || !PlayerLG.getPlayerManager(e.getPlayer().getUniqueId()).isAlive()) {
				for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
					if(pl.getRole() instanceof Chaman && pl.getRole().verifCommand(pl)){
						pl.getPlayer().sendMessage("§k§8Pseudo §r§7: "+e.getMessage());
					}
				}
				
				for(Player p : Bukkit.getOnlinePlayers()) {
					PlayerLG pl = PlayerLG.getPlayerManager(p.getUniqueId());
					if(pl.getRole() instanceof Spectateur || !pl.isAlive()) {
						p.sendMessage("§7[Mort] "+e.getPlayer().getName()+": "+e.getMessage());
					}
				}	
			}
		}
	}
	
	@EventHandler
	public void writeCommand(PlayerCommandPreprocessEvent e){
		if(LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.etat.equals(EnumGame.MIDDLEGAME)) {
			if(e.getMessage().startsWith("/me") || e.getMessage().startsWith("/tell") || e.getMessage().startsWith("/tellraw") || e.getMessage().startsWith("/say") || e.getMessage().startsWith("/msg")){
				e.setCancelled(true);
			}	
		}
	}
}
