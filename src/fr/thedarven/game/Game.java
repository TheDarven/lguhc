package fr.thedarven.game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.SqlConnection;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;
import fr.thedarven.roles.Assassin;
import fr.thedarven.roles.Cupidon;
import fr.thedarven.roles.InfectPèreDesLoups;
import fr.thedarven.roles.LoupBlanc;
import fr.thedarven.roles.Loups;
import fr.thedarven.roles.PetitAssassin;
import fr.thedarven.roles.RolesBis;
import fr.thedarven.roles.Spectateur;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.Title;


public class Game {
	
	public static ArrayList<Objective> objectiveRoles = new ArrayList<Objective>();
	static Objective objectiveTimer = LGUHC.board.registerNewObjective(ChatColor.GOLD+"=== LGUHC ===", "dummy");
	static Objective objectiveR = LGUHC.board.registerNewObjective("Roles", "dummy");
	static Score scoreSpace3 = objectiveTimer.getScore("§a");
	static Score scorePlayer = objectiveTimer.getScore("");
	static Score scoreSpace2 = objectiveTimer.getScore("§b");
	static Score scoreWall = objectiveTimer.getScore("");
	static Score scoreTimer = objectiveTimer.getScore("");
	static Score scoreSpace1 = objectiveTimer.getScore("§c");
	static Score scoreGroupe = objectiveTimer.getScore("");
	static Score scoreSpace = objectiveTimer.getScore("§d");
	static Score scoreSizeWall = objectiveTimer.getScore("");
	
	public static void start() {
		Bukkit.getScheduler().runTaskTimer(LGUHC.instance, new Runnable(){
			@Override
			public void run(){
				if(LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.etat.equals(EnumGame.MIDDLEGAME)){
					scoreboard();
					
					if(LGUHC.timer == 0) {
						SqlRequest.createGame();
						for (Player player : Bukkit.getOnlinePlayers()) {
							SqlRequest.createLG(player);
						}
					}
								
					if(LGUHC.timer%1200 == 0 && LGUHC.timer >= 1){
						Bukkit.broadcastMessage("§9---------- Fin Episode "+LGUHC.timer/1200+" ----------");
					}
					
					if(LGUHC.timer == InventoryRegister.annonceroles.getValue()*60){
						LGUHC.etat = EnumGame.MIDDLEGAME;
					}
					
					if(InventoryRegister.pvp.getValue() != 0 && LGUHC.timer == InventoryRegister.pvp.getValue()*60){
						Bukkit.broadcastMessage("§6[LGUHC] Le PVP est maintenant activé, bonne chance à vous !");
						for(Player p: Bukkit.getOnlinePlayers()){
							p.playSound(p.getLocation(), Sound.SHEEP_IDLE , 1.0f, 1.0f);
						}
					}
					
					if(InventoryRegister.murtime.getValue()*60 - LGUHC.timer == 0){
						Bukkit.broadcastMessage("§6[LGUHC] Le mur commence à se rétrécir, dirigez-vous vers le milieu.");
						for(Player p: Bukkit.getOnlinePlayers()){
							p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL , 1.0f, 1.0f);
						}
						World world = Bukkit.getWorld("lguhc");
						WorldBorder border = world.getWorldBorder();
						border.setCenter(0.0, 0.0);
						double taille = (double) (InventoryRegister.murtailleaprès.getValue())*2.0;
						border.setSize(taille, (long) ((long) (InventoryRegister.murtailleavant.getValue() - InventoryRegister.murtailleaprès.getValue())/(InventoryRegister.murvitesse.getValue()/100)));
					}
					
					
					boolean winLGB = true;
					boolean winLG = true;
					boolean winLove = true;
					boolean winVillage = true;
					boolean winAssassin = true;
					for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
						if(pl.isOnline()) {
							int distance = (int) Math.sqrt((Math.pow(pl.getPlayer().getLocation().getBlockX(),2)+Math.pow(pl.getPlayer().getLocation().getBlockZ(),2)));
							String message = "Entre "+((int) distance/300)*300+" et "+((int) distance/300+1)*300+" blocks";
							if(!InventoryRegister.coordonneesvisibles.getValue()) {
								Title.sendActionBar(pl.getPlayer(), ChatColor.GOLD+"Distance au centre : "+ChatColor.YELLOW+message);
							}
							if(LGUHC.etat.equals(EnumGame.MIDDLEGAME)){
								pl.getRole().startRole(pl);
								pl.getRole().verifRole(pl);
								pl.getRole().endRole(pl);
								if(LGUHC.timer < InventoryRegister.votes.getValue()*60 && pl.isOnline() && pl.isAlive() && pl.getRole().getActive()){
									if(pl.getPlayer().getMaxHealth() != pl.getRole().getMaxhealth()){
										pl.getPlayer().setMaxHealth(pl.getRole().getMaxhealth());
									}
								}
							}
						}
						if(!(pl.getRole() instanceof LoupBlanc)){
							winLGB = false;
						}
						if(!pl.getRole().getInfecte() || pl.getRole() instanceof LoupBlanc){
							winLG = false;
						}
						if(!pl.isCouple() && !(pl.getRole() instanceof Cupidon)){
							winLove = false;
						}
						if(pl.getRole().getInfecte() || pl.getRole() instanceof Assassin || pl.getRole() instanceof PetitAssassin){
							winVillage = false;
						}
						if(!(pl.getRole() instanceof Assassin) && !(pl.getRole() instanceof PetitAssassin)){
							winAssassin = false;
						}
					}
					if(winLGB){
						Bukkit.broadcastMessage("§6[LGUHC] Le Loup garou blanc à sû trahir tous le village !");
						endGame();
						winLG = false;
					}else if(winLG){
						Bukkit.broadcastMessage("§6[LGUHC] Le camp des Loups-Garous remporte la victoire !");
						endGame();
						winLove = false;
						winVillage = false;
						winAssassin = false;
					}else if(winLove){
						Bukkit.broadcastMessage("§6[LGUHC] L'amour a été le vainqueur de la partie.");
						endGame();
						winVillage = false;
						winAssassin = false;
					}else if(winVillage){
						Bukkit.broadcastMessage("§6[LGUHC] Le village a sû traquer et éliminer chacun des traîtres qui le composait, félicitation à lui !");
						endGame();
						winAssassin = false;
					}else if(winAssassin) {
						Bukkit.broadcastMessage("§6[LGUHC] L'art de l'assassinat a très bien été représenté dans cette partie et à empêcher les deux autres camps de gagner !");
						endGame();
					}
					
					int nextTime = 0;
					for(int i = LGUHC.timer+1; i<LGUHC.timer+600; i++){
						if(LGUHC.cycle.getAtSecond(LGUHC.timer) != LGUHC.cycle.getAtSecond(i) && nextTime == 0){
							nextTime = i - LGUHC.timer;
						}
					}
					if(nextTime != 0) {
						long timeset = Bukkit.getWorld("lguhc").getTime();
						if(LGUHC.cycle.getAtSecond(LGUHC.timer).equals(EnumTime.DAY)) {
							if(timeset>=24000) {
								timeset = timeset - 24000;
							}
							timeset = timeset+(12000-timeset)/nextTime;
						}else {
							timeset = timeset+(24000-timeset)/nextTime;
							if(timeset>=24000) {
								timeset = timeset - 24000;
							}
						}
						Bukkit.getWorld("lguhc").setTime(timeset);
					}
					
					LGUHC.votes.action();
					LGUHC.timer++;
				}
			}
			
			
			private void scoreboard() {
				/* CREER LES SCOREBOARD ROLES */
				if(LGUHC.timer == 0){
					scoreSpace.setScore(6);
					/* PREND TOUS LES ROLES EN UNE FOIS */
					HashSet<RolesBis<?>> listTempOfRoles = new HashSet<RolesBis<?>>();
					for(int i=0; i<LGUHC.composition.size(); i++){
						listTempOfRoles.add(LGUHC.composition.get(i));
					}
					
					/* CREER LE BON NOMBRE D'OBJECTIFS */
					for(int i=0;i<(double)listTempOfRoles.size()/10;i++){
						Objective objective = LGUHC.board.registerNewObjective("Roles "+(i+1)+"/"+(listTempOfRoles.size()/10+1), "dummy");
						objectiveRoles.add(objective);
					}
					addRolesScoreboard();
				}
				
				LGUHC.board.resetScores(scorePlayer.getEntry());
				LGUHC.board.resetScores(scoreGroupe.getEntry());
				LGUHC.board.resetScores(scoreWall.getEntry());
				LGUHC.board.resetScores(scoreTimer.getEntry());
				LGUHC.board.resetScores(scoreSizeWall.getEntry());
				
				int number = 1;
				scoreSizeWall = objectiveTimer.getScore("Bordures :§e "+(int) (Bukkit.getServer().getWorld("lguhc").getWorldBorder().getSize()/2));
				scoreSizeWall.setScore(number);
				number++;
				scoreSpace.setScore(number);
				number++;
				int innocents = 0;
				int traitres = 0;
				for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()) {
					if(pl.getRole() instanceof InfectPèreDesLoups || pl.getRole() instanceof LoupBlanc || pl.getRole() instanceof Loups) {
						traitres++;
					}else {
						innocents++;
					}
				}
				innocents = (innocents < traitres) ? innocents : traitres;
				innocents = (innocents < 3) ? 3 : innocents;
				if(InventoryRegister.taillegroupes.getValue() > 3 && InventoryRegister.taillegroupes.getValue() < innocents) {
					innocents = InventoryRegister.taillegroupes.getValue();
				}
				scoreGroupe = objectiveTimer.getScore("Groupes :§e "+innocents);
				scoreGroupe.setScore(number);
				number++;
				scoreSpace1.setScore(number);
				number++;
				
				if(LGUHC.timer<6000){
					String dateformatChrono = DurationFormatUtils.formatDuration(LGUHC.timer * 1000 , "mm:ss");
					scoreTimer = objectiveTimer.getScore("Chrono :§e "+dateformatChrono);
					scoreTimer.setScore(number);
					number++;
				}else{
					String dateformatChrono = DurationFormatUtils.formatDuration(LGUHC.timer * 1000 , "mmm:ss");
					scoreTimer = objectiveTimer.getScore("Chrono :§e "+dateformatChrono);
					scoreTimer.setScore(number);
					number++;
				}
				
				if(InventoryRegister.murtime.getValue()*60 - LGUHC.timer>0){
					if(InventoryRegister.murtime.getValue()*60 - LGUHC.timer<6000){
						String dateformatWall = DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60 - LGUHC.timer) * 1000 , "mm:ss");
						scoreWall = objectiveTimer.getScore("Mur :§e "+dateformatWall);
						scoreWall.setScore(number);
					}else{
						String dateformatWall = DurationFormatUtils.formatDuration((InventoryRegister.murtime.getValue()*60 - LGUHC.timer) * 1000 , "mmm:ss");
						scoreWall = objectiveTimer.getScore("Mur :§e "+dateformatWall);
						scoreWall.setScore(number);
					}
					number++;
				}
				scoreSpace2.setScore(number);
				number++;
				scorePlayer = objectiveTimer.getScore("Joueurs :§e "+PlayerLG.getAlivePlayersManagers().size());
				scorePlayer.setScore(number);
				number++;
				scoreSpace3.setScore(number);
				number++;


		    	/* MODIFIER LES SCOREBOARD ROLES*/
				if(LGUHC.timer%((objectiveRoles.size()+1)*InventoryRegister.dureescoreboard.getValue()) == 0 && LGUHC.timer>0 && InventoryRegister.rolesvisiblespendant.getValue()){
					HashSet<RolesBis<?>> listTempOfRoles = new HashSet<RolesBis<?>>();
					for(int i=0; i<LGUHC.composition.size(); i++){
						listTempOfRoles.add(LGUHC.composition.get(i));
					}
					Object[] listOfRoles = listTempOfRoles.toArray();
					for(int i=0; i<listOfRoles.length;i++){
						LGUHC.board.resetScores(((RolesBis<?>) listOfRoles[i]).getName());
					}
		
					addRolesScoreboard();
				}
				
				if(LGUHC.timer%((objectiveRoles.size()+1)*InventoryRegister.dureescoreboard.getValue())<InventoryRegister.dureescoreboard.getValue() || !InventoryRegister.rolesvisiblespendant.getValue() || (!InventoryRegister.rolesvisiblesavant.getValue() && LGUHC.etat.equals(EnumGame.STARTGAME))){
					objectiveTimer.setDisplaySlot(DisplaySlot.SIDEBAR);
				    for(Player p : Bukkit.getOnlinePlayers()){
				    	p.setScoreboard(LGUHC.board);
					}
				}else{
					for(int i=0;i<objectiveRoles.size();i++){
						if(LGUHC.timer%((objectiveRoles.size()+1)*InventoryRegister.dureescoreboard.getValue())<(i+2)*InventoryRegister.dureescoreboard.getValue()){
							objectiveRoles.get(i).setDisplaySlot(DisplaySlot.SIDEBAR);
						    for(Player p : Bukkit.getOnlinePlayers()){
						    	p.setScoreboard(LGUHC.board);
							}
						    return;
						}
					}
				}
			}
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			private HashMap sortByValues(Map<RolesBis<?>, Integer> listOfNumberRoles) {
				List list = new LinkedList(listOfNumberRoles.entrySet());
				
			    Collections.sort(list, new Comparator() {
				    public int compare(Object o2, Object o1) {
				    	return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
				    }
			    });

				HashMap sortedHashMap = new LinkedHashMap();
				for (Iterator it = list.iterator(); it.hasNext();) {
					Map.Entry entry = (Map.Entry) it.next();
						sortedHashMap.put(entry.getKey(), entry.getValue());
					} 
				return sortedHashMap;
			}
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			private void addRolesScoreboard(){
				/* PREND TOUS LES ROLES EN UNE FOIS */
				HashSet<RolesBis<?>> listTempOfRoles = new HashSet<RolesBis<?>>();
				for(int i=0; i<LGUHC.composition.size(); i++){
					listTempOfRoles.add(LGUHC.composition.get(i));
				}

				/* ATTRIBUE A CHAQUE ROLES LE NOMBRE DE JOUEUR */
				HashMap<RolesBis<?>, Integer> listOfNumberRoles = new HashMap<RolesBis<?>, Integer>();
				Object[] listOfRoles = listTempOfRoles.toArray();
				for(int i=0; i<listOfRoles.length;i++){
					int number = 0;
					for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
						if(pl.getRole().getName().equals(((RolesBis<?>) listOfRoles[i]).getName()) && pl.isAlive()){
							number++;
						}
					}
					listOfNumberRoles.put(((RolesBis<?>) listOfRoles[i]), number);
				}
				
				/* CREER LES SCORES DES SCOREBOARD */
				Map<RolesBis<?>, Integer> listOfNumberRolesEnd = sortByValues(listOfNumberRoles);
				Set set = listOfNumberRolesEnd.entrySet();
			    Iterator iterator2 = set.iterator();
			    int roleNumber = 0;
			    while(iterator2.hasNext()){
			    	Map.Entry me2 = (Map.Entry)iterator2.next();
			    	for(int i=0;i<objectiveRoles.size();i++){
			    		if(roleNumber/10>=i && roleNumber/10<i+1){
			    			if((int) (me2.getValue()) == 0){
			    				Score score = objectiveRoles.get(i).getScore(ChatColor.GRAY+""+ChatColor.STRIKETHROUGH+((RolesBis<?>) me2.getKey()).getName());
			    				score.setScore(0);
			    			}else{
			    				Score score = objectiveRoles.get(i).getScore(((RolesBis<?>) me2.getKey()).getName());
			    				score.setScore((int) (me2.getValue()));
			    			}
			    		}
			    	}		    	
			        roleNumber++;
			    }
			}
		},20,20);
	}
	
	public static void endGame(){
		for(Player p : Bukkit.getOnlinePlayers()){
			p.playSound(p.getLocation(), Sound.WITHER_SPAWN , 1.0f, 1.0f);
		}
		LGUHC.etat = EnumGame.END;
		
		for(PlayerLG pl : PlayerLG.getAllPlayersManagers()){
			if(!(pl.getRole() instanceof Spectateur)){
				String message = "§5";
				if(pl.getRole().getInfecte()) {
					message += "☭ ";
				}
				if(!pl.isAlive()) {
					message += "§m";
				}
				message += Bukkit.getOfflinePlayer(pl.getUuid()).getName()+"("+pl.getKills()+") :§o "+pl.getRole().getName();
				if(pl.isCouple()) {
					message += "❤ ";
				}
				Bukkit.broadcastMessage(message);
			}
		}
		
		for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
			try {
	             PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT win FROM players WHERE uuid = ?");
	             q.setString(1, pl.getUuid().toString());
	             ResultSet resultat = q.executeQuery();
	             
	             while(resultat.next()){
			    	try {
        				PreparedStatement q1 = SqlConnection.connection.prepareStatement("UPDATE players SET win = ? WHERE uuid = ?");
        	            q1.setInt(1,resultat.getInt("win")+1);
        	            q1.setString(2, pl.getUuid().toString());
        	            q1.execute();
        	            q1.close();
        	        } catch (SQLException error) {
        	        	error.printStackTrace();
        	        }
	             }
	    	 } catch (SQLException error) {
	    		 error.printStackTrace();
    	     }
		}
		SqlRequest.updateGameDuree();
	}
}
