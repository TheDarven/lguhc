package fr.thedarven.configuration.builders;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.thedarven.game.enums.EnumGame;
import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;

public class InventoryGUI extends InventoryBuilder{
	
	protected Inventory inventory;
	protected ArrayList<InventoryGUI> childs = new ArrayList<>();
	private static ArrayList<InventoryGUI> elements = new ArrayList<>();
	
	public InventoryGUI(String pName, String pDescription, int pLines, Material pItem, InventoryGUI pParent, int pPosition) {
		super(pName, pDescription, pLines, pItem, pParent, pPosition);
		initInventory();
		elements.add(this);
	}
	
	public InventoryGUI(String pName, String pDescription, int pLines, Material pItem, InventoryGUI pParent) {
		super(pName, pDescription, pLines, pItem, pParent);
		initInventory();
		elements.add(this);
	}
	
	
	private void initInventory() {
		Inventory inv = Bukkit.createInventory(null, this.getLines()*9, this.getName());
		
		if(this.getParent() != null) {
			ItemStack redstone = new ItemStack(Material.REDSTONE, 1);
			ItemMeta redstoneM = redstone.getItemMeta();
			redstoneM.setDisplayName(ChatColor.RED+"Retour");
			redstone.setItemMeta(redstoneM);
			inv.setItem(this.getLines()*9-1, redstone);
			
			this.getParent().addItem(this);
		}
		
		this.inventory = inv;
	}
	
	public void addItem(InventoryGUI pInventoryGUI) {
		boolean setItem = false;
		if(this.inventory.getItem(pInventoryGUI.getPosition()) != null || this.inventory.getSize() <= pInventoryGUI.getPosition()) {
			int i = 0;
			boolean boucle = true;
			while(boucle && i < this.inventory.getSize()) {
				if(this.inventory.getItem(i) == null){
					boucle = false;
					pInventoryGUI.setPosition(i);
					childs.add(pInventoryGUI);
					setItem = true;
				}
				i++;
			}
			if(boucle) {
				System.out.println("\\033[31mErreur de positionnement de l'item "+pInventoryGUI.getName());
			}
		}else {
			setItem = true;
		}
		
		if(setItem) {
			this.inventory.setItem(pInventoryGUI.getPosition(), pInventoryGUI.getItem());
			childs.add(pInventoryGUI);
		}
	}
	
	public void modifyItem(int pHashCode, ItemStack pNewItem) {
		for(int i=0; i<inventory.getSize(); i++) {
			if(inventory.getItem(i) != null && inventory.getItem(i).hashCode() == pHashCode) {
				inventory.setItem(i, pNewItem);
				return;
			}
		}
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§cRetour")){
				p.openInventory(this.getParent().getInventory());
				return;
			}
			
			if(!e.getCurrentItem().getType().equals(Material.AIR)) {
				for(InventoryGUI inventoryGUI : childs) {
					if(inventoryGUI.getItem().equals(e.getCurrentItem()) && inventoryGUI != InventoryRegister.cycle && inventoryGUI != InventoryRegister.composition) {
						if((inventoryGUI instanceof OptionBoolean || inventoryGUI instanceof OptionNumeric) && (!p.isOp() || !LGUHC.etat.equals(EnumGame.WAIT))) {
							return;
						}
						p.openInventory(inventoryGUI.getInventory());
						return;
					}
				}
			}
		}
	}
	
	public void delayClick(final PlayerLG pl) {
		pl.setCanClick(false);
		new BukkitRunnable(){
			
			@Override
			public void run() {
				pl.setCanClick(true);
				this.cancel();
			}
			
		}.runTaskTimer(LGUHC.instance,3,20);
	}
	
	public static ArrayList<InventoryGUI> getElements(){
		return elements;
	}
	
}
