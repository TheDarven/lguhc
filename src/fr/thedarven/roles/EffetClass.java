package fr.thedarven.roles;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.game.enums.EnumTime;

public class EffetClass {
	private EnumTime periode;
	private PotionEffect potion;
	private boolean needActive;
	private boolean needInfecte;
	
	public EffetClass(EnumTime periodeValue, PotionEffect potionValue, boolean needActiveValue, boolean needInfecteValue) {
		this.periode = periodeValue;
		this.potion = potionValue;
		this.needActive = needActiveValue;
		this.needInfecte = needInfecteValue;
		if(potionValue.getType().equals(PotionEffectType.ABSORPTION) || potionValue.getType().equals(PotionEffectType.REGENERATION)) {
			System.out.println("\\033[31mUn role à l'effet Absorption ou Regeneration, il faut modifier la structure du code pour l'adapter.");
		}
	}
	
	//PERIODE
	public EnumTime getPeriode() {
		return this.periode;
	}
	
	// POTION
	public PotionEffect getPotion() {
		return this.potion;
	}
	
	// NEED ACTIVE
	public boolean getNeedActive() {
		return this.needActive;
	}
	
	// NEED INFECTE
	public boolean getNeedInfecte() {
		return this.needInfecte;
	}
}
