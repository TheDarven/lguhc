package fr.thedarven.roles;

import java.util.ArrayList;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class LoupBlanc extends RolesBis<Boolean>{
	
	public LoupBlanc() {
		this.active = true;
		this.infecte = true;
		this.kit = true;
		this.name = "Loup garou blanc";
		this.pouvoir = false;
		this.taupelist = true;
		this.maxhealth = 30.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es Loup-garous blanc, ton but est de trahir à la fois le village et les autres Loups-garous. Tu possèdes chaque nuit les effets §bForce I§9 et §bNight vision§9 ainsi qu’un bonus de §b5 coeurs supplémentaires§9. ");
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
}
