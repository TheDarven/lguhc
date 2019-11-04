package fr.thedarven.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class EnfantSauvage extends RolesBis<UUID>{
	
	public EnfantSauvage() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Enfant sauvage";
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
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es l’Enfant sauvage, ton but est de gagner avec le village. En début de partie, tu disposes de 5 minutes pour choisir un modèle à l’aide la commande §b/lg modele <pseudo>§9. Si ce dernier meurt, tu deviendras un Loup-garous.");
			if(!infecte && pouvoir != null)
				pl.getPlayer().sendMessage("§9Ton modèle est "+Bukkit.getOfflinePlayer(pouvoir).getName()+".");
			super.messageRole(pl);
		}
	}
	
	@Override
	public void startRole(PlayerLG pl) {
		if(LGUHC.timer == InventoryRegister.annonceroles.getValue()*60){
			messageRole(pl);
			pl.getPlayer().sendMessage("§6[LGUHC]§e Tu as 5 minutes pour choisir ton modèle à l'aide de la commande /lg modele <pseudo>.");
		}
	}

	public void verifRole(PlayerLG pl) {
		super.verifRole(pl);
	}

	@Override
	public void endRole(PlayerLG pl) {
		// CHOISIT UN MODELE (SECU)
		if(LGUHC.timer == InventoryRegister.annonceroles.getValue()*60+300 && pouvoir == null){		
			List<PlayerLG> playerList = new ArrayList<PlayerLG>();
			for(PlayerLG p : PlayerLG.getAlivePlayersManagers()){
				if(!(p.getRole() instanceof EnfantSauvage)){
					playerList.add(p);
				}
			}
			
			Collections.shuffle(playerList);
			pouvoir = playerList.get(0).getUuid();
			if(pl.isOnline() && pl.isAlive()){
				pl.getPlayer().sendMessage("§6[LGUHC]§a Votre modèle ("+Bukkit.getOfflinePlayer(playerList.get(0).getUuid()).getName()+") a bien été enregistré. S'il vient à mourir, vous rejoindrez le camp des Loups-Garous.");
			}
		}
	}
	
	@Override
	public boolean verifCommand(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.STARTGAME) || LGUHC.timer > InventoryRegister.annonceroles.getValue()*60+300 || !pl.isAlive() || !active){
			return false;
		}
		return true;
	}
}
