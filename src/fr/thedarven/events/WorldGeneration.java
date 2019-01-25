package fr.thedarven.events;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import fr.thedarven.main.LGUHC;

public class WorldGeneration implements Listener {

	public WorldGeneration(LGUHC pl) {
	}
	
	@EventHandler
	public void chunkLoad(ChunkUnloadEvent e) {
		if(e.getChunk().getWorld().getName().equals("lguhc")) {
			Chunk ch = e.getChunk();
			int blockX = ch.getX() << 4;
			int blockZ = ch.getZ() << 4;
			if(ch.isLoaded() && !ch.getWorld().getBlockAt(blockX, 0, blockZ).getType().equals(Material.BARRIER)) {
				ch.getWorld().getBlockAt(blockX, 0, blockZ).setType(Material.BARRIER);
				int sugar = (int) (Math.random()*6);
				if(sugar == 5) {
					int i = 0, j = 0;
					while(i<16 && sugar == 5) {
						while(j<16 && sugar == 5) {;
							Location loc = ch.getWorld().getHighestBlockAt(blockX+i, blockZ+j).getLocation().add(0, -1, 0);
							
							if(loc.getBlock().getType().equals(Material.SAND) || loc.getBlock().getType().equals(Material.GRASS) || loc.getBlock().getType().equals(Material.DIRT)) {
								for(int k=-1; k<2; k++) {
									for(int l=-1; l<2; l++) {
										if((k == 0 && l != 0) || (k != 0 && l == 0)) {
											Location loc2 = new Location(Bukkit.getWorld("lguhc"), loc.getX()+k, loc.getY(), loc.getZ()+l);
											if(ch.getWorld().getBlockAt(loc2).getType().equals(Material.WATER) || ch.getWorld().getBlockAt(loc2).getType().equals(Material.STATIONARY_WATER) || ch.getWorld().getBlockAt(loc2).getType().equals(Material.ICE)) {
												sugar = 0;
											}
										}
									}
								}
								if(sugar == 0) {
									int taille = (int) (Math.random()*3)+1;
									for(int k = 0; k<taille; k++) {
										loc.add(0, 1, 0).getBlock().setType(Material.SUGAR_CANE_BLOCK);
									}
								}
							}
							j++;
						}
						j = 0;
						i++;
					}
				}
				int cow = (int) (Math.random()*31);
				if(cow == 30) {
					int x = (int) (Math.random()*16);
					int z = (int) (Math.random()*16);

					int y = Bukkit.getWorld("lguhc").getHighestBlockAt(blockX+x, blockZ+z).getY()-1;
					while((Bukkit.getWorld("lguhc").getBlockAt(blockX+x, y, blockZ+z).getType().equals(Material.LEAVES) || Bukkit.getWorld("lguhc").getBlockAt(blockX+x, y, blockZ+z).getType().equals(Material.LEAVES_2) || Bukkit.getWorld("lguhc").getBlockAt(blockX+x, y, blockZ+z).getType().equals(Material.AIR) || Bukkit.getWorld("lguhc").getBlockAt(blockX+x, y, blockZ+z).getType().equals(Material.LOG_2) || Bukkit.getWorld("lguhc").getBlockAt(blockX+x, y, blockZ+z).getType().equals(Material.LOG) || !Bukkit.getWorld("lguhc").getBlockAt(blockX+x, y+1, blockZ+z).getType().equals(Material.AIR)) && y > 50) {
						y--;
					}
					Location loc = new Location(Bukkit.getWorld("lguhc"), blockX+x, y,blockZ+z);
					if(y > 54 && loc.getBlock().getType().equals(Material.GRASS)) {
						loc.add(0, 1, 0);
						int nombre = (int) (Math.random()*3)+1;
						for(int i=0; i<nombre; i++) {
							Bukkit.getWorld("lguhc").spawnEntity(loc, EntityType.COW);
							loc.add(1, 0, 1);
						}	
					}	
				}
			}
		}
	}
}
