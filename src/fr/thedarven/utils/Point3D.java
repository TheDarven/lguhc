package fr.thedarven.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Point3D {
	private int x;
	private int y;
	private int z;
	
	public Point3D(int x,int y,int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setCoord(int x,int y,int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getZ(){
		return this.z;
	}
	
	public Location getLocation(){
		Location loc = new Location(Bukkit.getWorld("lguhc"), x, y, z);
		return loc;
		
	}
}
