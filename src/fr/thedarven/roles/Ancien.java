
package fr.thedarven.roles;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class Ancien extends RolesBis<Boolean>{

	public Ancien() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Ancien";
		this.pouvoir = true;
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.DAY,new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40000, 0, true, false),false,false));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40000, 0, true, false),false,false));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es l’Ancien, ton but est de gagner avec le village. Tu possèdes comme bonus un effet de §bRésistance I §9permanent ainsi que le pouvoir de te ressusciter une fois. Si un villageois te tue, il perdra tous ses pouvoirs.");
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
	public void playerDeath(final Player p, final UUID pKill) {
		if(pKill != null && !PlayerLG.getPlayerManager(pKill).getRole().getInfecte() && !(PlayerLG.getPlayerManager(pKill).getRole() instanceof Assassin) && !(PlayerLG.getPlayerManager(pKill).getRole() instanceof PetitAssassin) && PlayerLG.getPlayerManager(pKill).getPlayer() != p){
			PlayerLG.getPlayerManager(pKill).getRole().setActive(false);
			Bukkit.getPlayer(pKill).sendMessage("§6[LGUHC]§e Vous venez de tuer l'ancien. Pour vous punir, les dieux ont décidés de vous ôter votre pouvoir.");
		}
		
		if(pouvoir){
			pouvoir = false;
			p.sendMessage("§6[LGUHC]§e Vous venez de mourrir mais les dieux ont décidés de vous ressusciter.");
			p.setHealth(p.getMaxHealth());
			
			// TELEPORT LE JOUEUR
			Random r = new Random();					
			int x = -199 + r.nextInt(298);
			int z = -199 + r.nextInt(298);
			int y = Bukkit.getWorld("lguhc").getHighestBlockYAt(x,z);
			Location loc = new Location(Bukkit.getWorld("lguhc"), x, y+1, z);
			p.teleport(loc);
			
		}else{
			super.playerDeath(p, pKill);
		}
	}
	
}
