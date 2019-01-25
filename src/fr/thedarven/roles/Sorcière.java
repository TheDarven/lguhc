package fr.thedarven.roles;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class Sorcière extends RolesBis<Boolean>{

	public Sorcière() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Sorcière";
		this.pouvoir = true;
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es Sorcière, ton but est de gagner avec le village. Tu peux, une fois dans la partie, ressusciter un joueur. Tu possèdes aussi un kit.");
			super.messageRole(pl);
		}
	}
	
	@Override
	public void startRole(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && pl.isOnline() && kit){	
			messageRole(pl);
			Potion heal = new Potion(PotionType.INSTANT_HEAL, 3);
			heal.setSplash(true);
			Potion regeneration = new Potion(PotionType.REGEN, 1);
			regeneration.setSplash(true);
			Potion damage = new Potion(PotionType.INSTANT_DAMAGE, 3);
			damage.setSplash(true);
					
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), heal.toItemStack(3));
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), regeneration.toItemStack(1));
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), damage.toItemStack(3));
			kit = false;
		}
	}

	public void verifRole(PlayerLG pl) {
		super.verifRole(pl);
	}

	@Override
	public void endRole(PlayerLG pl) {	}
	
	@Override
	public boolean verifCommand(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.STARTGAME) || !pl.isAlive() || !active || !pouvoir){
			return false;
		}
		return true;
	}
}
