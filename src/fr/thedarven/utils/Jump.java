package fr.thedarven.utils;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class Jump{
	private Point3D A;
	private Point3D B;
	private Point3D C;
	public ArrayList<Point3D> allBlock = new ArrayList<Point3D>();
	
	public Jump(){
		A = new Point3D(3,201,0);
		B = new Point3D(3,202,3);
		C = new Point3D(6,203,4);
		
		allBlock.add(A);
		allBlock.add(B);
		allBlock.add(C);
	}
	
	@SuppressWarnings("deprecation")
	public void addBlock(Location loc){
		if(A.getLocation().equals(loc) || B.getLocation().equals(loc) || C.getLocation().equals(loc)){
			Random r = new Random();
			int x = 0;
			int y = 0;
			int z = 0;
	
			int nombre = 6;
			int intervalle = 7;
			if(loc.getBlockY() < 252){
				nombre = r.nextInt(6);
				intervalle = 5;
					
				x = C.getX()+(-3 + r.nextInt(intervalle));
				y = C.getY()+1-nombre/5;
				z = C.getZ()+(-3 + r.nextInt(intervalle));
					
				while(!correctCoord(x,y,z)){
					x = C.getX()+(-3 + r.nextInt(intervalle));
					z = C.getZ()+(-3 + r.nextInt(intervalle));	
				}
			}else{
				x = C.getX()+(-3 + r.nextInt(intervalle));
				y = 202;
				z = C.getZ()+(-3 + r.nextInt(intervalle));
					
				while(!correctCoord(x,y,z)){
					x = C.getX()+(-3 + r.nextInt(intervalle));
					z = C.getZ()+(-3 + r.nextInt(intervalle));	
				}
			}
						
			Point3D point = new Point3D(x, y, z);
			allBlock.add(point);	
			
			if(loc.equals(A.getLocation())){
				A = B;
				B = C;
				C = point;	
				
				Bukkit.getWorld("lguhc").getBlockAt(C.getLocation()).setType(Material.STAINED_GLASS);
				Bukkit.getWorld("lguhc").getBlockAt(C.getLocation()).setData((byte)0);
			}else if(loc.equals(B.getLocation()) || loc.equals(C.getLocation())){
				A = B;
				B = C;
				C = point;
				
				Bukkit.getWorld("lguhc").getBlockAt(C.getLocation()).setType(Material.STAINED_GLASS);
				Bukkit.getWorld("lguhc").getBlockAt(C.getLocation()).setData((byte)0);
				
				addBlock(loc);
			}	
		}
	}

	@SuppressWarnings("deprecation")
	public void setBlock(){
		Bukkit.getWorld("lguhc").getBlockAt(A.getLocation()).setType(Material.STAINED_GLASS);
		Bukkit.getWorld("lguhc").getBlockAt(A.getLocation()).setData((byte)0);
		Bukkit.getWorld("lguhc").getBlockAt(B.getLocation()).setType(Material.STAINED_GLASS);
		Bukkit.getWorld("lguhc").getBlockAt(B.getLocation()).setData((byte)0);
		Bukkit.getWorld("lguhc").getBlockAt(C.getLocation()).setType(Material.STAINED_GLASS);
		Bukkit.getWorld("lguhc").getBlockAt(C.getLocation()).setData((byte)0);
	}
	
	private boolean correctCoord(int x, int y, int z){
		for(int i = 0; i<allBlock.size(); i++){
			if(allBlock.get(i).getY()+3 >= y && ((x-allBlock.get(i).getX())*(x-allBlock.get(i).getX())+(z-allBlock.get(i).getZ())*(z-allBlock.get(i).getZ())) < 9){
				return false;	
			}
		}
		return true;
	}
}
