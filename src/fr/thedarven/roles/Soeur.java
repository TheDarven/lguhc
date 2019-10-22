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

public class Soeur extends RolesBis<String>{

	public Soeur() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Soeur";
		this.pouvoir = null;
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es soeur, ton but est de gagner avec le village. Tu connais l'identité de la deuxième soeur. Toutes les dix minutes, vous pouvez vous échanger un message à l'aide de la commande §b/lg chat <message>§9. Les messages n'apparaisent que lorsque vous avez tout les deux envoyés votre message.");
			for(PlayerLG p : PlayerLG.getAllPlayersManagers()) {
				if(!p.equals(pl) && p.getRole() instanceof Soeur) {
					pl.getPlayer().sendMessage("§6[LGUHC] §dLe pseudo de votre soeur est §l"+Bukkit.getOfflinePlayer(p.getUuid()).getName());
				}
			}
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
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && LGUHC.timer%600 == 300) {
			pouvoir = null;
			if(pl.isOnline() && pl.isAlive() && active){
				pl.getPlayer().sendMessage("§6[LGUHC] §eTu as 1 minute pour échanger un message avec ta soeur grâce à la commande /lg chat <message>.");
			}
		}
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && LGUHC.timer%600 == 360) {
			if(pl.isOnline() && pl.getRole() != null && pl.getRole().getPouvoir() != null) {
				for(PlayerLG p : PlayerLG.getAllPlayersManagers()) {
					if(p.getRole() instanceof Soeur) {
						if(!p.equals(pl)) {
							p.getPlayer().sendMessage("§6[LGUHC] §7§o("+Bukkit.getOfflinePlayer(pl.getUuid())+") §r§d"+pl.getRole().getPouvoir());
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public void endRole(PlayerLG pl) {	}
	
	@Override
	public boolean verifCommand(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.timer%600 < 300 || LGUHC.timer%600 > 360 || !pl.isAlive() || !active || pouvoir != null){
			return false;
		}
		return true;
	}
	
}
