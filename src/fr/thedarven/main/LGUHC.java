package fr.thedarven.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.thedarven.confiavant.Cycle;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.CommandComplet;
import fr.thedarven.events.Commands;
import fr.thedarven.events.EventsManager;
import fr.thedarven.events.Login;
import fr.thedarven.game.Votes;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.roles.RolesBis;
import fr.thedarven.utils.BiomeEdit;
import fr.thedarven.utils.Jump;
import fr.thedarven.utils.SqlConnection;
import fr.thedarven.utils.SqlRequest;

public class LGUHC extends JavaPlugin {

	public static int timer = 0;
	public static EnumGame etat = EnumGame.WAIT;
	public SqlConnection sql;
	
	public static boolean developpement = false;
	public static boolean sqlConnect = false;
	public static String succes_password;
	
	// public static ArrayList<UUID> playerList = new ArrayList<UUID>();
	public static ArrayList<RolesBis<?>> composition = new ArrayList<RolesBis<?>>();
	public static Votes votes = new Votes();
	public static Jump jump = new Jump();
	public static Cycle cycle = new Cycle();
	public static InventoryRegister configuration;
	/*
	 * Trouver le System.out
	 *lg scenarios
	 */
	
	public static ScoreboardManager manager;
    public static Scoreboard board;
    
    public static LGUHC instance;
	public static LGUHC getInstance(){
		return instance;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable(){
		instance = this;
		
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		
		this.saveDefaultConfig();
		
		String host = this.getConfig().getString("bd.host-address");
		String database = this.getConfig().getString("bd.database-name");
		String user = this.getConfig().getString("bd.user");
		String password = this.getConfig().getString("bd.password");
		host = host == null ? "" : host;
		database = database == null ? "" : database;
		user = user == null ? "" : user;
		password = password == null ? "" : password;
		
		sql = new SqlConnection("jdbc:mysql://",host,database,user,password);
        try {
			sql.connection();
			sqlConnect = true;
		} catch (SQLException e) {
			
		}
		
		if(!sqlConnect) {
			System.out.println("[ERREUR] La connexion a la base de donnee a echoue !");
		}
        
		BiomeEdit.changeBiome("FOREST");
        WorldCreator c = new WorldCreator("lguhc");
        c.environment(Environment.NORMAL);
        c.type(WorldType.NORMAL);
        Bukkit.createWorld(c);
		
		EventsManager.registerEvents(this);
		getCommand("lg").setExecutor(new Commands());
		this.getCommand("lg").setTabCompleter(new CommandComplet());
		
		for(Player p : Bukkit.getOnlinePlayers()){
			SqlRequest.updatePlayerLast(p);
			PlayerLG.getPlayerManager(p.getUniqueId());
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	        Login.loginWaitAction(p);
		}
				
		Location loc = new Location(Bukkit.getWorld("lguhc"), 0, 205, 0);
		for(Entity e : loc.getWorld().getEntities()){
			if(loc.distance(e.getLocation()) <= 100 && e instanceof ArmorStand){
				e.remove();
			}
		}
		
		createSpawn();
		
		ItemStack GoldenHead = new ItemStack(Material.GOLDEN_APPLE, 1);
		ItemMeta GoldenHeadM = GoldenHead.getItemMeta();
		GoldenHeadM.addEnchant(Enchantment.DURABILITY, 1, false);
		GoldenHeadM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		GoldenHeadM.setDisplayName(ChatColor.GOLD+"Golden Head");
		GoldenHead.setItemMeta(GoldenHeadM); 
		
		ShapedRecipe recette = new ShapedRecipe(GoldenHead);
		recette.shape("OOO", "OTO", "OOO");
		recette.setIngredient('O', Material.GOLD_INGOT);
		recette.setIngredient('T', Material.SKULL_ITEM, (short) 3);
		getServer().addRecipe(recette);
		
		configuration = new InventoryRegister();
		
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	    for(int x=0; x<15;x++) {
	       int i = (int)Math.floor(Math.random() * 62);
	       succes_password += chars.charAt(i);
	    }
	}
	
	@Override
	public void onDisable(){
		for(int i = 0; i<jump.allBlock.size(); i++){
			jump.allBlock.get(i).getLocation().getBlock().setType(Material.AIR);
		}
		for(Player p: Bukkit.getOnlinePlayers()){
			SqlRequest.updatePlayerTimePlay(p);
		}
		if(SqlRequest.id_partie != 0) {
			SqlRequest.updateGameDuree();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void createSpawn(){
		Bukkit.getWorld("lguhc").setGameRuleValue("doDaylightCycle", "false");
		Bukkit.getWorld("lguhc").setGameRuleValue("naturalRegeneration", "false");
		Bukkit.getWorld("lguhc").setDifficulty(Difficulty.HARD);
		Bukkit.getWorld("lguhc").setTime(6000);
		World world = Bukkit.getWorld("lguhc");
		WorldBorder border = world.getWorldBorder();
		border.setCenter(0.0, 0.0);
		border.setSize(1500.0,0);
		border.setWarningDistance(50);
		border.setWarningTime(5);
		border.setDamageAmount(1.0);
		
		for(int i = -15; i<16; i++){
			for(int j = -15; j<16; j++){
				Random r = new Random();
				int random = 0 + r.nextInt(15);
				Bukkit.getWorld("lguhc").getBlockAt(i, 200, j).setType(Material.STAINED_GLASS);
				Bukkit.getWorld("lguhc").getBlockAt(i, 200, j).setData((byte) random);
				if(i == -15 || i == 15 || j == -15 || j == 15){
					for(int k=201; k<204; k++){
						Block lobby_block_wall = getServer().getWorld("lguhc").getBlockAt(i, k, j);
						lobby_block_wall.setType(Material.STAINED_GLASS_PANE);
						lobby_block_wall.setData((byte)0);
					}
				}
			}
		}
		
		if(LGUHC.sqlConnect) {
			try {
	            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM players ORDER BY kills DESC LIMIT 10");
	            ResultSet resultat = q.executeQuery();
	            
	            Location loc = new Location(Bukkit.getWorld("lguhc"), 14, 204, 0);
	    		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
	    		as.setCustomName("§6Classement des kills");
	    		as.setCustomNameVisible(true);
	    		as.setGravity(false);
	    		as.setVisible(false);
	    		loc.setY(loc.getY()-0.5);
	            int rang = 1;
	    		
	            while(resultat.next()){
	            	ArmorStand as1 = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
	    			as1.setCustomName("§e"+rang+". §f"+resultat.getString("name")+" §e"+resultat.getInt("kills"));
	    			as1.setCustomNameVisible(true);
	    			as1.setGravity(false);
	    			as1.setVisible(false);
	    			loc.setY(loc.getY()-0.4);
	    			rang++;
	            }
	            q.close();
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
			
			try {
	            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM players ORDER BY win DESC LIMIT 10");
	            ResultSet resultat = q.executeQuery();
	            
	            Location loc = new Location(Bukkit.getWorld("lguhc"), -14, 204, 0);
	    		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
	    		as.setCustomName("§6Classement des victoires");
	    		as.setCustomNameVisible(true);
	    		as.setGravity(false);
	    		as.setVisible(false);
	    		loc.setY(loc.getY()-0.5);
	            int rang = 1;
	    		
	            while(resultat.next()){
	            	ArmorStand as1 = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
	    			as1.setCustomName("§e"+rang+". §f"+resultat.getString("name")+" §e"+resultat.getInt("win"));
	    			as1.setCustomNameVisible(true);
	    			as1.setGravity(false);
	    			as1.setVisible(false);
	    			loc.setY(loc.getY()-0.4);
	    			rang++;
	            }
	            q.close();
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }	
		}
		
		
		Location loc = new Location(Bukkit.getWorld("lguhc"), 0, 201, 15);
		loc.setYaw(180);
		
		ItemStack tete = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta teteM = (SkullMeta) tete.getItemMeta();
		teteM.setOwner("TheDarven");
		tete.setItemMeta(teteM);
		
		/* ArmorStand as1 = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		as1.setCustomName("§e10/10");
		as1.setCustomNameVisible(true);
		as1.setGravity(false);
		as1.setVisible(false);
		as1.setHelmet(tete);
		as1.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE, 1, (byte) 1));
		as1.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS, 1, (byte) 1));
		as1.setBoots(new ItemStack(Material.LEATHER_BOOTS, 1, (byte) 1)); */
		
		jump.setBlock();
	}
	
	public static ArrayList<String> toLoreItem(String pDescription, String pColor, int pSize){
		pSize = pSize < 25 ? 25 : pSize;
		List<String> items = Arrays.asList(pDescription.split(" "));
		ArrayList<String> list = new ArrayList<String>();
		String ligne = pColor+items.get(0);
		for(int i=1; i<items.size(); i++) {
			if((ligne+" "+items.get(i)).length() <= pSize){
				ligne += " "+items.get(i);
				if(i == items.size()-1) {
					list.add(ligne);
				}
			}else {
				list.add(ligne);
				ligne = pColor+items.get(i);
				if(i == items.size()-1) {
					list.add(ligne);
				}
			}	
		}
		return list;		
	}
}
