package fr.thedarven.roles;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class Salvateur extends RolesBis<ArrayList<UUID>> {

	public Salvateur() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Salvateur";
		this.pouvoir = new ArrayList<UUID>();
		pouvoir.add(null);
		pouvoir.add(null);
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es le Salvateur, ton but est de gagner avec le village. Tu possèdes un kit dans lequel se trouve deux potions §bd'Instant Heal I§9. Tu peux aussi, à l’aube, donner ta bénédiction à joueur à l’aide de la commande §b/lg proteger <player>§9, ce qui lui donnera les effets de Résistance I et NoFall pendant 20 minutes. Tu ne peux cependant pas protéger le même joueur deux fois d'affilé.");
			super.messageRole(pl);
		}
	}
	
	@Override
	public void startRole(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && pl.isOnline() && kit){	
			messageRole(pl);
			Potion heal = new Potion(PotionType.INSTANT_HEAL, 2);
			heal.setSplash(true);
					
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), heal.toItemStack(3));
			kit = false;
		}
	}

	public void verifRole(PlayerLG pl) {
		super.verifRole(pl);
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && LGUHC.timer%1200 == 0){
			if(pouvoir.get(1) != null) {
				PlayerLG affectPlayer = PlayerLG.getPlayerManager(pouvoir.get(1));
				affectPlayer.getRole().removeEffect(new EffetClass(EnumTime.DAY, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40000, 0), false, false), affectPlayer);
				affectPlayer.getRole().removeEffect(new EffetClass(EnumTime.NIGHT, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40000, 0), false, false), affectPlayer);
			}
			pouvoir.set(0, pouvoir.get(1));
			pouvoir.set(1, null);
			if(pl.isOnline() && pl.isAlive() && active){
				pl.getPlayer().sendMessage("§6[LGUHC]§e Tu disposes de 2 minutes pour protéger un joueur à l'aide de la commande /lg proteger <pseudo>.");
			}
		}
	}

	@Override
	public void endRole(PlayerLG pl) {	}
	
	@Override
	public boolean verifCommand(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.timer%1200 > 120 || !pl.isAlive() || !active || pouvoir.get(1) != null) {
			return false;
		}
		return true;
	}

}
