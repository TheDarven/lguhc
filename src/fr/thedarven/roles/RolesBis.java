package fr.thedarven.roles;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.Game;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;
import fr.thedarven.utils.SqlRequest;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class RolesBis<T> implements Listener{
	protected boolean active;
	protected ArrayList<EffetClass> effect;
	protected boolean infecte;
	protected boolean kit;
	protected String name;
	protected T pouvoir;
	protected boolean taupelist;
	protected double maxhealth;
	
	// ACTIVE
	public boolean getActive() {
		return this.active;
	}
	
	public void setActive(boolean value) {
		this.active = value;
	}
	
	// INFECTE
	public boolean getInfecte() {
		return this.infecte;
	}
	
	public void setInfecte(UUID uuid, boolean value) {
		this.infecte = value;
		SqlRequest.updateLGInfect√©(uuid, value);
	}
	
	// NAME
	public String getName() {
		return this.name;
	}
	
	// POUVOIR
	public T getPouvoir() {
		return this.pouvoir;
	}
	
	public void setPouvoir(T value) {
		this.pouvoir = value;
	}
	
	// TAUPELIST
	public boolean getTaupelist() {
		return this.taupelist;
	}
	
	public void setTaupelist(boolean value) {
		this.taupelist = value;
	}
	
	// VIE
	public double getMaxhealth() {
		return this.maxhealth;
	}
	
	public void setMaxhealth(double value) {
		this.maxhealth = value;
	}
	
	// MESSAGE ANNONCE
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().playSound(pl.getPlayer().getLocation(), Sound.CREEPER_HISS , 1.0f, 1.0f);
			if(pl.getRole().getInfecte()){
				String pseudo = "¬ß6[LGUHC] ¬ß4Voici la liste des loups-garous :  ¬ßc";
				// for(UUID uuid : LGUHC.playerList){
				for(PlayerLG player : PlayerLG.getAlivePlayersManagers()) {
					if((player.getRole().getInfecte() || player.getRole().getTaupelist()) && player.isAlive()){
						pseudo += Bukkit.getOfflinePlayer(player.getUuid()).getName()+"  ";
					}
				}
				pl.getPlayer().sendMessage(pseudo);
			}
		}
	}
	
	// LES EFFETS
	public void verifRole(PlayerLG pl) {
		if(LGUHC.cycle.getAtSecond(LGUHC.timer-1) != LGUHC.cycle.getAtSecond(LGUHC.timer) || LGUHC.timer == InventoryRegister.annonceroles.getValue()*60) {
			applyEffects(pl);
		}
	}
	
	abstract public void startRole(PlayerLG pl);
	abstract public void endRole(PlayerLG pl);
	
	public boolean verifCommand(PlayerLG pl) {
		return true;
	}
	
	public void applyEffects(PlayerLG pl) {
		if(pl.isOnline() && pl.isAlive() && pl.getRole().getActive()) {
			
			// SELECTIONE LES EFFETS NORMALEMENT ACTIFS
			ArrayList<EffetClass> activePotion = new ArrayList<EffetClass>();
			for(EffetClass effects : effect) {
				if(effects.getPeriode().equals(LGUHC.cycle.getNow()) && (!effects.getNeedActive() || getActive()) && (!effects.getNeedInfecte() || getInfecte())) {
					activePotion.add(effects);
				}
			}
			
			// SUPPRIMES LES EFFETS
			for(PotionEffect potion : pl.getPlayer().getActivePotionEffects()) {
				if(potion.getDuration() > 10000) {
					boolean dedans = false;
					for(EffetClass effects : activePotion) {
						if(potion.getAmplifier() == effects.getPotion().getAmplifier() && potion.getType().equals(effects.getPotion().getType())) {
							dedans = true;
						}
					}
					if(!dedans) {
						pl.getPlayer().removePotionEffect(potion.getType());
					}
				}
			}
			
			// AJOUTE LES EFFETS
			for(EffetClass effects : activePotion) {
				boolean ajouter = true;
				for(PotionEffect potion : pl.getPlayer().getActivePotionEffects()) {
					if(potion.getType().equals(effects.getPotion().getType()) && (potion.getAmplifier() > effects.getPotion().getAmplifier())){
						ajouter = false;
					}
				}
				if(ajouter) {
					pl.getPlayer().addPotionEffect(effects.getPotion());
				}
			}
		}
	}
	
	public void removeEffect(EffetClass effectValue, PlayerLG pl) {
		for(int i=0; i<effect.size(); i++) {
			if(effect.get(i).getPeriode().equals(effectValue.getPeriode()) && effect.get(i).getPotion().getType().equals(effectValue.getPotion().getType()) && effect.get(i).getPotion().getAmplifier() == effectValue.getPotion().getAmplifier() && effect.get(i).getNeedActive() == effectValue.getNeedActive() && effect.get(i).getNeedActive() == effectValue.getNeedInfecte()) {
				effect.remove(i);
				return;
			}
		}
		applyEffects(pl);
	}
	
	public void addEffect(EffetClass effectValue, PlayerLG pl) {
		effect.add(effectValue);
		applyEffects(pl);
	}
	
	public void playerDeath(final Player p, final UUID pKill){
		final PlayerLG pm = PlayerLG.getPlayerManager(p.getUniqueId());
		final UUID uuid = p.getUniqueId();
		
		// EN VIE ET PAS EN TRAIN DE MOURRIR
		if(pm.isAlive() && (Integer) pm.getPreDeath().get(0) == 0){
			int timer = 0;
			for(PlayerLG pl : PlayerLG.getAllPlayersManagers()){
				if(pl.getRole() instanceof Sorci√®re || pl.getRole() instanceof InfectP√®reDesLoups)
					timer = timer + 6;
			}
			pm.setPreDeath(0, timer);
			
			// SI ON PEUT LE RESSUCITER
			if(LGUHC.etat.equals(EnumGame.MIDDLEGAME)){
				pm.setInventory(0, p.getLocation());
				pm.setInventory(1, p.getInventory());
				pm.setInventory(2, p.getTotalExperience());
				
				
				// Enlever le fall au tp
				// Disable les damage
				
				// Spectator 3 √† la reconnexion
				
				
				p.setGameMode(GameMode.ADVENTURE);
				p.sendMessage("¬ß6[LGUHC]¬ß9 Vous √™tes mort mais on peut vous ressusciter, merci de patienter quelques instant.");
				Location loc = new Location(Bukkit.getWorld("lguhc"), 0.0, 1000.0, 0.0);
				p.teleport(loc);
				
				new BukkitRunnable(){
					boolean infectAt6 = true;
					
					@Override
					public void run(){
						if((Integer) pm.getPreDeath().get(0) == 12) {
							infectAt6 = false;
						}else if(infectAt6 && (Integer) pm.getPreDeath().get(0) == 6) {
							infectAt6 = false;
							for(PlayerLG pl : PlayerLG.getAllPlayersManagers()){
								if(pl.getRole() instanceof InfectP√®reDesLoups)
									infectAt6 = true;
							}
						}
						
						if((Integer) pm.getPreDeath().get(0) == 12 && pKill != null && PlayerLG.getPlayerManager(pKill).getRole().getInfecte()){
							for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
								if(pl.getRole() instanceof InfectP√®reDesLoups){
									if(pl.getRole().verifCommand(pl)) {
										TextComponent message = new TextComponent("¬ß6[LGUHC]¬ß6 Le ¬ß6joueur "+p.getName()+" ¬ß6est ¬ß6mort. ¬ß6Vous ¬ß6avez ¬ß66 ¬ß6secondes ¬ß6pour ¬ß6le ¬ß6transformer ¬ß6en ¬ß6cliquant ¬ß6ici.");  // AFFICHE
										message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("¬ß9Transformer "+p.getName()).create()));   // SURVOL
										message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg transform "+p.getName()));
										pl.getPlayer().spigot().sendMessage(message);
										pm.setPreDeath(0, (Integer) pm.getPreDeath().get(0)-1);
										pm.setPreDeath(1, "InfecteP√®reDesLoups");	
									}
									break;
								}
							}
						}
						
						if((Integer) pm.getPreDeath().get(0) == 6){
							if(infectAt6) {
								if(pKill != null && PlayerLG.getPlayerManager(pKill).getRole().getInfecte()){
									for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
										if(pl.getRole() instanceof InfectP√®reDesLoups){
											if(pl.getRole().verifCommand(pl)) {
												TextComponent message = new TextComponent("¬ß6[LGUHC]¬ß6 Le ¬ß6joueur "+p.getName()+" ¬ß6est ¬ß6mort. ¬ß6Vous ¬ß6avez ¬ß66 ¬ß6secondes ¬ß6pour ¬ß6le ¬ß6transformer ¬ß6en ¬ß6cliquant ¬ß6ici.");  // AFFICHE
												message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("¬ß9Transformer "+p.getName()).create()));   // SURVOL
												message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg transform "+p.getName()));
												pl.getPlayer().spigot().sendMessage(message);
												pm.setPreDeath(0, (Integer) pm.getPreDeath().get(0)-1);
												pm.setPreDeath(1, "InfecteP√®reDesLoups");
											}
											break;
										}
									}
								}
							}else {
								for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
									if(pl.getRole() instanceof Sorci√®re){
										if(pl.getRole().verifCommand(pl)) {
											TextComponent message = new TextComponent("¬ß6[LGUHC]¬ß6 Le joueur "+p.getName()+" ¬ß6est ¬ß6mort. ¬ß6Vous ¬ß6avez ¬ß66 ¬ß6secondes ¬ß6pour ¬ß6le ¬ß6sauver ¬ß6en ¬ß6cliquant ¬ß6ici.");  // AFFICHE
											message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("¬ß9Sauver "+p.getName()).create()));   // SURVOL
											message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg revive "+p.getName()));
											pl.getPlayer().spigot().sendMessage(message);
											pm.setPreDeath(0, (Integer) pm.getPreDeath().get(0)-1);
											pm.setPreDeath(1, "Sorci√®re");
										}
										break;
									}
								}
							}
						}
						
						if((Integer) pm.getPreDeath().get(0) == 0){
							killPlayer(uuid, false);
							this.cancel();
						}
						
						// ON LE REVIVE
						if((Integer) pm.getPreDeath().get(0) < 0) {
							
							// SORCIERE QUI REVIVE
							if((Integer) pm.getPreDeath().get(0) == -1){

								// TELEPORT
								p.sendMessage("¬ß6[LGUHC]¬ße La sorci√®re vient de vous ressusciter.");
								
								detectVictory();
							}
							
							// PERE DES LOUPS QUI REVIVE
							if((Integer) pm.getPreDeath().get(0) == -2){
								
								// TELEPORT
								p.sendMessage("¬ß6[LGUHC]¬ße L'infecte p√®re des loups vient de vous transformer : vous faites d√©sormais partie du camp des Loups-Garous.");
								
								
								String pseudo = "¬ß6[LGUHC] ¬ß4Voici la liste des loups-garous :  ¬ßc";
								// for(UUID uuid : LGUHC.playerList){
								for(PlayerLG player : PlayerLG.getAlivePlayersManagers()) {
									if((player.getRole().getInfecte() || player.getRole().getTaupelist()) && player.isAlive()){
										pseudo += Bukkit.getOfflinePlayer(uuid).getName()+"  ";
									}
								}
								p.sendMessage(pseudo);
								for(Player player : Bukkit.getOnlinePlayers()){
									if(PlayerLG.getPlayerManager(player.getUniqueId()).getRole().getInfecte() && PlayerLG.getPlayerManager(player.getUniqueId()).isAlive()){
										player.sendMessage("¬ß6[LGUHC]¬ßc "+p.getName()+" a rejoint le camp des Loups-Garous.");
									}
								}
								
								detectVictory();
							}
							pm.setPreDeath(0, 0);
							p.setFallDistance(0);
							p.setHealth(pm.getRole().getMaxhealth());
							p.setGameMode(GameMode.SURVIVAL);
							
							Random r = new Random();
							int border = (int) (Bukkit.getServer().getWorld("lguhc").getWorldBorder().getSize()/2)-20;
							int x = -border + r.nextInt(border*2);
							int z = -border + r.nextInt(border*2);
							int y = Bukkit.getWorld("lguhc").getHighestBlockYAt(x,z);
							Location loc = new Location(Bukkit.getWorld("lguhc"), x, y+1, z);
							p.teleport(loc);
							
							this.cancel();
						}
						
						if((Integer) pm.getPreDeath().get(0) > 0){
							pm.setPreDeath(0, (Integer) pm.getPreDeath().get(0)-1);	
						}
					}
				}.runTaskTimer(LGUHC.instance,20,20);
			}else{
				pm.setInventory(0, p.getLocation());
				pm.setInventory(1, p.getInventory());
				pm.setInventory(2, p.getTotalExperience());
				killPlayer(uuid, false);
			}	
		}
		
		
		
		
		// EN VIE ET PAS EN TRAIN DE MOURRIR
		/* if(pm.isAlive() && (Integer) pm.getPreDeath().get(0) == 0){
			int timer = 0;
			for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
				if(pl.getRole() instanceof Sorci√®re || (pKill != null && pl.getRole() instanceof InfectP√®reDesLoups && PlayerLG.getPlayerManager(pKill).getRole().getInfecte())){
					timer = timer + 6;
				}
			}
			pm.setPreDeath(0, timer);
			
			// SI ON PEUT LE RESSUCITER
			if(LGUHC.etat.equals(EnumGame.MIDDLEGAME)){
				pm.setInventory(0, p.getLocation());
				pm.setInventory(1, p.getInventory());
				pm.setInventory(2, p.getTotalExperience());
				
				
				// Enlever le fall au tp
				// Disable les damage
				
				// Spectator 3 √† la reconnexion
				
				
				p.setGameMode(GameMode.ADVENTURE);
				p.sendMessage("¬ß6[LGUHC]¬ß9 Vous √™tes mort mais on peut vous ressusciter, merci de patienter quelques instant.");
				Location loc = new Location(Bukkit.getWorld("lguhc"), 0.0, 1000.0, 0.0);
				p.teleport(loc);
				
				new BukkitRunnable(){
					boolean infectAt6 = true;
					
					@Override
					public void run(){
						if((Integer) pm.getPreDeath().get(0) == 12) {
							infectAt6 = false;
						}
						
						if((Integer) pm.getPreDeath().get(0) == 12 && pKill != null && PlayerLG.getPlayerManager(pKill).getRole().getInfecte()){
							for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
								if(pl.getRole() instanceof InfectP√®reDesLoups && pl.getRole().verifCommand(pl)){
									TextComponent message = new TextComponent("¬ß6[LGUHC]¬ß6 Le joueur "+p.getName()+" est mort. Vous avez 6 secondes pour le transformer en cliquant ici.");  // AFFICHE
									message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("¬ß9Transformer "+p.getName()).create()));   // SURVOL
									message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg transform "+p.getName()));
									pl.getPlayer().spigot().sendMessage(message);
									pm.setPreDeath(0, (Integer) pm.getPreDeath().get(0)-1);
									pm.setPreDeath(1, "InfecteP√®reDesLoups");
									return;
								}
							}
						}
						if((Integer) pm.getPreDeath().get(0) == 6){
							for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
								if(pl.getRole() instanceof Sorci√®re && pl.getRole().verifCommand(pl)){
									TextComponent message = new TextComponent("¬ß6[LGUHC]¬ß6 Le joueur "+p.getName()+" ¬ß6est ¬ß6mort. ¬ß6Vous ¬ß6avez ¬ß66 ¬ß6secondes ¬ß6pour ¬ß6le ¬ß6sauver ¬ß6en ¬ß6cliquant ¬ß6ici.");  // AFFICHE
									message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("¬ß9Sauver "+p.getName()).create()));   // SURVOL
									message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg revive "+p.getName()));
									pl.getPlayer().spigot().sendMessage(message);
									pm.setPreDeath(0, (Integer) pm.getPreDeath().get(0)-1);
									pm.setPreDeath(1, "Sorci√®re");
									return;
								}else if(pKill != null && pl.getRole() instanceof InfectP√®reDesLoups && pl.getRole().verifCommand(pl) && PlayerLG.getPlayerManager(pKill).getRole().getInfecte() && infectAt6){
									TextComponent message = new TextComponent("¬ß6[LGUHC]¬ß6 Le ¬ß6joueur "+p.getName()+" ¬ß6est ¬ß6mort. ¬ß6Vous ¬ß6avez ¬ß66 ¬ß6secondes ¬ß6pour ¬ß6le ¬ß6transformer ¬ß6en ¬ß6cliquant ¬ß6ici.");  // AFFICHE
									message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("¬ß9Transformer "+p.getName()).create()));   // SURVOL
									message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg transform "+p.getName()));
									pl.getPlayer().spigot().sendMessage(message);
									pm.setPreDeath(0, (Integer) pm.getPreDeath().get(0)-1);
									pm.setPreDeath(1, "InfecteP√®reDesLoups");
									return;
								}
							}
						}
						
						if((Integer) pm.getPreDeath().get(0) == 0){
							killPlayer(uuid, false);
							this.cancel();
						}
						
						// ON LE REVIVE
						if((Integer) pm.getPreDeath().get(0) < 0) {
							
							// SORCIERE QUI REVIVE
							if((Integer) pm.getPreDeath().get(0) == -1){

								// TELEPORT
								p.sendMessage("¬ß6[LGUHC]¬ße La sorci√®re vient de vous ressusciter.");					
							}
							
							// PERE DES LOUPS QUI REVIVE
							if((Integer) pm.getPreDeath().get(0) == -2){
								
								// TELEPORT
								p.sendMessage("¬ß6[LGUHC]¬ße L'infecte p√®re des loups vient de vous transformer : vous faites d√©sormais partie du camp des Loups-Garous.");
								
								
								String pseudo = "¬ß6[LGUHC] ¬ß4Voici la liste des loups-garous :  ¬ßc";
								// for(UUID uuid : LGUHC.playerList){
								for(PlayerLG player : PlayerLG.getAlivePlayersManagers()) {
									if((player.getRole().getInfecte() || player.getRole().getTaupelist()) && player.isAlive()){
										pseudo += Bukkit.getOfflinePlayer(uuid).getName()+"  ";
									}
								}
								p.sendMessage(pseudo);
								for(Player player : Bukkit.getOnlinePlayers()){
									if(PlayerLG.getPlayerManager(player.getUniqueId()).getRole().getInfecte() && PlayerLG.getPlayerManager(player.getUniqueId()).isAlive()){
										player.sendMessage("¬ß6[LGUHC]¬ßc "+p.getName()+" a rejoint le camp des Loups-Garous.");
									}
								}
							}
							pm.setPreDeath(0, 0);
							p.setFallDistance(0);
							p.setHealth(pm.getRole().getMaxhealth());
							p.setGameMode(GameMode.SURVIVAL);
							
							Random r = new Random();
							int border = (int) (Bukkit.getServer().getWorld("lguhc").getWorldBorder().getSize()/2)-20;
							int x = -border + r.nextInt(border*2);
							int z = -border + r.nextInt(border*2);
							int y = Bukkit.getWorld("lguhc").getHighestBlockYAt(x,z);
							Location loc = new Location(Bukkit.getWorld("lguhc"), x, y+1, z);
							p.teleport(loc);
							
							this.cancel();
						}
						
						if((Integer) pm.getPreDeath().get(0) > 0){
							pm.setPreDeath(0, (Integer) pm.getPreDeath().get(0)-1);	
						}
					}
				}.runTaskTimer(LGUHC.instance,20,20);
			}else{
				pm.setInventory(0, p.getLocation());
				pm.setInventory(1, p.getInventory());
				pm.setInventory(2, p.getTotalExperience());
				killPlayer(uuid, false);
			}	
		} */
	}
	
	public static void killPlayer(UUID uuid, boolean couple){
		SqlRequest.updateLGMort(uuid.toString(), 1);
		
		PlayerLG pm = PlayerLG.getPlayerManager(uuid);
		if(pm.isAlive()){
			pm.setDeath(true);
			pm.setPreDeath(0, 0);
			pm.setPreDeath(1, "aucun");
			// LGUHC.playerList.remove(uuid);
			
			for(Player pl : Bukkit.getOnlinePlayers()){
				pl.playSound(pl.getLocation(), Sound.AMBIENCE_THUNDER , 1.0f, 1.0f);
			}
			Bukkit.broadcastMessage("¬ßc========= ‚ė≠ =========");
			if(couple){
				Bukkit.broadcastMessage("¬ßa Dans un √©lan de chagrin, "+Bukkit.getOfflinePlayer(uuid).getName()+" rejoint son amoureux dans sa tombe : il √©tait "+pm.getRole().getName()+".");
			}else{
				Bukkit.broadcastMessage("¬ßa Le village a perdu un de ses membres : "+Bukkit.getOfflinePlayer(uuid).getName()+" qui √©tait "+pm.getRole().getName()+".");
			}
			Bukkit.broadcastMessage("¬ßc========= ‚ė≠ =========");
			if(pm.getRole() instanceof Villageois && pm.getRole().getActive() && pm.getRole().getPouvoir() != null) {
				Bukkit.broadcastMessage("¬ß6[LGUHC]¬ße Sa derni√®re volont√© √©tait : "+pm.getRole().getPouvoir());
			}
			
			PlayerInventory inv = (PlayerInventory) pm.getInventory().get(1);
			if(inv != null){
				Location loc = (Location) pm.getInventory().get(0);
				if(inv.getHelmet() != null){
					Bukkit.getWorld("lguhc").dropItemNaturally(loc, inv.getHelmet());
				}
				if(inv.getChestplate() != null){
					Bukkit.getWorld("lguhc").dropItemNaturally(loc, inv.getChestplate());
				}
				if(inv.getLeggings() != null){
					Bukkit.getWorld("lguhc").dropItemNaturally(loc, inv.getLeggings());
				}
				if(inv.getBoots() != null){
					Bukkit.getWorld("lguhc").dropItemNaturally(loc, inv.getBoots());
				}
		
				for(ItemStack i : inv){
		            if(i != null){
		                Bukkit.getWorld("lguhc").dropItemNaturally(loc, i);
		            }
		        }
				
				if((int) ((int) pm.getInventory().get(2) / 7) < 100){
					ExperienceOrb exp = (ExperienceOrb) Bukkit.getWorld("lguhc").spawnEntity(loc, EntityType.EXPERIENCE_ORB);
					exp.setExperience((int) ((int) pm.getInventory().get(2) / 7));	
				}else{
					ExperienceOrb exp = (ExperienceOrb) Bukkit.getWorld("lguhc").spawnEntity(loc, EntityType.EXPERIENCE_ORB);
					exp.setExperience(100);
				}
				
				
				
				if(InventoryRegister.goldenhead.getValue() > 0 && LGUHC.timer >= InventoryRegister.pvp.getValue()*60){
					ItemStack tete = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
					SkullMeta teteM = (SkullMeta) tete.getItemMeta();
					teteM.setOwner(Bukkit.getOfflinePlayer(uuid).getName());
					teteM.setDisplayName(ChatColor.GOLD+"T√™te de "+Bukkit.getOfflinePlayer(uuid).getName());
					tete.setItemMeta(teteM);
					
					Bukkit.getWorld("lguhc").dropItemNaturally(loc, tete);
				}	
			}		
			
			if(pm.isOnline()){
				pm.getPlayer().sendMessage("¬ß6[LGUHC]¬ßc Vous √™tes √† pr√©sent mort. Merci de vous muter ou de changer de channel mumble.");
				pm.getPlayer().setGameMode(GameMode.SPECTATOR);
				Location loc = new Location(Bukkit.getWorld("lguhc"), 0.0, Bukkit.getWorld("lguhc").getHighestBlockYAt(0, 0)+50, 0.0);
				pm.getPlayer().teleport(loc);
				pm.getPlayer().getInventory().clear();
				pm.setPreDeath(2, true);
			}
			
			if(pm.isCouple()){
				for(PlayerLG pm1 : PlayerLG.getAlivePlayersManagers()){
					if(pm1.isCouple() && pm1 != pm){
						killPlayer(pm1.getUuid(), true);	
					}
				}
			}
			
			for(PlayerLG player: PlayerLG.getAlivePlayersManagers()){
				if(player.getRole() instanceof EnfantSauvage){
					EnfantSauvage role = (EnfantSauvage) player.getRole();
					if(role.getPouvoir() != null && role.getPouvoir().equals(uuid) && !role.getInfecte()){
						role.setInfecte(player.getUuid(), true);
						if(player.isOnline()){
							player.getPlayer().sendMessage("¬ß6[LGUHC]¬ße Votre mod√®le vient de mourrir, vous rejoignez donc le camp des Loups-Garous.");
							String pseudo = "¬ß6[LGUHC] ¬ß4Voici la liste des loups-garous :  ¬ßc";
							// for(UUID uuid1 : LGUHC.playerList){
							for(PlayerLG players : PlayerLG.getAlivePlayersManagers()) {
								if((players.getRole().getInfecte() || players.getRole().getTaupelist()) && players.isAlive()){
									pseudo += Bukkit.getOfflinePlayer(players.getUuid()).getName()+"  ";
								}
							}
							player.getPlayer().sendMessage(pseudo);
						}
						for(Player player1 : Bukkit.getOnlinePlayers()){
							if(PlayerLG.getPlayerManager(player1.getUniqueId()).getRole().getInfecte() && PlayerLG.getPlayerManager(player1.getUniqueId()).isAlive()){
								player1.sendMessage("¬ß6[LGUHC]¬ßc "+Bukkit.getOfflinePlayer(player.getUuid()).getName()+" a rejoint le camp des Loups-Garous.");
							}
						}
					}
					break;
				}
			}
			
			if(!pm.isCouple() || couple)
				detectVictory();
			
		}
	}
	
	private static void detectVictory() {
		/* Verif victoire */
		
		LGUHC.winLGB = true;
		LGUHC.winLG = true;
		LGUHC.winLove = true;
		LGUHC.winVillage = true;
		LGUHC.winAssassin = true;
		
		for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()){
			/* if(pl.isOnline()) {
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
			} */

			if(!(pl.getRole() instanceof LoupBlanc)){
				LGUHC.winLGB = false;
			}
			if(!pl.getRole().getInfecte() || pl.getRole() instanceof LoupBlanc){
				LGUHC.winLG = false;
			}
			if(!pl.isCouple() && !(pl.getRole() instanceof Cupidon)){
				LGUHC.winLove = false;
			}
			if(pl.getRole().getInfecte() || pl.getRole() instanceof Assassin || pl.getRole() instanceof PetitAssassin){
				LGUHC.winVillage = false;
			}
			if(!(pl.getRole() instanceof Assassin) && !(pl.getRole() instanceof PetitAssassin)){
				LGUHC.winAssassin = false;
			}
		}
		if(LGUHC.winLGB){
			Bukkit.broadcastMessage("¬ß6[LGUHC] Le Loup garou blanc √† s√Ľ trahir tous le village !");
			Game.endGame();
			LGUHC.winLG = false;
		}else if(LGUHC.winLG){
			Bukkit.broadcastMessage("¬ß6[LGUHC] Le camp des Loups-Garous remporte la victoire !");
			Game.endGame();
			LGUHC.winLove = false;
			LGUHC.winVillage = false;
			LGUHC.winAssassin = false;
		}else if(LGUHC.winLove){
			Bukkit.broadcastMessage("¬ß6[LGUHC] L'amour a √©t√© le vainqueur de la partie.");
			Game.endGame();
			LGUHC.winVillage = false;
			LGUHC.winAssassin = false;
		}else if(LGUHC.winVillage){
			Bukkit.broadcastMessage("¬ß6[LGUHC] Le village a s√Ľ traquer et √©liminer chacun des tra√ģtres qui le composait, f√©licitation √† lui !");
			Game.endGame();
			LGUHC.winAssassin = false;
		}else if(LGUHC.winAssassin) {
			Bukkit.broadcastMessage("¬ß6[LGUHC] L'art de l'assassinat a tr√®s bien √©t√© repr√©sent√© dans cette partie et √† emp√™cher les deux autres camps de gagner !");
			Game.endGame();
		}
	}
}
