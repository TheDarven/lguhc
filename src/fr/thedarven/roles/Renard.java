package fr.thedarven.roles;

import java.util.ArrayList;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class Renard extends RolesBis<Integer>{
	
	public Renard() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Renard";
		this.pouvoir = 3;
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es le Renard, ton but est de gagner avec le village. Tu possèdes la commande §b/lg flairer <player>§9 qui te permet de savoir si un joueur est loups. Cependant, tu ne peux utiliser cette commande que trois fois.");
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
	}

	@Override
	public void endRole(PlayerLG pl) {	}
	
	@Override
	public boolean verifCommand(PlayerLG pl) {
		if(pouvoir == 0 || !pl.isAlive() || LGUHC.etat.equals(EnumGame.STARTGAME) || !active){
			return false;
		}
		return true;
	}
}
