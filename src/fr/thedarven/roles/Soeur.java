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

public class Soeur extends RolesBis<Integer>{
	
	public Soeur() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Soeur";
		this.pouvoir = -1200;
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
	}
	
	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es soeur, ton but est de gagner avec le village. Tu connais l'identité de la deuxième soeur. Tu peux envoyer des messages à ta soeur à l'aide de la commande §b/lg chat <message>§9. Cependant, tu dois attendre au moins dix minutes entre chaque message.");
			for(PlayerLG p : PlayerLG.getAllPlayersManagers()) {
				if(!p.equals(pl) && p.getRole() instanceof Soeur) {
					pl.getPlayer().sendMessage("§6[LGUHC] §dLe pseudo de votre soeur est §l"+Bukkit.getOfflinePlayer(p.getUuid()).getName());
					break;
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
	}

	@Override
	public void endRole(PlayerLG pl) {	}
	
	@Override
	public boolean verifCommand(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.STARTGAME) || !pl.isAlive() || !active){
			return false;
		}
		return true;
	}
	
}
