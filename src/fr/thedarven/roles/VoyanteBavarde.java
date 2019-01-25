package fr.thedarven.roles;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class VoyanteBavarde extends RolesBis<Boolean>{

	public VoyanteBavarde() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Voyante bavarde";
		this.pouvoir = true;
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),true,false));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es la Voyante bavarde, ton but est de gagner avec le village. Tu possèdes un kit ainsi que l’effet §bNight vision chaque nuit§9. Tu peux aussi, à l’aube, espionner le rôle d’un joueur à l’aide de la commande §b/lg look <player>§9. Cependant, tout le village a connaisance du rôle que tu as observé. ");
			super.messageRole(pl);
		}
	}
	
	@Override
	public void startRole(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && pl.isOnline() && kit){	
			messageRole(pl);
			ItemStack bookshelf = new ItemStack(Material.BOOKSHELF, 4);
			ItemStack obsidian = new ItemStack(Material.OBSIDIAN, 4);
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), bookshelf);
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), obsidian);
			kit = false;
		}
	}

	public void verifRole(PlayerLG pl) {
		super.verifRole(pl);
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && LGUHC.timer%1200 == 0){
			if(pl.isOnline() && pl.isAlive() && active){
				pouvoir = true;
				pl.getPlayer().sendMessage("§6[LGUHC]§e Tu as 5 minutes pour espionner quelqu'un à l'aide de la commande /lg look <pseudo>.");
			}
		}
	}

	@Override
	public void endRole(PlayerLG pl) {	}
	
	@Override
	public boolean verifCommand(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.timer%1200 > 300 || !pl.isAlive() || !pouvoir || !active){
			return false;
		}
		return true;
	}
}
