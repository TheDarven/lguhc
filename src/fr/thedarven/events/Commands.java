package fr.thedarven.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.Teleportation;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;
import fr.thedarven.roles.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			PlayerLG pl = PlayerLG.getPlayerManager(p.getUniqueId());
			if(cmd.getName().equalsIgnoreCase("lg") && args.length > 0){				
				if(args[0].equals("scenarios")){
					p.openInventory(InventoryRegister.configuration.getInventory());
				}
				
				// Commandes d'administrateurs
				if(p.isOp()){
					if(args.length >= 1) {
						if(args[0].equals("config") && LGUHC.etat.equals(EnumGame.WAIT)){
							p.openInventory(InventoryRegister.menu.getInventory());
						}else if(args[0].equals("g")){
							String message = " ";
							for(int i=1; i<args.length; i++){
								message = message+args[i]+" ";
							}
							Bukkit.broadcastMessage(" ");
							message = "§l︳ §e"+p.getName()+" §r§7≫§a"+message;
							
							TextComponent messageFinal = new TextComponent(message);  // AFFICHE
							messageFinal.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClique ici pour voir.").create()));   // SURVOL
							messageFinal.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg "+LGUHC.succes_password));
							for(Player player : Bukkit.getOnlinePlayers()) {
								player.spigot().sendMessage(messageFinal);
							}
							
							Bukkit.broadcastMessage(" ");
						}else if(args[0].equals("heal") && (LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.etat.equals(EnumGame.MIDDLEGAME))){
							for(Player player : Bukkit.getOnlinePlayers()) {
								player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 100, true, false));
							}
							Bukkit.broadcastMessage("§6[LGUHC] §aVous venez d'être soigné.");
						}else if(args[0].equals("start") && LGUHC.etat.equals(EnumGame.WAIT)){
							if(LGUHC.composition.size() != Bukkit.getOnlinePlayers().size()){
								p.sendMessage("§6[LGUHC]§c Démarrage impossible : le nombre de rôle ne correspond pas au nombre de joueurs connectés.");
								return true;
							}else{
								Teleportation.start();
							}
						}
						else if(args.length >= 2 && args[0].equals("timer") && LGUHC.developpement && LGUHC.timer > 0){
							LGUHC.timer = Integer.parseInt(args[1]);
						}
					}
					if(args.length == 1 && args[0].equals(LGUHC.succes_password)) {
						p.sendMessage("ha");
					}
					
					if(args.length >= 2 && args[0].equals("kill") && playerInGame(args[1]) != null) {
						PlayerLG player = PlayerLG.getPlayerManager(playerInGame(args[1]));
						if(!player.isAlive() || (Integer) player.getPreDeath().get(0) != 0) {
							return false;
						}
						if(player.isCouple()) {
							for(PlayerLG players : PlayerLG.getAlivePlayersManagers()) {
								if(players.isCouple()) {
									players.setCouple(false);
								}
							}
							Bukkit.broadcastMessage("§6[LGUHC]§c Le couple vient d'être dissout.");
						}
						player.setInventory(1, null);
						RolesBis.killPlayer(player.getUuid(), false);
					}
				}
				
				if(args.length >= 2 && pl.getRole() instanceof Soeur && pl.getRole().verifCommand(pl)){
					if(args[0].equals("chat") && args[1] != null){
						String message = "";
						for(int i=1; i<args.length; i++) {
							message += args[i]+" ";
						}
						if(message.length() > 100){
							p.sendMessage("§6[LGUHC]§c Le message est trop long pour être enregistré (maximum 100 caractères).");
							return false;
						}
						((Soeur) pl.getRole()).setPouvoir(message);
						p.sendMessage("§6[LGUHC]§a Ton message a été enregistré avec succès, ta soeur le recevra bientôt.");
					}
				}
				if(args.length >= 2 && pl.getRole() instanceof Renard && pl.getRole().verifCommand(pl)){
					if(args[0].equals("flairer") && playerInGame(args[1]) != null){
						if(PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Renard || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Spectateur || !PlayerLG.getPlayerManager(playerInGame(args[1])).isAlive() || !PlayerLG.getPlayerManager(playerInGame(args[1])).isOnline()){
							return false;
						}
						
						Player player = Bukkit.getPlayer(playerInGame(args[1]));
						
						if(p.getLocation().distance(player.getLocation()) <= 10){
							((Renard) pl.getRole()).setPouvoir(((Renard) pl.getRole()).getPouvoir()-1);;
							if(PlayerLG.getPlayerManager(player.getUniqueId()).getRole().getInfecte()){
								p.sendMessage("§6[LGUHC]§4 Le joueur "+player.getName()+" appartient au camp des Loups-Garous.");
							}else{
								p.sendMessage("§6[LGUHC]§a Le joueur "+player.getName()+" est innocent.");
							}	
						}else{
							p.sendMessage("§6[LGUHC]§c Vous devez être à moins de 10 blocs du joueur.");
						}
						
					}
				}
				if(args.length >= 2 && pl.getRole() instanceof Voyante && pl.getRole().verifCommand(pl)){
					if(args[0].equals("look") && playerInGame(args[1]) != null){
						if(PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Spectateur || !PlayerLG.getPlayerManager(playerInGame(args[1])).isAlive()){
							return false;
						}
						((Voyante) pl.getRole()).setPouvoir(false);
						if(PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Loups || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof LoupBlanc || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof InfectPèreDesLoups || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Assassin || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof PetitAssassin || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof EnfantSauvage){
							p.sendMessage("§6[LGUHC]§4 Vous avez espionné un joueur qui est "+PlayerLG.getPlayerManager(playerInGame(args[1])).getRole().getName()+".");
						}else{
							p.sendMessage("§6[LGUHC]§a Vous avez espionné un joueur qui est "+PlayerLG.getPlayerManager(playerInGame(args[1])).getRole().getName()+".");
						}
					}
				}
				if(args.length >= 2 && pl.getRole() instanceof VoyanteBavarde && pl.getRole().verifCommand(pl)){
					if(args[0].equals("look") && playerInGame(args[1]) != null){
						if(PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Spectateur || !PlayerLG.getPlayerManager(playerInGame(args[1])).isAlive()){
							return false;
						}
						((VoyanteBavarde) pl.getRole()).setPouvoir(false);
						if(PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Loups || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof LoupBlanc || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof InfectPèreDesLoups || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Assassin || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof PetitAssassin || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof EnfantSauvage){
							Bukkit.broadcastMessage("§6[LGUHC]§4 La Voyante a espionné un joueur qui est "+PlayerLG.getPlayerManager(playerInGame(args[1])).getRole().getName()+".");
						}else{
							Bukkit.broadcastMessage("§6[LGUHC]§a La Voyante a espionné un joueur qui est "+PlayerLG.getPlayerManager(playerInGame(args[1])).getRole().getName()+".");
						}
					}
				}
				if(args.length >= 3 && pl.getRole() instanceof Cupidon && pl.getRole().verifCommand(pl)){
					if(args[0].equals("love") && playerInGame(args[1]) != null && playerInGame(args[2]) != null && playerInGame(args[1]) != playerInGame(args[2])){
						if(PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Spectateur || !PlayerLG.getPlayerManager(playerInGame(args[1])).isAlive() || PlayerLG.getPlayerManager(playerInGame(args[2])).getRole() instanceof Spectateur || !PlayerLG.getPlayerManager(playerInGame(args[2])).isAlive()){
							return false;
						}
						PlayerLG.getPlayerManager(playerInGame(args[1])).setCouple(true);
						PlayerLG.getPlayerManager(playerInGame(args[2])).setCouple(true);
						
						((Cupidon) pl.getRole()).messageCouple(pl);
					}
				}
				if(args.length >= 1 && pl.getRole() instanceof EnfantSauvage && pl.getRole().verifCommand(pl)){
					if(args[0].equals("modele") && ((EnfantSauvage) pl.getRole()).getPouvoir() == null && playerInGame(args[1]) != null && PlayerLG.getPlayerManager(playerInGame(args[1])).isAlive()){
						p.sendMessage("§6[LGUHC]§e Votre modèle est "+Bukkit.getOfflinePlayer(((EnfantSauvage) pl.getRole()).getPouvoir()).getName()+". Si il meurt, vous rejoindrez directement le camp des Loups-Garous.");
					}
				}
				if(args.length >= 2 && pl.getRole() instanceof EnfantSauvage && pl.getRole().verifCommand(pl)){
					if(args[0].equals("modele") && ((EnfantSauvage) pl.getRole()).getPouvoir() == null && playerInGame(args[1]) != null){
						if(PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof EnfantSauvage || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Spectateur || !PlayerLG.getPlayerManager(playerInGame(args[1])).isAlive()){
							return false;
						}
						((EnfantSauvage) pl.getRole()).setPouvoir(playerInGame(args[1]));
						p.sendMessage("§6[LGUHC]§a Votre modèle ("+Bukkit.getOfflinePlayer(playerInGame(args[1])).getName()+") a bien été enregistré. S'il vient à mourir, vous rejoindrez le camp des Loups-Garous.");
					}
				}
				if(args.length >= 1 && args[0].equals("players")){
					String message = "§eListe des joueurs : ";
					if(LGUHC.etat.equals(EnumGame.WAIT) || LGUHC.etat.equals(EnumGame.TELEPORTATION)) {
						for(PlayerLG player : PlayerLG.getAllPlayersManagers()) {
							if((int) player.getPreDeath().get(0) != 0) {
								message+="§6";
							}else if(player.isOnline()) {
								message+="§a";
							}else {
								message+="§c";
							}
							message+=Bukkit.getOfflinePlayer(player.getUuid()).getName()+" §l§f︱ §r";
						}
					}else {
						for(PlayerLG player : PlayerLG.getAlivePlayersManagers()) {
							if((int) player.getPreDeath().get(0) != 0) {
								message+="§6";
							}else if(player.isOnline()) {
								message+="§a";
							}else {
								message+="§c";
							}
							message+=Bukkit.getOfflinePlayer(player.getUuid()).getName()+" §l§f︱ §r";
						}	
					}
					p.sendMessage(message);
				}
				if(args.length >= 2 && pl.getRole() instanceof Salvateur && pl.getRole().verifCommand(pl)){
					if(args[0].equals("proteger") && playerInGame(args[1]) != null){
						if(playerInGame(args[1]).equals(((Salvateur) pl.getRole()).getPouvoir().get(0))){
							p.sendMessage("§6[LGUHC]§c Vous ne pouvez pas protéger le même joueur deux fois d'affilé.");
							return false;
						}
						((Salvateur) pl.getRole()).getPouvoir().set(1, playerInGame(args[1]));
						PlayerLG.getPlayerManager(playerInGame(args[1])).getRole().addEffect(new EffetClass(EnumTime.DAY, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40000, 0), false, false));
						PlayerLG.getPlayerManager(playerInGame(args[1])).getRole().addEffect(new EffetClass(EnumTime.NIGHT, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40000, 0), false, false));
						p.sendMessage("§6[LGUHC]§a Le joueur "+Bukkit.getOfflinePlayer(playerInGame(args[1])).getName()+" a été protégé par ta volonté.");
						Player pSelect = PlayerLG.getPlayerManager(playerInGame(args[1])).getPlayer();
						if(pSelect != null)
							pSelect.sendMessage("§6[LGUHC]§a Le salvateur a décidé de vous protéger. Vous obtenez Resistance I et NoFall durant 20 minutes.");
					}
				}
				if(args.length >= 2 && pl.getRole() instanceof Sorcière && pl.getRole().verifCommand(pl)){
					if(args[0].equals("revive") && playerInGame(args[1]) != null){
						if(!Bukkit.getOfflinePlayer(playerInGame(args[1])).isOnline() || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Spectateur || !PlayerLG.getPlayerManager(playerInGame(args[1])).isAlive() || !Bukkit.getPlayer(playerInGame(args[1])).getGameMode().equals(GameMode.ADVENTURE) || PlayerLG.getPlayerManager(playerInGame(args[1])).getPreDeath().get(1) != "Sorcière"){
							return false;
						}						
						p.sendMessage("§6[LGUHC]§a Vous avez réssusciter "+Bukkit.getOfflinePlayer(playerInGame(args[1])).getName()+" avec succès.");
						((Sorcière) pl.getRole()).setPouvoir(false);
						PlayerLG.getPlayerManager(playerInGame(args[1])).setPreDeath(1, "aucun");
						PlayerLG.getPlayerManager(playerInGame(args[1])).setPreDeath(0, -1);
					}
				}
				if(args.length >= 1 && !(pl.getRole() instanceof Spectateur) && LGUHC.etat.equals(EnumGame.MIDDLEGAME)){
					if(args[0].equals("role")){
						pl.getRole().messageRole(pl);
					}
				}
				if(args.length >= 2 && pl.getRole() instanceof InfectPèreDesLoups && pl.getRole().verifCommand(pl)){
					if(args[0].equals("transform") && args[1] != null && playerInGame(args[1]) != null){
						if(!Bukkit.getOfflinePlayer(playerInGame(args[1])).isOnline() || PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Spectateur || !PlayerLG.getPlayerManager(playerInGame(args[1])).isAlive() || !Bukkit.getPlayer(playerInGame(args[1])).getGameMode().equals(GameMode.ADVENTURE) || PlayerLG.getPlayerManager(playerInGame(args[1])).getPreDeath().get(1) != "InfectePèreDesLoups"){
							return false;
						}	
						p.sendMessage("§6[LGUHC]§a Vous avez transformé "+Bukkit.getOfflinePlayer(playerInGame(args[1])).getName()+" avec succès.");
						((InfectPèreDesLoups) pl.getRole()).setPouvoir(false);
						PlayerLG.getPlayerManager(playerInGame(args[1])).getRole().setInfecte(Bukkit.getPlayer(playerInGame(args[1])).getUniqueId(),true);;
						PlayerLG.getPlayerManager(playerInGame(args[1])).setPreDeath(1, "aucun");
						PlayerLG.getPlayerManager(playerInGame(args[1])).setPreDeath(0, -2);
					}
				}
				if(args.length >= 2 && pl.getRole() instanceof Villageois && pl.getRole().verifCommand(pl)){
					if(args[0].equals("volonté") && args[1] != null){
						String message = "";
						for(int i=1; i<args.length; i++) {
							message += args[i]+" ";
						}
						if(message.length() > 100){
							p.sendMessage("§6[LGUHC]§c Le message est trop long pour être enregistré (maximum 100 caractères).");
							return false;
						}
						((Villageois) pl.getRole()).setPouvoir(message);
						p.sendMessage("§6[LGUHC]§a Dernière volonté enregistré avec succès.");
					}
				}
				if(args.length >= 2 && !(pl.getRole() instanceof Spectateur) && pl.isAlive() && LGUHC.timer >= InventoryRegister.votes.getValue()*60 && LGUHC.timer%1200 <= 60){
					if(args[0].equals("vote") && playerInGame(args[1]) != null){
						if(!(PlayerLG.getPlayerManager(playerInGame(args[1])).getRole() instanceof Spectateur)){
							LGUHC.votes.addVote(p, PlayerLG.getPlayerManager(playerInGame(args[1])));
							return true;
						}
						p.sendMessage("§6[LGUHC]§c Un erreur a eu lieu dans le vote.");
					}
				}
			}
		}
		return false;
	}
	
	public UUID playerInGame(String name){
		if(name != null){
			/* for(OfflinePlayer p : Bukkit.getOfflinePlayers()){	
				if(LGUHC.playerList.contains(p.getUniqueId())){
					if(p.getName().equals(name)){
						return p.getUniqueId();
					}
				}
			} */
			for(PlayerLG player : PlayerLG.getAlivePlayersManagers()) {
				if(Bukkit.getOfflinePlayer(player.getUuid()).getName().equals(name)) {
					return player.getUuid();
				}
			}
		}
		return null;
	}
}
