package fr.thedarven.events;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;
import fr.thedarven.roles.Assassin;
import fr.thedarven.roles.InfectPèreDesLoups;
import fr.thedarven.roles.PetitAssassin;
import fr.thedarven.roles.RolesBis;
import fr.thedarven.roles.Salvateur;
import fr.thedarven.roles.Spectateur;
import fr.thedarven.roles.Villageois;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.avancements.Avancements;

public class Death implements Listener {
	
	public Death(LGUHC pl) {
	}
	
	@EventHandler
	public void onDeath(EntityDamageEvent e){
		if(LGUHC.etat.equals(EnumGame.WAIT) && (e.getEntity() instanceof Player || e.getEntity() instanceof ArmorStand)){
			e.setCancelled(true);
			if(e.getEntity() instanceof ArmorStand) {
				if(e.getEntity().getName().contains("§e")) {
					int number = Integer.parseInt(e.getEntity().getName().substring(2, e.getEntity().getName().lastIndexOf("/")))-1;
					int number_max = Integer.parseInt(e.getEntity().getName().substring(e.getEntity().getName().lastIndexOf("/")+1, e.getEntity().getName().length()));
					if(number > 0) {
						e.getEntity().setCustomName("§e"+number+"/"+number_max);
					}else {
						e.getEntity().setCustomName("§e"+number_max*10+"/"+number_max*10);
						if(e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)e).getDamager() instanceof Player) {
							Player player = (Player) ((EntityDamageByEntityEvent)e).getDamager();
							Avancements.modifierAvancement(player, "detruire_punching_ball");
						}
						/* Bukkit.broadcastMessage("§e§kABCDE§r§a SUCCÉS DÉVÉROUILLIÉ : §nTuer l'armorstand§e §kABCDE");
						for(Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.LEVEL_UP , 1, 1);
						} */
					}
				}
				
			}
		}else if(LGUHC.etat.equals(EnumGame.TELEPORTATION)){
			e.setCancelled(true);
		}else if(e.getEntity() instanceof Player){
			final Player p = (Player) e.getEntity();
			final PlayerLG pm = PlayerLG.getPlayerManager(p.getUniqueId());	
			
			if(LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.etat.equals(EnumGame.MIDDLEGAME)){
				if(LGUHC.timer < 60) {
					e.setCancelled(true);
				}else {
					if(e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)e).getDamager() instanceof Player){
						pm.setLastPunch(PlayerLG.getPlayerManager(((EntityDamageByEntityEvent)e).getDamager().getUniqueId()), LGUHC.timer);
						for(PotionEffect potion : ((Player) ((EntityDamageByEntityEvent)e).getDamager()).getActivePotionEffects()) {
							if(potion.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
								e.setDamage(e.getDamage()-(potion.getAmplifier()+1)*0.75);
							}
						}
					}
					
					
					if(e.getCause().equals(DamageCause.FALL)) {
						for(PlayerLG pl : PlayerLG.getAllPlayersManagers()) {
							if(pl.getRole() instanceof Salvateur) {
								if(((Salvateur) pl.getRole()).getPouvoir().get(1) != null && ((Salvateur) pl.getRole()).getPouvoir().get(1).equals(pm.getUuid())) {
									e.setCancelled(true);
									return;
								}
							}
						}
					}
					if(p.getHealth() - e.getFinalDamage() <= 0){
						e.setCancelled(true);
						
						// Echange son rôle avec un villageois
						if(LGUHC.etat.equals(EnumGame.STARTGAME) && pm.isAlive() && !(pm.getRole() instanceof Villageois) && !(pm.getRole() instanceof Spectateur)) {
							boolean changement = false;
							for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()) {
								if(!changement && !pl.getUuid().equals(pm.getUuid()) && pl.getRole() instanceof Villageois) {
									RolesBis<?> villageois = pl.getRole();
									pl.setRole(pm.getRole());
									pm.setRole(villageois);
									changement = true;
								}
							}
						}
						
						PlayerLG damager = pm.getLastPunch();
						if(damager == null) {
							pm.getRole().playerDeath(p,null);
						}else {
							if(damager.getRole() instanceof InfectPèreDesLoups && damager.isAlive()) {
								damager.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1, true, false));
							}else if(damager.getRole().getInfecte() && damager.isAlive()){
								damager.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0, true, false));
							}else if((damager.getRole() instanceof Assassin || damager.getRole() instanceof PetitAssassin) && damager.isAlive()){
								damager.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1, true, false));	
							}
						    SqlRequest.updateLGKill(damager.getPlayer());
						    damager.addKill();
						    pm.getRole().playerDeath(p,damager.getUuid());
						}
					}
					
					/* if(p.getHealth() - e.getFinalDamage() <= 0){							
					if(LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.etat.equals(EnumGame.MIDDLEGAME)){
						e.setCancelled(true);
						
						if(LGUHC.etat.equals(EnumGame.STARTGAME) && pm.isAlive() && !(pm.getRole() instanceof Villageois) && !(pm.getRole() instanceof Spectateur)) {
							boolean changement = false;
							for(PlayerLG pl : PlayerLG.getAlivePlayersManagers()) {
								if(!changement && !pl.getUuid().equals(pm.getUuid()) && pl.getRole() instanceof Villageois) {
									RolesBis<?> villageois = pl.getRole();
									pl.setRole(pm.getRole());
									pm.setRole(villageois);
									changement = true;
								}
							}
						}
						if(LGUHC.timer > 60){
							UUID damager = null;
							if (e instanceof EntityDamageByEntityEvent) {
							    if(((EntityDamageByEntityEvent)e).getDamager() instanceof Player){
							    	damager = ((EntityDamageByEntityEvent)e).getDamager().getUniqueId();
							    	if(PlayerLG.getPlayerManager(damager).getRole() instanceof InfectePèreDesLoups && PlayerLG.getPlayerManager(damager).isAlive()) {
							    		Bukkit.getPlayer(damager).addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1, true, false));
							    	}else if(PlayerLG.getPlayerManager(damager).getRole().getInfecte() && PlayerLG.getPlayerManager(damager).isAlive()){
							    		Bukkit.getPlayer(damager).addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0, true, false));
							    	}else if((PlayerLG.getPlayerManager(damager).getRole() instanceof Assassin || PlayerLG.getPlayerManager(damager).getRole() instanceof PetitAssassin) && PlayerLG.getPlayerManager(damager).isAlive()){
							    		Bukkit.getPlayer(damager).addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1, true, false));	
							    	}
							    	try {
							             PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT kills FROM players WHERE uuid = ?");
							             q.setString(1, damager.toString());
							             ResultSet resultat = q.executeQuery();
							             
							             while(resultat.next()){
									    	try {
						        				PreparedStatement q1 = SqlConnection.connection.prepareStatement("UPDATE players SET kills = ? WHERE uuid = ?");
						        	            q1.setInt(1,resultat.getInt("kills")+1);
						        	            q1.setString(2, damager.toString());
						        	            q1.execute();
						        	            q1.close();
						        	        } catch (SQLException error) {
						        	        	error.printStackTrace();
						        	        }
							             }
							    	 } catch (SQLException error) {
							    		 error.printStackTrace();
					        	     }	
							    	SqlRequest.updateLGKill(Bukkit.getPlayer(damager));
							    }
							}
							((RolesBis<?>) pm.getRole()).playerDeath(p,damager);
						}
					}
				} */
				}
			}
		}
	}
}
