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

public class PetitAssassin extends RolesBis<Boolean>{
	
	public PetitAssassin() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Petit assassin";
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
			String assassin = null;
			for(PlayerLG p : PlayerLG.getAllPlayersManagers()){
				if(p.getRole() instanceof Assassin) {
					assassin = Bukkit.getOfflinePlayer(p.getUuid()).getName();
				}
			}
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es le Petit Assassin, ton but est de gagner avec l'Assassin. Si tu te trouve à moins de 50 blocs de l'Assassin en journée, tu obtiens un effet de §bForce I§9. A chaque kill, tu obtiens pendant deux minutes §bdeux coeurs d'absorbtion§9. L'Assassin est §b"+assassin+"§9.");
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
		boolean aliveAssassin = false;
		double distance = -1.0;
		for(PlayerLG p : PlayerLG.getAlivePlayersManagers()){
			if(p.getRole() instanceof Assassin) {
				aliveAssassin = true;
				if(p.isOnline() && pl.isOnline()) {
					distance = pl.getPlayer().getLocation().distance(p.getPlayer().getLocation());
				}
			}
		}
		if(LGUHC.cycle.getNow().equals(EnumTime.DAY)) {
			if(aliveAssassin) {
				if(distance != -1.0 && distance <= 50) {
					// DONNE EFFET
					if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && LGUHC.cycle.getAtSecond(LGUHC.timer) == EnumTime.DAY && pl.isOnline() && pl.isAlive()){
						pl.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 0, true, false));
					}
				}
			}else {
				// DONNE EFFET
				if(pl.isOnline() && pl.isAlive() && LGUHC.etat.equals(EnumGame.MIDDLEGAME) && LGUHC.cycle.getAtSecond(LGUHC.timer) == EnumTime.DAY){
					pl.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 0, true, false));
				}
			}	
		}
		super.verifRole(pl);
	}

	@Override
	public void endRole(PlayerLG pl) {	}
}
