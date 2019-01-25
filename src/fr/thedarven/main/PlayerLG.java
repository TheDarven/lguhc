package fr.thedarven.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.roles.RolesBis;
import fr.thedarven.roles.Spectateur;
import fr.thedarven.utils.SqlRequest;

public class PlayerLG{
	private static Map<UUID, PlayerLG> playerManagerHashMap = new HashMap<>();
	private UUID uuid;
	private RolesBis<?> role;
	private boolean death;
	private ArrayList<Object> predeath = new ArrayList<Object>();
	private ArrayList<Object> inventory = new ArrayList<Object>();
	private ArrayList<Object> lastPunch = new ArrayList<Object>();
	private boolean couple;
	private boolean compas;
	private int kills;
	private boolean canClick;
	 
	public PlayerLG(UUID playerUuid){
		uuid = playerUuid;
		role = new Spectateur();
		death = false;
		predeath.add(0);
		predeath.add("aucun");
		predeath.add(false);
		inventory.add(Bukkit.getPlayer(playerUuid).getLocation());
		inventory.add(Bukkit.getPlayer(playerUuid).getInventory());
		inventory.add(Bukkit.getPlayer(playerUuid).getTotalExperience());
		couple = false;
		compas = false;
		kills = 0;
		canClick = true;
		
		playerManagerHashMap.put(uuid, this);
	}
	 
	public void setRole(RolesBis<?> role){
		this.role = role;
		if(!this.death && !(this.role instanceof Spectateur) && (LGUHC.etat.equals(EnumGame.MIDDLEGAME) || LGUHC.etat.equals(EnumGame.STARTGAME))){
			SqlRequest.updateLGRole(this.uuid);
		}
	}
	
	public void setDeath(boolean death){
		this.death=death;
	}
	
	public void setPreDeath(int number, Object value){
	 	this.predeath.set(number, value);
	}
	
	public void setInventory(int number, Object value){
	 	this.inventory.set(number, value);
	}
	
	public void setLastPunch(PlayerLG player, int timer) {
		if(lastPunch.size() != 0) {
			lastPunch.clear();
		}
		lastPunch.add(player);
		lastPunch.add(timer);
	}
	
	public void setCouple(boolean couple){
		this.couple = couple;
	}
	
	public void setCompas(boolean compas){
		this.compas = compas;
	}
	
	public void addKill() {
		this.kills++;
	}
	
	public void setCanClick(boolean pCanClick) {
		this.canClick = pCanClick;
	}
	
	
	
	public RolesBis<?> getRole() {
		return role;
	}
	
	public UUID getUuid(){
		return uuid;
	}
	
	public ArrayList<Object> getPreDeath(){
		return predeath;
	}
	
	public ArrayList<Object> getInventory(){
		return inventory;
	}
	
	public PlayerLG getLastPunch() {
		if(lastPunch.size() == 2 && (int) lastPunch.get(1) <= LGUHC.timer+5) {
			return (PlayerLG) lastPunch.get(0);
		}
		return null;
	}
	
	public Player getPlayer(){
		if(isOnline()){
			return Bukkit.getPlayer(uuid);
		}
		return null;
	}
	
	
	public boolean isOnline(){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.getUniqueId().equals(this.uuid)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isAlive(){
		return !death;
	}
	
	public boolean isCouple(){
		return couple;
	}
	
	public boolean hasCompas(){
		return compas;
	}
	
	public boolean hasDeathInfo(){
		if(death && (Boolean) predeath.get(2)){
			return true;
		}
		return false;
	}
	
	public int getKills() {
		return this.kills;
	}
	
	public boolean getCanClick() {
		return this.canClick;
	}
	
	 
	public static PlayerLG getPlayerManager(UUID playerUuid) {
		if (playerManagerHashMap.containsKey(playerUuid)) {
			return playerManagerHashMap.get(playerUuid);
		}
		return new PlayerLG(playerUuid);
	}
	 
	public static List<PlayerLG> getAlivePlayersManagers() {
		List<PlayerLG> list = new ArrayList<PlayerLG>();
		for(PlayerLG pl : playerManagerHashMap.values()){
			if(pl.isAlive() && !(pl.getRole() instanceof Spectateur)){
				list.add(pl);
			}
		}
		return list;
	}
	
	public static List<PlayerLG> getAllPlayersManagers() {
		List<PlayerLG> list = new ArrayList<PlayerLG>(playerManagerHashMap.values());
		return list;
	}
}
