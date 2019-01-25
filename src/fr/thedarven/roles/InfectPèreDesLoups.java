package fr.thedarven.roles;

import java.util.ArrayList;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class InfectPèreDesLoups extends RolesBis<Boolean>{

	public InfectPèreDesLoups() {
		this.active = true;
		this.infecte = true;
		this.kit = true;
		this.name = "Infect père des loups";
		this.pouvoir = true;
		this.taupelist = true;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es l’Infect père des loups, ton but est de trahir le village et de gagner avec les autres Loups-garous. Tu possèdes chaque nuit les effets §bForce I§9 et §bNight vision§9. Tu peux aussi ressusciter un joueur afin qu’il devienne Loup-garou. A chaque kill, tu obtiens pendant deux minutes §bdeux coeurs d'absorption§9.");
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
		if(LGUHC.etat.equals(EnumGame.STARTGAME) || !pl.isAlive() || !pouvoir){
			return false;
		}
		return true;
	}
	
}
