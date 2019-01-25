package fr.thedarven.roles;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class Assassin extends RolesBis<Boolean>{

	public Assassin() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Assassin";
		this.pouvoir = false;
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.NIGHT,new PotionEffect(PotionEffectType.NIGHT_VISION, 40000, 0, true, false),false,true));
		effect.add(new EffetClass(EnumTime.DAY,new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40000, 0, true, false),false,false));
	}

	@Override
	public void messageRole(PlayerLG pl) {
		if(pl.isOnline()){
			pl.getPlayer().sendMessage("§6[LGUHC] §9Tu es l’Assassin, ton but est de gagner tout seul. Tu possèdes comme bonus un kit et un effet de §bForce I §9seulement la journée. A chaque kill, tu obtiens pendant deux minutes §bdeux coeurs d'absorbtion§9.");
			super.messageRole(pl);
		}
	}
	
	@Override
	public void startRole(PlayerLG pl) {
		if(LGUHC.etat.equals(EnumGame.MIDDLEGAME) && pl.isOnline() && kit){	
			messageRole(pl);
			ItemStack sharpness = new ItemStack(Material.ENCHANTED_BOOK, 1);
			EnchantmentStorageMeta sharpnessM = (EnchantmentStorageMeta) sharpness.getItemMeta();
			sharpnessM.addStoredEnchant(Enchantment.DAMAGE_ALL, 3, true);
			sharpness.setItemMeta(sharpnessM);
			
			ItemStack protection = new ItemStack(Material.ENCHANTED_BOOK, 1);
			EnchantmentStorageMeta protectionM = (EnchantmentStorageMeta) protection.getItemMeta();
			protectionM.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
			protection.setItemMeta(protectionM);
			
			ItemStack power = new ItemStack(Material.ENCHANTED_BOOK, 1);
			EnchantmentStorageMeta powerM = (EnchantmentStorageMeta) power.getItemMeta();
			powerM.addStoredEnchant(Enchantment.ARROW_DAMAGE, 3, true);
			power.setItemMeta(powerM);
			
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), sharpness);
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), protection);
			Bukkit.getWorld("lguhc").dropItemNaturally(pl.getPlayer().getLocation(), power);
			kit = false;
		}
	}

	public void verifRole(PlayerLG pl) {
		super.verifRole(pl);
	}

	@Override
	public void endRole(PlayerLG pl) {	}
}
