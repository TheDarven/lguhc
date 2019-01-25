package fr.thedarven.roles;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class Chaman extends RolesBis<Boolean>{

	public Chaman() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Chaman";
		this.pouvoir = false;
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es le Chaman, ton but est de gagner avec le village. A l’aube, tu pourras écouter les morts pendant une minute jusqu’à la fin de la partie.");
			super.messageRole(pl);
		}
	}
	
	@Override
	public void startRole(PlayerLG pl) {
		if(LGUHC.timer == InventoryRegister.annonceroles.getValue()*60){
			messageRole(pl);
		}
	}

	public void verifRole(PlayerLG pl) {
		super.verifRole(pl);
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && LGUHC.timer%1200 == 0){
			if(pl.isAlive()){
				Bukkit.broadcastMessage("§6[LGUHC]§e Pendant une minute, le Chaman peut écouter les morts.");
			}
		}
	}

	@Override
	public void endRole(PlayerLG pl) {	}
	
	@Override
	public boolean verifCommand(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.timer%1200 > 60 || !pl.isAlive() || !active || !pl.isOnline()){
			return false;
		}
		return true;
	}
}
