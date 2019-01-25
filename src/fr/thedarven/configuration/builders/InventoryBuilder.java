package fr.thedarven.configuration.builders;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

import fr.thedarven.main.LGUHC;

public abstract class InventoryBuilder implements Listener{
	private String name;
	private ItemStack item;
	private int lines;
	private String description;
	private InventoryGUI parent;
	private int position;
	
	public InventoryBuilder(String pName, String pDescription, int pLines, Material pItem, InventoryGUI pParent, int pPosition) {
		this.name = pName;
		this.description = pDescription;
		this.lines = (pLines < 1 || pLines > 6) ? 1 : pLines;			
		this.parent = pParent;
		this.position = pPosition;
		initItem(pName, pDescription, pItem);
		
		PluginManager pm = LGUHC.getInstance().getServer().getPluginManager();
		pm.registerEvents(this, LGUHC.getInstance());
	}
	
	public InventoryBuilder(String pName, String pDescription, int pLines, Material pItem, InventoryGUI pParent) {
		this.name = pName;
		this.description = pDescription;
		this.lines = pLines;
		this.parent = pParent;
		this.position = 0;
		initItem(pName, pDescription, pItem);
		
		PluginManager pm = LGUHC.getInstance().getServer().getPluginManager();
		pm.registerEvents(this, LGUHC.getInstance());
	}
	
	private void initItem(String pName, String pDescription, Material pItem) {
		ItemStack item = new ItemStack(pItem,1);
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(ChatColor.YELLOW+pName);
		if(pDescription != null) {
			itemM.setLore(LGUHC.toLoreItem(pDescription,"§7", this.getName().length()+15));
		}
		itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(itemM);
		this.item = item;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getLines() {
		return lines;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public InventoryGUI getParent() {
		return parent;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int pPosition) {
		this.position = pPosition;
	}
	
}
