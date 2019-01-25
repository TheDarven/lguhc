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

public class MontreurDours extends RolesBis<Boolean>{
	
	public MontreurDours() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Montreur d'Ours";
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
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es le Montreur d'Ours, ton but est de gagner avec le village. Tu te met à grogner chaque matin si un loup se trouve à moins de 50 blocs de toi.");
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
		
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && LGUHC.cycle.getAtSecond(LGUHC.timer-1).equals(EnumTime.NIGHT) && LGUHC.cycle.getNow().equals(EnumTime.DAY)){
			if(pl.isOnline() && pl.isAlive() && active){
				int number = 0;
				for(PlayerLG p : PlayerLG.getAlivePlayersManagers()){
					if(p.isOnline() && p.getPlayer() != pl && p.isAlive()){
						if(p.getPlayer().getLocation().distance(pl.getPlayer().getLocation()) <= 50){
							number++;
						}
					}
				}
				for(int i=0; i<number; i++) {
					Bukkit.broadcastMessage("§6[LGUHC] Agreuuuuuuuh !");
				}
			}
		}
	}

	@Override
	public void endRole(PlayerLG pl) {	}

}
