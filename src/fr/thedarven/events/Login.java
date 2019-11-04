package fr.thedarven.events;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;
import fr.thedarven.roles.Spectateur;
import fr.thedarven.utils.DisableF3;
import fr.thedarven.utils.SqlConnection;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.Title;
import net.md_5.bungee.api.ChatColor;

public class Login implements Listener {

	public Login(LGUHC pl) {
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		SqlRequest.updatePlayerLast(p);
        PlayerLG pm = PlayerLG.getPlayerManager(p.getUniqueId());
        if(!LGUHC.developpement && LGUHC.sqlConnect && !hasAccount(e.getPlayer())) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO players(uuid,name) VALUES (?,?)");
	            q.setString(1, e.getPlayer().getUniqueId().toString());
	            q.setString(2, e.getPlayer().getName());
	            q.execute();
	            q.close();
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
		}
        
        // SI RECONNEXION APRES MORT
        if(!pm.isAlive() && !pm.hasDeathInfo()){
        	
        	p.setGameMode(GameMode.SPECTATOR);
			Location loc = new Location(Bukkit.getWorld("lguhc"), 0.0, Bukkit.getWorld("lguhc").getHighestBlockYAt(0, 0)+50, 0.0);
			p.teleport(loc);
			p.getInventory().clear();
			pm.setPreDeath(2, true);
			p.sendMessage("§6[LGUHC]§c Vous êtes à présent mort. Merci de vous muter ou de changer de channel mumble.");
        }
        
        // SI RECONNEXION EN SPECTATEUR
        /*if((LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.etat.equals(EnumGame.MIDDLEGAME)) && pm.getRole() instanceof Spectateur){
        	p.setGameMode(GameMode.SPECTATOR);
        	p.sendMessage("§6[LGUHC]§c Vous êtes à présent mort. Merci de vous muter ou de changer de channel mumble.");
        } */
        
        if(LGUHC.etat.equals(EnumGame.WAIT)){
        	loginWaitAction(p);
			
			if(LGUHC.developpement && Bukkit.getOnlinePlayers().size() == 1) {
				Title.title(p, ChatColor.GOLD +"Mode test : ON", "", 20);
			}
		}else if(LGUHC.etat.equals(EnumGame.TELEPORTATION) || LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.etat.equals(EnumGame.MIDDLEGAME)) {
			if(pm.getRole() instanceof Spectateur) {
				p.setGameMode(GameMode.SPECTATOR);
			}else {
				if(LGUHC.etat != EnumGame.TELEPORTATION)
					pm.getRole().applyEffects(pm);
			}
			if(!InventoryRegister.coordonneesvisibles.getValue()) {
				DisableF3.disableF3(p);
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		Player p = e.getPlayer();
        PlayerLG pm = PlayerLG.getPlayerManager(p.getUniqueId());
        if(LGUHC.etat.equals(EnumGame.TELEPORTATION)) {
        	Bukkit.getScheduler().cancelAllTasks();
        	LGUHC.etat = EnumGame.WAIT;
        	for(Player player : Bukkit.getOnlinePlayers()) {
        		player.playSound(player.getLocation(), Sound.WITHER_HURT , 1, 1);
				Title.title(player, ChatColor.RED +"Lancement annulé", "", 10);
				PlayerLG.getPlayerManager(p.getUniqueId()).setRole(new Spectateur());
				loginWaitAction(player);
			}
        }
        if((Integer) pm.getPreDeath().get(0) == 0 && pm.isAlive()){
	        pm.setInventory(0, p.getLocation());
	        pm.setInventory(1, p.getInventory());
	        pm.setInventory(2, p.getTotalExperience());
        }
        SqlRequest.updatePlayerTimePlay(p);
		SqlRequest.updatePlayerLast(p);
	}
	
	public static boolean hasAccount(Player p){      
        try {
            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT name FROM players WHERE uuid = ?");
            q.setString(1, p.getUniqueId().toString());
            ResultSet resultat = q.executeQuery();
            
            while(resultat.next()){
            	if(!resultat.getString("name").equals(p.getName())){
            		try {
        				PreparedStatement q1 = SqlConnection.connection.prepareStatement("UPDATE players SET name = ? WHERE uuid = ?");
        	            q1.setString(1,p.getName());
        	            q1.setString(2, p.getUniqueId().toString());
        	            q1.execute();
        	            q1.close();
        	        } catch (SQLException error) {
        	        	error.printStackTrace();
        	        }
            	}
            	return true;
            }
            q.close();         
        } catch (SQLException error) {
        	error.printStackTrace();
        }
       
        return false;
    }
	
	public static void loginWaitAction(Player p) {
		p.teleport(new Location(Bukkit.getWorld("lguhc"),0,202,0));
		p.setMaxHealth(20);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setLevel(0);
		p.getInventory().clear();
		p.setGameMode(GameMode.SURVIVAL);
		for(PotionEffect effect : p.getActivePotionEffects()){
			p.removePotionEffect(effect.getType());
		}
		DisableF3.enableF3(p);
		Title.sendTabHF(p, "§6LG UHC\n\n§eTimer : §f00:00\n", "\n§2Plugin par TheDarven\n§adiscord.gg/HZyS5T7");
		/* ItemStack coffre = new ItemStack(Material.CHEST, 1);
		ItemMeta coffreM = coffre.getItemMeta();
		coffreM.setDisplayName("§ePersonnalisation");
		coffre.setItemMeta(coffreM);
		p.getInventory().setItem(4, coffre); */
	}
}
