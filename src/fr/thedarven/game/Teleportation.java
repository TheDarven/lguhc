package fr.thedarven.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.Title;
import fr.thedarven.utils.effects.ParticleEffect;

public class Teleportation {
	
	public static void start() {
		LGUHC.etat = EnumGame.TELEPORTATION;
		Collections.shuffle(LGUHC.composition);
		Collections.shuffle(LGUHC.composition);					
		
		final List<PlayerLG> playerList = new ArrayList<PlayerLG>();
		for(Player player : Bukkit.getOnlinePlayers()){
			playerList.add(PlayerLG.getPlayerManager(player.getUniqueId()));
			if(!InventoryRegister.coordonneesvisibles.getValue()) {
				DisableF3.disableF3(player);
			}
		}
		
		
		new BukkitRunnable(){
			int timeSeparate = InventoryRegister.teleportationvitesse.getValue();
			int a = timeSeparate-1;
			int rayon = InventoryRegister.murtailleavant.getValue() - 50;
			double X;
			int Z = -1;
			
			@Override
			public void run() {
				if(a < playerList.size()*timeSeparate){
					if(a%timeSeparate == timeSeparate-1) {
						Z++;
						X = Z * (6.283184/Bukkit.getOnlinePlayers().size());
						int location_x = (int) (rayon * Math.cos(X));
						int location_z = (int) (rayon * Math.sin(X));
						if(playerList.get(a/timeSeparate).isOnline()) {
							playerList.get(a/timeSeparate).getPlayer().teleport(new Location(Bukkit.getWorld("lguhc"), location_x, Bukkit.getWorld("lguhc").getHighestBlockYAt(location_x, location_z)+10,location_z));
						}
						playerList.get(a/timeSeparate).getPlayer().setGameMode(GameMode.SURVIVAL);	
						playerList.get(a/timeSeparate).getPlayer().getInventory().clear();
						
						for(int i=0; i<10; i++) {
							X = i * (6.283184/10);
							double location_x2 = 0.7*Math.cos(X);
							double location_z2 = 0.7*Math.sin(X);
							ParticleEffect.CLOUD.display(0, 0, 0, 0.001f, 100, playerList.get(a/timeSeparate).getPlayer().getLocation().add(location_x2, 0, location_z2), 10);
						}
						
						for(Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ITEM_PICKUP , 1, 1);
						}
						
					}
					for(Player player : Bukkit.getOnlinePlayers()) {
						Title.sendActionBar(player, ChatColor.GOLD+"Teleportation : "+ChatColor.YELLOW+((int)(a+1)/timeSeparate)+"/"+playerList.size());
					}
				}else if(a >= playerList.size()*timeSeparate && a <= playerList.size()*timeSeparate+4) {
					if(a == playerList.size()*timeSeparate){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.RED +"5", "", 10);
						}
					}
					if(a == playerList.size()*timeSeparate+1){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.YELLOW +"4", "", 10);
						}
					}
					if(a == playerList.size()*timeSeparate+2){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.GOLD +"3", "", 10);
						}
					}
					if(a == playerList.size()*timeSeparate+3){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.GREEN +"2", "", 10);
						}
					}
					if(a == playerList.size()*timeSeparate+4){
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ORB_PICKUP , 1, 1);
							Title.title(player, ChatColor.DARK_GREEN +"1", "", 10);
						}
					}						
				}else {
					this.cancel();
					Game.start();
					
					LGUHC.etat = EnumGame.STARTGAME;
					
					// SPAWN DELETE
					Location loc = new Location(Bukkit.getWorld("lguhc"), 0, 205, 0);
					for(Entity e : loc.getWorld().getEntities()){
						if(loc.distance(e.getLocation()) <= 100 && e instanceof ArmorStand){
							e.remove();
						}
					}
					for(int i = 0; i<LGUHC.jump.allBlock.size(); i++){
						LGUHC.jump.allBlock.get(i).getLocation().getBlock().setType(Material.AIR);
					}
					for(int i = -15; i<16; i++){
						for(int j = -15; j<16; j++){
							for(int k = 200; k<204; k++){
								Bukkit.getWorld("lguhc").getBlockAt(i, k, j).setType(Material.AIR);
							}
						}
					}
					
					// EFFETS ET CLEAR
					for(int i=0; i<playerList.size(); i++){
						if(playerList.get(i).isOnline()){
							playerList.get(i).setDeath(false);
							playerList.get(i).setRole(LGUHC.composition.get(i));	
							// LGUHC.playerList.add(playerList.get(i).getUuid());
							playerList.get(i).getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2, true, false));
							playerList.get(i).getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 0, true, false));
							playerList.get(i).getPlayer().playSound(playerList.get(i).getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL , 1.0f, 1.0f);
							Title.title(playerList.get(i).getPlayer(), ChatColor.DARK_GREEN +"Go !", "", 10);
						}
					}

					if(LGUHC.cycle.getAtSecond(0).equals(EnumTime.DAY)) {
						Bukkit.getWorld("lguhc").setTime(0);
					}else {
						Bukkit.getWorld("lguhc").setTime(12000);
					}
					
					Bukkit.broadcastMessage("§6[LGUHC]§a La partie commence, que le meilleur gagne !");
				}
				a++;
			}
		
		}.runTaskTimer(LGUHC.instance,20,20);
	}
	
}
