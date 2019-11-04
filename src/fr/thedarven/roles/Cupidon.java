package fr.thedarven.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.enums.EnumCupidonState;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;
import fr.thedarven.utils.SqlRequest;

public class Cupidon extends RolesBis<EnumCupidonState>{
	
	public Cupidon() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Cupidon";
		this.pouvoir = EnumCupidonState.SELECT;
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es le Cupidon, ton but est de gagner avec le village ou avec le couple que tu vas former. Pour cela, tu disposes de 5 minutes pour utiliser la commande §b/lg love <pseudo1> <pseudo2>§9.");
			super.messageRole(pl);
		}
	}
	
	@Override
	public void startRole(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && pl.isOnline() && kit){	
			messageRole(pl);
			ItemStack arrow = new ItemStack(Material.ARROW, 32);
			ItemStack enchant = new ItemStack(Material.ENCHANTED_BOOK, 1);
			EnchantmentStorageMeta enchantM = (EnchantmentStorageMeta) enchant.getItemMeta();
			enchantM.addStoredEnchant(Enchantment.ARROW_DAMAGE, 2, true);
			enchantM.addStoredEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
			enchant.setItemMeta(enchantM);
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), arrow);
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), enchant);
			kit = false;
		}
		if(pl.isOnline() && LGUHC.timer == InventoryRegister.annonceroles.getValue()*60){
			pl.getPlayer().sendMessage("§6[LGUHC]§e Tu as 5 minutes pour choisir deux joueurs à mettre en couple à l'aide de la commande /lg love <pseudo1> <pseudo2>. Tu possède un aussi kit.");
		}
	}

	public void verifRole(PlayerLG pl) {
		super.verifRole(pl);
		actionCouple();
	}

	@Override
	public void endRole(PlayerLG pl) {	
		// CHOISIT UN COUPLE (SECU)
		if(LGUHC.timer == InventoryRegister.annonceroles.getValue()*60+300 && pouvoir == EnumCupidonState.SELECT){
			pouvoir = EnumCupidonState.MESSAGE;
			List<PlayerLG> list = PlayerLG.getAlivePlayersManagers();
			
			List<PlayerLG> playerList = new ArrayList<PlayerLG>();
			for(PlayerLG p : list){
				if(!(p.getRole() instanceof Cupidon)){
					playerList.add(p);
				}
			}
				
			Collections.shuffle(playerList);
			PlayerLG.getPlayerManager(playerList.get(0).getUuid()).setCouple(true);
			PlayerLG.getPlayerManager(playerList.get(1).getUuid()).setCouple(true);
			messageCouple(pl);
		}
	}
	
	@Override
	public boolean verifCommand(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.timer >= InventoryRegister.annonceroles.getValue()*60+300 || pouvoir != EnumCupidonState.SELECT || !pl.isAlive() || !active){
			return false;
		}
		return true;
	}
	
	public void messageCouple(PlayerLG pl) {
		ArrayList<String> pseudoAmoureux = new ArrayList<String>();
		
		// MESSAGE AU COUPLE
		for(PlayerLG p : PlayerLG.getAlivePlayersManagers()){
			if(p.isCouple()){
				SqlRequest.updateLGCouple(p.getUuid());
				pseudoAmoureux.add(Bukkit.getOfflinePlayer(p.getUuid()).getName());
			}
		}
		pl.getPlayer().sendMessage("§6[LGUHC]§a Votre couple a été choisit avec succès : §2"+pseudoAmoureux.get(0)+"§a et §2"+pseudoAmoureux.get(1)+"§a.");
		actionCouple();
	}
	
	public void actionCouple() {
		// MESSAGE AU COUPLE
		if(pouvoir == EnumCupidonState.MESSAGE && LGUHC.timer >= InventoryRegister.annonceroles.getValue()*60){
			int nbCouple = 0;
			for(PlayerLG p : PlayerLG.getAlivePlayersManagers()){
				if(!p.hasCompas() && p.isOnline() && p.isCouple()){
					p.setCompas(true);
					String amoureux = null;
					for(int i=0; i<PlayerLG.getAllPlayersManagers().size(); i++){
						if(!PlayerLG.getAllPlayersManagers().get(i).equals(p) && PlayerLG.getAllPlayersManagers().get(i).isCouple()){
							amoureux = Bukkit.getOfflinePlayer(PlayerLG.getAllPlayersManagers().get(i).getUuid()).getName();
							if(PlayerLG.getAllPlayersManagers().get(i).isOnline()){
								p.getPlayer().setCompassTarget(PlayerLG.getAllPlayersManagers().get(i).getPlayer().getLocation());
							}else{
								p.getPlayer().setCompassTarget((Location) PlayerLG.getAllPlayersManagers().get(i).getInventory().get(0));
							}
						}
					}
					p.getPlayer().sendMessage("§6[LGUHC] §c❤§e Vous tombez amoureux de "+amoureux+". Vous pouvez gagner avec Cupidon et/ou le village et si l'un de vous vient à mourrir, l'autre ne supportera pas la douleur et se suicidera.");
					ItemStack compass = new ItemStack(Material.COMPASS, 1);
					p.getPlayer().getWorld().dropItem(p.getPlayer().getLocation(), compass);
				}
				if(p.hasCompas())
					nbCouple++;
				if(nbCouple == 2) {
					pouvoir = EnumCupidonState.END;
					break;
				}
			}
		}
	}
}
