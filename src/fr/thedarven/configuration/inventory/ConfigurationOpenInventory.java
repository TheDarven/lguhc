package fr.thedarven.configuration.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.game.enums.EnumCompo;
import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;

public class ConfigurationOpenInventory {
	
	public static void openCycleInventory(Player p, int page) {
		Inventory inv = Bukkit.createInventory(null, 36, "Menu > Cycle > Jour "+page);
		for(int i=0; i<20; i++){
			EnumTime minute = LGUHC.cycle.getAtMinute((page-1)*20+i+1);
			if(minute.equals(EnumTime.DAY)) {
				ItemStack dayBlock = new ItemStack(Material.WOOL, 1, (short) 0);
				ItemMeta dayBlockM = dayBlock.getItemMeta();
				dayBlockM.setDisplayName(ChatColor.AQUA+"Minute "+i+"-"+(i+1));
				dayBlock.setItemMeta(dayBlockM);
				inv.setItem(i, dayBlock);
			}else {
				ItemStack nightBlock = new ItemStack(Material.WOOL, 1, (short) 15);
				ItemMeta nightBlockM = nightBlock.getItemMeta();
				nightBlockM.setDisplayName(ChatColor.AQUA+"Minute "+i+"-"+(i+1));
				nightBlock.setItemMeta(nightBlockM);
				inv.setItem(i, nightBlock);
			}
		}
		
		if(page != 1) {
			ItemStack pageback = new ItemStack(Material.ARROW, 1);
			ItemMeta pagebackM = pageback.getItemMeta();
			pagebackM.setDisplayName(ChatColor.GREEN+"Jour "+(page-1));
			pageback.setItemMeta(pagebackM);
			inv.setItem(30, pageback);
		}
		
		ItemStack clone = new ItemStack(Material.SIGN, 1);
		ItemMeta cloneM = clone.getItemMeta();
		cloneM.setDisplayName(ChatColor.GREEN+"Cloner sur les prochains jours");
		clone.setItemMeta(cloneM);
		inv.setItem(31, clone);
		
		ItemStack redstone = new ItemStack(Material.REDSTONE, 1);
		ItemMeta redstoneM = redstone.getItemMeta();
		redstoneM.setDisplayName(ChatColor.RED+"Retour");
		redstone.setItemMeta(redstoneM);
		inv.setItem(35, redstone);
		
		if(page != 50) {
			ItemStack pagefront = new ItemStack(Material.ARROW, 1);
			ItemMeta pagefrontM = pagefront.getItemMeta();
			pagefrontM.setDisplayName(ChatColor.GREEN+"Jour "+(page+1));
			pagefront.setItemMeta(pagefrontM);
			inv.setItem(32, pagefront);
		}
		
		p.openInventory(inv);
	}
	
	public static void openCompoInventory(Player p){
		Inventory inv = Bukkit.createInventory(null, 54, "Menu > Composition");
		List<EnumCompo> yourEnums = Arrays.asList(EnumCompo.values());
		for(int i = 0; i < yourEnums.size(); i++){
			int number = 0;
			int slot = yourEnums.get(i).getPosition();
			if(LGUHC.composition != null){
				for(int j = 0; j < LGUHC.composition.size(); j++){
					if(LGUHC.composition.get(j).getName().equals(yourEnums.get(i).getRoleName())){
						number++;
					}
				}
			}
			if(inv.getItem(slot) != null) {
				slot = inv.firstEmpty();
				System.out.println("\033[31mLe role "+yourEnums.get(i).getRoleName()+" est dans un slot deja utilise dans la configuration.");
			}
			List<String> itemLore = new ArrayList<String>();
			itemLore.add("");
			itemLore.add(ChatColor.GREEN+"► Clique droit pour ajouter");
			itemLore.add(ChatColor.RED+"► Clique gauche pour supprimer");
			if(number > 0){
				ItemStack configItem = new ItemStack(Material.STAINED_CLAY, number, (short) 13);
				ItemMeta configItemM = configItem.getItemMeta();
				configItemM.setDisplayName("§b"+yourEnums.get(i).getRoleName());
				configItemM.setLore(itemLore);
				configItem.setItemMeta(configItemM);
				inv.setItem(slot, configItem);
			}else{
				ItemStack configItem = new ItemStack(Material.STAINED_CLAY, 0, (short) 14);
				ItemMeta configItemM = configItem.getItemMeta();
				configItemM.setDisplayName("§b"+yourEnums.get(i).getRoleName());
				configItemM.setLore(itemLore);
				configItem.setItemMeta(configItemM);
				inv.setItem(slot, configItem);	
			}
			
		}
		
		
		
		ItemStack redstone = new ItemStack(Material.REDSTONE, 1);
		ItemMeta redstoneM = redstone.getItemMeta();
		redstoneM.setDisplayName(ChatColor.RED+"Retour");
		redstone.setItemMeta(redstoneM);
		inv.setItem(53, redstone);
		p.openInventory(inv);
	}
}
