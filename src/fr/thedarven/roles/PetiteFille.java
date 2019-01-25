package fr.thedarven.roles;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class PetiteFille extends RolesBis<Boolean>{
	
	public PetiteFille() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Petite fille";
		this.pouvoir = false;
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INVISIBILITY, 40000, 0, true, false),true,false));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.WEAKNESS, 40000, 0, true, false),true,false));
		effect.add(new EffetClass(EnumTime.DAY,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),true,false));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),true,false));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es la Petite fille, ton but est de gagner avec le village. Tu possèdes comme bonus un kit, l’effet §bNight vision§9 ainsi que les effets §bInvisibilité§9 et §bFaiblesse I§9 durant la nuit. A l’aube et au début de la nuit, un message t’avertiras des personnes se trouvant dans les 100 blocs autour de toi.");
			super.messageRole(pl);
		}
	}
	
	@Override
	public void startRole(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && pl.isOnline() && kit){	
			messageRole(pl);
			ItemStack tnt = new ItemStack(Material.TNT, 5);
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), tnt);
			kit = false;
		}
	}

	public void verifRole(PlayerLG pl) {
		super.verifRole(pl);
		
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && (LGUHC.timer%1200 == 0 || LGUHC.timer%1200 == 600)){
			if(pl.isOnline() && pl.isAlive() && active){
				pl.getPlayer().sendMessage("§6[LGUHC]§e Les joueurs à moins de 100 blocs de vous : ");
				String message = "§e";
				for(PlayerLG p : PlayerLG.getAlivePlayersManagers()){
					if(p.isOnline() && p.getPlayer() != pl && p.isAlive()){
						if(p.getPlayer().getLocation().distance(pl.getPlayer().getLocation()) <= 100){
							message += p.getPlayer().getName()+"   ";
						}
					}
				}
				pl.getPlayer().sendMessage(message);
			}
		}
	}

	@Override
	public void endRole(PlayerLG pl) {	}

}
