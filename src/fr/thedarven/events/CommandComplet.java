package fr.thedarven.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.google.common.collect.ImmutableList;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;
import fr.thedarven.roles.*;

public class CommandComplet implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
    	Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if(cmd.getName().equals("lg")){
        	if(args.length == 1){
        		if(LGUHC.etat.equals(EnumGame.WAIT)){
        			Object[][] COMMANDS = { {"config", "g", "players", "scenarios", "start"}, {null, null, null, null, null }, {true, true, false, false, true} };
        			List<String> completions = new ArrayList<String>();
        			for(int i=0; i<COMMANDS[0].length; i++) {
        				boolean ajout = true;
        				if(COMMANDS[1][i] != null && !COMMANDS[1][i].getClass().getName().equals(PlayerLG.getPlayerManager(Bukkit.getPlayerExact(sender.getName()).getUniqueId()).getRole().getClass().getName())) {
        					ajout = false;
        				}
        				if((boolean) COMMANDS[2][i] == true && !Bukkit.getPlayerExact(sender.getName()).isOp() || !((String)COMMANDS[0][i]).startsWith(args[args.length-1])) {
        					ajout = false;
        				}
        				if(ajout) {
        					
        					completions.add((String) COMMANDS[0][i]);
        				}
        			}
            		return completions;
        		}else{
        			Object[][] COMMANDS = { {"chat", "flairer", "g", "heal", "kill", "look", "look", "love", "modele", "players", "proteger", "revive", "role", "scenarios", "transform", "volonté", "vote"}, {new Soeur(), new Renard(), null, null, null, new Voyante(), new VoyanteBavarde(), new Cupidon(), new EnfantSauvage(), null, new Salvateur(), new Sorcière(), null, null, new InfectPèreDesLoups(), new Villageois(), null}, {false, false, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false} };
        			List<String> completions = new ArrayList<String>();
        			for(int i=0; i<COMMANDS[0].length; i++) {
        				boolean ajout = true;
        				if(COMMANDS[1][i] != null && !COMMANDS[1][i].getClass().getName().equals(PlayerLG.getPlayerManager(Bukkit.getPlayerExact(sender.getName()).getUniqueId()).getRole().getClass().getName())) {
        					ajout = false;
        				}
        				if(COMMANDS[1][i] != null && !LGUHC.etat.equals(EnumGame.MIDDLEGAME)) {
        					ajout = false;
        				}
        				if((boolean) COMMANDS[2][i] == true && !Bukkit.getPlayerExact(sender.getName()).isOp() || !((String)COMMANDS[0][i]).startsWith(args[args.length-1])) {
        					ajout = false;
        				}
        				if(ajout) {
        					completions.add((String) COMMANDS[0][i]);
        				}
        			}
            		return completions;
        		}
        	}else if(args.length == 2){
        		if(args[0].equals("flairer")){
 	        		List<String> completions = new ArrayList<String>();
 	        		// for(UUID uuid : LGUHC.playerList){
 	        		for(PlayerLG player : PlayerLG.getAlivePlayersManagers()){
 	        			if(Bukkit.getOfflinePlayer(player.getUuid()).getName().startsWith(args[args.length-1]))
 	        				completions.add(Bukkit.getOfflinePlayer(player.getUuid()).getName());
	 	       		}
 	        		return completions; 
        		}else if(args[0].equals("kill")){
 	        		List<String> completions = new ArrayList<String>();
 	        		for(PlayerLG player : PlayerLG.getAlivePlayersManagers()){
 	        			if(Bukkit.getOfflinePlayer(player.getUuid()).getName().toLowerCase().startsWith(args[args.length-1].toLowerCase()))
 	        				completions.add(Bukkit.getOfflinePlayer(player.getUuid()).getName());
	 	       		}
 	        		return completions; 
        		}else if(args[0].equals("look")){
	        		List<String> completions = new ArrayList<String>();
	        		// for(UUID uuid : LGUHC.playerList){
	        		for(PlayerLG player : PlayerLG.getAlivePlayersManagers()){
	        			if(Bukkit.getOfflinePlayer(player.getUuid()).getName().toLowerCase().startsWith(args[args.length-1].toLowerCase()))
	        				completions.add(Bukkit.getOfflinePlayer(player.getUuid()).getName());
	 	       		}
		        	return completions; 
        		}else if(args[0].equals("love")){
 	        		List<String> completions = new ArrayList<String>();
 	        		// for(UUID uuid : LGUHC.playerList){
 	        		for(PlayerLG player : PlayerLG.getAlivePlayersManagers()){
 	        			if(Bukkit.getOfflinePlayer(player.getUuid()).getName().toLowerCase().startsWith(args[args.length-1].toLowerCase()))
 	        				completions.add(Bukkit.getOfflinePlayer(player.getUuid()).getName());
	 	       		}
 	        		return completions; 
        		}else if(args[0].equals("modele")){
 	        		List<String> completions = new ArrayList<String>();
 	        		// for(UUID uuid : LGUHC.playerList){
 	        		for(PlayerLG player : PlayerLG.getAlivePlayersManagers()){
 	        			if(!Bukkit.getOfflinePlayer(player.getUuid()).getName().equals(sender.getName()) && Bukkit.getOfflinePlayer(player.getUuid()).getName().toLowerCase().startsWith(args[args.length-1].toLowerCase())){
 	        				completions.add(Bukkit.getOfflinePlayer(player.getUuid()).getName());
 	        			}
	 	       		}
 	        		return completions; 
        		}else if(args[0].equals("proteger")){
 	        		List<String> completions = new ArrayList<String>();
 	        		// for(UUID uuid : LGUHC.playerList){
 	        		for(PlayerLG player : PlayerLG.getAlivePlayersManagers()){
 	        			if(Bukkit.getOfflinePlayer(player.getUuid()).getName().toLowerCase().startsWith(args[args.length-1].toLowerCase()))
 	        				completions.add(Bukkit.getOfflinePlayer(player.getUuid()).getName());
	 	       		}
 	        		return completions; 
        		}else if(args[0].equals("revive")){
 	        		List<String> completions = new ArrayList<String>();
 	        		
 	        		// for(UUID uuid : LGUHC.playerList){
 	        		for(PlayerLG player : PlayerLG.getAlivePlayersManagers()){
 	        			if(player.isOnline() && !(player.getRole() instanceof Spectateur) && player.isAlive() && player.getPlayer().getGameMode().equals(GameMode.SPECTATOR) && Bukkit.getOfflinePlayer(player.getUuid()).getName().toLowerCase().startsWith(args[args.length-1].toLowerCase()))
 	        				completions.add(Bukkit.getOfflinePlayer(player.getUuid()).getName());
	 	       		}
 	        		return completions; 
        		}else if(args[0].equals("transform")){
 	        		List<String> completions = new ArrayList<String>();
 	        		// for(UUID uuid : LGUHC.playerList){
 	        		for(PlayerLG player : PlayerLG.getAlivePlayersManagers()){
 	        			if(player.isOnline() && !(player.getRole() instanceof Spectateur) && player.isAlive() && player.getPlayer().getGameMode().equals(GameMode.SPECTATOR) && Bukkit.getOfflinePlayer(player.getUuid()).getName().toLowerCase().startsWith(args[args.length-1].toLowerCase()))
 	        				completions.add(Bukkit.getOfflinePlayer(player.getUuid()).getName());
	 	       		}
 	        		return completions; 
        		}else if(args[0].equals("vote")){
	        		List<String> completions = new ArrayList<String>();
	        		// for(UUID uuid : LGUHC.playerList){
	        		for(PlayerLG player : PlayerLG.getAlivePlayersManagers()){
	        			if(Bukkit.getOfflinePlayer(player.getUuid()).getName().toLowerCase().startsWith(args[args.length-1].toLowerCase()))
	        				completions.add(Bukkit.getOfflinePlayer(player.getUuid()).getName());
	 	       		}
		        	return completions; 
        		}
        	}else if(args.length == 3){
        		if(args[0].equals("love")){
        			List<String> completions = new ArrayList<String>();
        			// for(UUID uuid : LGUHC.playerList){
        			for(PlayerLG player : PlayerLG.getAlivePlayersManagers()){
        				if(Bukkit.getOfflinePlayer(player.getUuid()).getName().toLowerCase().startsWith(args[args.length-1].toLowerCase()))
        					completions.add(Bukkit.getOfflinePlayer(player.getUuid()).getName());
	 	       		}
  	        		return completions; 
         		} 
        	}
        }
        return ImmutableList.of();
    }
}
