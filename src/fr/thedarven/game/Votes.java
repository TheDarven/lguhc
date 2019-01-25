package fr.thedarven.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;
import fr.thedarven.roles.Ange;

public class Votes {
	private PlayerLG playerVote;
	private PlayerLG playerHealth;
	private static Map<Player, PlayerLG> voteList;
	private ArrayList<Player> playerList;
	
	public Votes(){
		playerVote = null;
		playerHealth = null;
		voteList = new HashMap<>();
		playerList = new ArrayList<Player>();
	}
	
	public void action(){
		if(LGUHC.timer >= InventoryRegister.votes.getValue()*60){
			if(LGUHC.timer%1200 == 0){
				for(PlayerLG player : PlayerLG.getAlivePlayersManagers()) {
					if(player.isOnline()) {
						player.getPlayer().sendMessage("§6[LGUHC] Vous avez 60 secondes pour voter avec la commande /lg vote <pseudo>. Le joueur qui reçoit le plus de votes perdra la moitié de sa vie.");
					}
				}
			}
			// AFFECTE LE JOUEUR VOTE
			if(LGUHC.timer%1200 == 61 && PlayerLG.getAlivePlayersManagers().size() != 0){
				
				int nbrVote = 0;
				PlayerLG playerMaxVote = null;
				
				// AVOIR LE JOUEUR AVEC LE PLUS DE VOTE
				// if(LGUHC.playerList.size() > 0){
				if(PlayerLG.getAlivePlayersManagers().size() > 0) {
					if(voteList.size() == 0){
						Random r = new Random();	
						// playerVote = PlayerLG.getPlayerManager(LGUHC.playerList.get(r.nextInt(LGUHC.playerList.size()-1)));
						playerVote = PlayerLG.getAlivePlayersManagers().get(r.nextInt(PlayerLG.getAlivePlayersManagers().size()-1));
					}else{
						HashMap<PlayerLG,Integer> hashMapPlayer = new HashMap<PlayerLG, Integer>();
						
						Set<Entry<Player, PlayerLG>> setHm = voteList.entrySet();
					    Iterator<Entry<Player, PlayerLG>> it = setHm.iterator();
					    while(it.hasNext()){
					    	Entry<Player, PlayerLG> e = it.next();
					    	if(hashMapPlayer.containsKey(e.getValue())){
					    		hashMapPlayer.put(e.getValue(), hashMapPlayer.get(e.getValue()) + 1);
					    	}else{
					    		hashMapPlayer.put(e.getValue(), 1);
					    	}		    
					    }
					
						Set<Entry<PlayerLG,Integer>> entrySet = hashMapPlayer.entrySet();
						for(Entry<PlayerLG,Integer> entry : entrySet){
							if(entry.getKey().getRole() instanceof Ange && entry.getKey().isAlive() && entry.getKey().getRole().getActive()){
								entry.getKey().getRole().setMaxhealth(entry.getKey().getRole().getMaxhealth()+entry.getValue());
							}
							
							if(entry.getValue() > nbrVote){
								nbrVote = entry.getValue();
								playerMaxVote = entry.getKey();
							}
						}
						playerVote = playerMaxVote;
					}
					Bukkit.broadcastMessage("§6[LGUHC]§9 Le joueur §l"+Bukkit.getOfflinePlayer(playerVote.getUuid()).getName()+"§r§9 est le joueur qui a réçu le plus votes : §1"+nbrVote+"§9. Il perd la moitié de sa vie.");	
				}
			}
			
			
			
			// JOUEUR ONLINE
			if(LGUHC.timer%1200 < 360 && playerVote != null && playerVote.isOnline() && playerVote.isAlive()){ // Timer en cours
				playerVote.getPlayer().playSound(playerVote.getPlayer().getLocation(), Sound.FALL_BIG , 1, 1);
				playerVote.getPlayer().setMaxHealth(playerVote.getRole().getMaxhealth()/2);
				playerHealth = playerVote;
				playerVote = null;
			}
			if(LGUHC.timer%1200 == 360){ // Fin du timer
				if(playerHealth != null && playerHealth.isOnline()){
					playerHealth.getPlayer().setMaxHealth(playerHealth.getRole().getMaxhealth());
				}
				playerVote = null;
				playerHealth = null;
				voteList = new HashMap<>();
			}
			
			
			// JOUEUR OFFLINE
			if(LGUHC.timer%1200 == 360){ // Fin du timer
				if(playerVote != null){
					playerList.add(playerVote.getPlayer());
					playerVote = null;
				}
			}
			for(int i = 0; i<playerList.size(); i++){ // Après le timer
				if(PlayerLG.getPlayerManager(playerList.get(i).getUniqueId()).isOnline()){
					if(playerList.get(i).getHealth() > (PlayerLG.getPlayerManager(playerList.get(i).getUniqueId()).getRole().getMaxhealth()/2)){
						playerVote.getPlayer().playSound(playerVote.getPlayer().getLocation(), Sound.FALL_BIG , 1, 1);
						playerList.get(i).setHealth(PlayerLG.getPlayerManager(playerList.get(i).getUniqueId()).getRole().getMaxhealth()/2);	
					}
					playerList.remove(i);
					return;
				}
			}
			for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
				if(playerHealth != null){
					if(pl.isOnline() && !pl.equals(PlayerLG.getPlayerManager(playerHealth.getUuid()))){
						if(pl.getPlayer().getMaxHealth() != pl.getRole().getMaxhealth() && pl.getRole().getActive()){
							pl.getPlayer().setMaxHealth(pl.getRole().getMaxhealth());
						}
					}
				}else{
					if(pl.isOnline()){
						if(pl.getPlayer().getMaxHealth() != pl.getRole().getMaxhealth() && pl.getRole().getActive()){
							pl.getPlayer().setMaxHealth(pl.getRole().getMaxhealth());
						}
					}
				}
				
				if(pl.isOnline() && !pl.getRole().getActive() && getPlayer() != pl){
					if(pl.getPlayer().getMaxHealth() != pl.getRole().getMaxhealth()){
						pl.getPlayer().setMaxHealth(pl.getRole().getMaxhealth());
					}
				}
			}
		}
	}
	
	public void addVote(Player p, PlayerLG pl){
		if(!voteList.containsKey(p)){
			p.sendMessage("§6[LGUHC]§a Votre vote a bien été comptabilisé.");
			voteList.put(p, pl);
		}else{
			p.sendMessage("§6[LGUHC]§c Vous avez déjà voté.");
		}
	}
	
	public PlayerLG getPlayer(){
		if(playerVote == null){
			return playerHealth;
		}
		return playerVote;
	}
}
